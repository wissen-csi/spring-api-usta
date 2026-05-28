import { useState, useEffect, useRef, useCallback } from 'react'
import { QrCode, Camera, Clock, Scan } from 'lucide-react'
import Toast from '../components/Toast'
import { entryService } from '../services/api'
import { useAuth } from '../context/AuthContext'
import { Html5Qrcode } from 'html5-qrcode'

const statusLabels = {
  FALLIDO: 'Fallido',
  DENTRO: 'Dentro',
  FUERA: 'Fuera'
}

const statusColors = {
  FALLIDO: 'bg-red-100 text-red-700 border-red-200',
  DENTRO: 'bg-emerald-100 text-emerald-700 border-emerald-200',
  FUERA: 'bg-amber-100 text-amber-700 border-amber-200'
}

export default function Attendance() {
  const { user } = useAuth()
  const [scanning, setScanning] = useState(false)
  const [submitting, setSubmitting] = useState(false)
  const [entries, setEntries] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [toast, setToast] = useState({ show: false, message: '', type: '' })
  const [manualId, setManualId] = useState('')
  const scannerRef = useRef(null)
  const submittingRef = useRef(false)

  const loadEntries = async () => {
    setLoading(true)
    setError('')
    try {
      const res = await entryService.findAll({ page: 0, size: 50 })
      const all = res.data.content || []
      setEntries(all.filter(e => e.studentDni === user.dni))
    } catch (err) {
      console.error('Error loading entries:', err)
      setError('Error al conectar con el servidor.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadEntries() }, [])

  const stopScanner = useCallback(async () => {
    if (scannerRef.current) {
      try {
        await scannerRef.current.stop()
        await scannerRef.current.clear()
      } catch {}
      scannerRef.current = null
    }
  }, [])

  useEffect(() => {
    return () => { stopScanner() }
  }, [stopScanner])

  const onScanSuccess = useCallback(async (decodedText) => {
    if (submittingRef.current) return
    submittingRef.current = true
    setSubmitting(true)
    await stopScanner()
    setScanning(false)
    try {
      await entryService.saveByQr(decodedText, {
        studentId: user.dni,
        assitance: new Date().toISOString()
      })
      setToast({ show: true, message: 'Asistencia registrada exitosamente', type: 'success' })
      setTimeout(() => setToast({ show: false, message: '', type: '' }), 3000)
      await loadEntries()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al registrar asistencia'
      setToast({ show: true, message: typeof msg === 'string' ? msg : JSON.stringify(msg), type: 'error' })
      setTimeout(() => setToast({ show: false, message: '', type: '' }), 3000)
    } finally {
      submittingRef.current = false
      setSubmitting(false)
    }
  }, [user, stopScanner])

  const startScanner = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ video: true })
      stream.getTracks().forEach(t => t.stop())

      const cameras = await Html5Qrcode.getCameras()
      if (!cameras || cameras.length === 0) {
        setToast({ show: true, message: 'No se encontró ninguna cámara.', type: 'error' })
        setTimeout(() => setToast({ show: false, message: '', type: '' }), 3000)
        return
      }
      setScanning(true)

      setTimeout(async () => {
        try {
          const scanner = new Html5Qrcode('qr-reader')
          scannerRef.current = scanner
          await scanner.start(
            cameras[0].id,
            { fps: 10, qrbox: { width: 400, height: 300 } },
            onScanSuccess,
            () => {}
          )
        } catch (err) {
          setToast({ show: true, message: 'Error al iniciar la cámara: ' + (err.message || err), type: 'error' })
          setTimeout(() => setToast({ show: false, message: '', type: '' }), 3000)
          setScanning(false)
        }
      }, 100)
    } catch (err) {
      setToast({ show: true, message: 'Error al acceder a la cámara: ' + (err.message || err) + '. Asegúrate de permitir el acceso a la cámara en tu navegador.', type: 'error' })
      setTimeout(() => setToast({ show: false, message: '', type: '' }), 3000)
    }
  }

  const handleManualSubmit = async (e) => {
    e.preventDefault()
    if (!manualId.trim()) return
    setSubmitting(true)
    try {
      await entryService.saveByQr(manualId.trim(), {
        studentId: user.dni,
        assitance: new Date().toISOString()
      })
      setToast({ show: true, message: 'Asistencia registrada exitosamente', type: 'success' })
      setTimeout(() => setToast({ show: false, message: '', type: '' }), 3000)
      setManualId('')
      await loadEntries()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al registrar asistencia'
      setToast({ show: true, message: typeof msg === 'string' ? msg : JSON.stringify(msg), type: 'error' })
      setTimeout(() => setToast({ show: false, message: '', type: '' }), 3000)
    } finally {
      setSubmitting(false)
    }
  }

  const formatDate = (iso) => {
    const d = new Date(iso)
    return d.toLocaleDateString('es-ES', {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit'
    })
  }

  const getPracticeTitle = (entry) => {
    return entry.entryPracticeResponseDTO?.title || entry.entryPracticeId || '—'
  }

  return (
    <div className="space-y-6 relative">
      <Toast show={toast.show} message={toast.message} type={toast.type || 'success'} />

      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Registro de Asistencia</h1>
          <p className="text-slate-500 mt-1">Registra tu asistencia escaneando un código QR</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-5 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <div className="bg-white rounded-2xl p-6 border border-slate-100 shadow-sm">
            <div className="flex items-center gap-3 mb-4">
              <div className="w-10 h-10 bg-clinical-50 text-clinical-600 rounded-xl flex items-center justify-center">
                <QrCode className="w-5 h-5" />
              </div>
              <h2 className="text-lg font-bold text-slate-800">Escanear QR</h2>
            </div>

            <div id="qr-reader" className={`w-full aspect-[4/3] max-h-[400px] overflow-hidden rounded-xl bg-black ${scanning ? '' : 'hidden'}`} />

            {!scanning && (
              <div>
                <div className="bg-slate-50 rounded-xl p-8 border border-slate-100 mb-4 text-center">
                  <Scan className="w-16 h-16 text-slate-300 mx-auto mb-3" />
                  <p className="text-sm text-slate-500 font-medium">Presiona el botón para escanear</p>
                  <p className="text-xs text-slate-400 mt-1">Usa la cámara para escanear el código QR de la práctica</p>
                </div>
                <button
                  onClick={startScanner}
                  className="btn-primary w-full flex items-center justify-center gap-2 shadow-lg shadow-clinical-600/20"
                >
                  <Camera className="w-4 h-4" />
                  Iniciar Escáner
                </button>
              </div>
            )}

            {scanning && (
              <button
                onClick={async () => {
                  await stopScanner()
                  setScanning(false)
                }}
                className="mt-3 btn-secondary w-full"
              >
                Cancelar escaneo
              </button>
            )}

            <div className="mt-6 pt-6 border-t border-slate-100">
              <p className="text-xs font-semibold text-slate-500 uppercase tracking-wider mb-3">O ingresa el código manualmente</p>
              <form onSubmit={handleManualSubmit} className="flex gap-2">
                <input
                  type="text"
                  placeholder="Código QR..."
                  value={manualId}
                  onChange={(e) => setManualId(e.target.value)}
                  className="input-field flex-1"
                />
                <button
                  type="submit"
                  disabled={submitting || !manualId.trim()}
                  className="btn-primary"
                >
                  {submitting ? '...' : 'Registrar'}
                </button>
              </form>
            </div>
          </div>
        </div>

        <div className="lg:col-span-3">
          <div className="bg-white rounded-2xl p-6 border border-slate-100 shadow-sm">
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-clinical-50 text-clinical-600 rounded-xl flex items-center justify-center">
                  <Clock className="w-5 h-5" />
                </div>
                <h2 className="text-lg font-bold text-slate-800">Mis Asistencias</h2>
              </div>
            </div>

            {loading ? (
              <div className="flex justify-center py-10">
                <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-clinical-600"></div>
              </div>
            ) : error ? (
              <div className="bg-red-50 border border-red-200 text-red-600 p-4 rounded-xl text-center">
                {error}
              </div>
            ) : entries.length === 0 ? (
              <div className="text-center py-10">
                <Clock className="w-12 h-12 text-slate-300 mx-auto mb-3" />
                <p className="text-slate-500 font-medium">No tienes asistencias registradas</p>
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b border-slate-100">
                      <th className="text-left py-3 px-2 text-xs font-bold text-slate-500 uppercase tracking-wider">Práctica</th>
                      <th className="text-left py-3 px-2 text-xs font-bold text-slate-500 uppercase tracking-wider">Hora</th>
                      <th className="text-left py-3 px-2 text-xs font-bold text-slate-500 uppercase tracking-wider">Estado</th>
                    </tr>
                  </thead>
                  <tbody>
                    {entries.map((entry) => (
                      <tr key={entry.id} className="border-b border-slate-50 hover:bg-clinical-50/50 transition-colors">
                        <td className="py-3 px-2 font-semibold text-slate-800">{getPracticeTitle(entry)}</td>
                        <td className="py-3 px-2 text-slate-600">{formatDate(entry.assistance)}</td>
                        <td className="py-3 px-2">
                          <span className={`inline-block px-3 py-1 rounded-full text-xs font-semibold border ${statusColors[entry.statusEntry] || 'bg-slate-100 text-slate-600 border-slate-200'}`}>
                            {statusLabels[entry.statusEntry] || entry.statusEntry || 'DENTRO'}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}