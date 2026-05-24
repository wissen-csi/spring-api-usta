import { useState, useEffect } from 'react'
import { QrCode, Camera, CheckCircle, Clock, X } from 'lucide-react'
import { entryService } from '../services/api'
import { useAuth } from '../context/AuthContext'

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
  const [entryPracticeId, setEntryPracticeId] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [entries, setEntries] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [toast, setToast] = useState({ show: false, message: '', type: '' })

  const loadEntries = async () => {
    setLoading(true)
    setError('')
    try {
      const res = await entryService.findAll({ page: 0, size: 50 })
      const all = res.data.content || []
      setEntries(all.filter(e => e.studentId === user.dni))
    } catch (err) {
      console.error('Error loading entries:', err)
      setError('Error al conectar con el servidor.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadEntries() }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!entryPracticeId.trim()) return
    setSubmitting(true)
    try {
      await entryService.create({
        studentId: user.dni,
        entryPracticeId: entryPracticeId.trim(),
        assistance: new Date().toISOString()
      })
      setToast({ show: true, message: 'Asistencia registrada exitosamente', type: 'success' })
      setTimeout(() => setToast({ show: false, message: '', type: '' }), 4000)
      setEntryPracticeId('')
      await loadEntries()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al registrar asistencia'
      setToast({ show: true, message: typeof msg === 'string' ? msg : JSON.stringify(msg), type: 'error' })
      setTimeout(() => setToast({ show: false, message: '', type: '' }), 4000)
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

  return (
    <div className="space-y-6 relative">
      {toast.show && (
        <div className={`fixed top-5 right-5 z-[100] flex items-center gap-3 bg-white border shadow-2xl rounded-2xl px-6 py-4 animate-in slide-in-from-top duration-300 ${toast.type === 'error' ? 'border-red-200' : 'border-emerald-200'}`}>
          <div className={`w-10 h-10 rounded-full flex items-center justify-center ${toast.type === 'error' ? 'bg-red-50 text-red-600' : 'bg-emerald-50 text-emerald-600'}`}>
            {toast.type === 'error' ? <X className="w-6 h-6" /> : <CheckCircle className="w-6 h-6" />}
          </div>
          <div>
            <h4 className="font-semibold text-slate-800 text-sm">{toast.type === 'error' ? 'Error' : 'Operación Completada'}</h4>
            <p className="text-xs text-slate-500">{toast.message}</p>
          </div>
        </div>
      )}

      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Registro de Asistencia</h1>
          <p className="text-slate-500 mt-1">Registra tu asistencia escaneando un código QR o ingresando el ID de la práctica</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-5 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <div className="bg-white rounded-2xl p-6 border border-slate-100 shadow-sm">
            <div className="flex items-center gap-3 mb-4">
              <div className="w-10 h-10 bg-clinical-50 text-clinical-600 rounded-xl flex items-center justify-center">
                <QrCode className="w-5 h-5" />
              </div>
              <h2 className="text-lg font-bold text-slate-800">Registrar Asistencia</h2>
            </div>

            <div className="bg-slate-50 rounded-xl p-4 border border-slate-100 mb-4 text-center">
              <Camera className="w-12 h-12 text-slate-300 mx-auto mb-2" />
              <p className="text-sm text-slate-500 font-medium">Escanea el código QR de la práctica</p>
              <p className="text-xs text-slate-400 mt-1">O ingresa manualmente el ID de la práctica a continuación</p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">ID de Práctica</label>
                <input
                  type="text"
                  placeholder="Ingresa el ID de la práctica..."
                  value={entryPracticeId}
                  onChange={(e) => setEntryPracticeId(e.target.value)}
                  className="input-field"
                  required
                />
              </div>
              <button
                type="submit"
                disabled={submitting || !entryPracticeId.trim()}
                className="btn-primary w-full flex items-center justify-center gap-2 shadow-lg shadow-clinical-600/20"
              >
                {submitting ? (
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white" />
                ) : (
                  <CheckCircle className="w-4 h-4" />
                )}
                {submitting ? 'Registrando...' : 'Registrar Asistencia'}
              </button>
            </form>
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
                      <th className="text-left py-3 px-2 text-xs font-bold text-slate-500 uppercase tracking-wider">ID Práctica</th>
                      <th className="text-left py-3 px-2 text-xs font-bold text-slate-500 uppercase tracking-wider">Hora de Asistencia</th>
                      <th className="text-left py-3 px-2 text-xs font-bold text-slate-500 uppercase tracking-wider">Estado</th>
                    </tr>
                  </thead>
                  <tbody>
                    {entries.map((entry) => (
                      <tr key={entry.id} className="border-b border-slate-50 hover:bg-slate-50/50 transition-colors">
                        <td className="py-3 px-2 font-semibold text-slate-800">{entry.entryPracticeId}</td>
                        <td className="py-3 px-2 text-slate-600">{formatDate(entry.assistance)}</td>
                        <td className="py-3 px-2">
                          <span className={`inline-block px-3 py-1 rounded-full text-xs font-semibold border ${statusColors[entry.status] || 'bg-slate-100 text-slate-600 border-slate-200'}`}>
                            {statusLabels[entry.status] || entry.status}
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
