import { useState, useEffect } from 'react'
import { Plus, Search, Trash2, X, Sparkles, CheckCircle, AlertTriangle, Pill, Calendar } from 'lucide-react'
import { medicalTreatmentService } from '../services/api'
import { useAuth } from '../context/AuthContext'

export default function MedicalTreatment() {
  const { user } = useAuth()
  const isDoctor = user?.role?.includes('DOCTOR')

  const [showModal, setShowModal] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [treatments, setTreatments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [toast, setToast] = useState({ show: false, message: '' })
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)
  const [submitting, setSubmitting] = useState(false)

  const [newTreatment, setNewTreatment] = useState({
    medicineId: '',
    studentId: '',
    startMedication: '',
    endMedication: ''
  })

  const showToast = (message) => {
    setToast({ show: true, message })
    setTimeout(() => setToast({ show: false, message: '' }), 4000)
  }

  const loadData = async () => {
    setLoading(true)
    setError('')
    try {
      const res = await medicalTreatmentService.findAll({ page: 0, size: 100 })
      setTreatments(res.data.content || [])
    } catch (err) {
      console.error('Error loading treatments:', err)
      setError('Error al conectar con el servidor. Por favor, intente de nuevo.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadData() }, [])

  const handleCreate = async (e) => {
    e.preventDefault()
    if (!newTreatment.medicineId || !newTreatment.studentId || !newTreatment.startMedication || !newTreatment.endMedication) {
      alert('Por favor complete todos los campos obligatorios.')
      return
    }
    setSubmitting(true)
    try {
      await medicalTreatmentService.create(newTreatment)
      setShowModal(false)
      setNewTreatment({ medicineId: '', studentId: '', startMedication: '', endMedication: '' })
      showToast('Tratamiento creado exitosamente')
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al crear el tratamiento'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setSubmitting(false)
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      await medicalTreatmentService.delete(deleteTarget.medicineTreatmentId)
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
      showToast('Tratamiento eliminado exitosamente')
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar el tratamiento'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setDeleting(false)
    }
  }

  const formatDate = (dateStr) => {
    if (!dateStr) return ''
    const d = new Date(dateStr)
    return d.toLocaleDateString('es-CO', { year: 'numeric', month: '2-digit', day: '2-digit' })
  }

  const filteredTreatments = treatments.filter(t => {
    const query = searchTerm.toLowerCase()
    return (t.studentId || '').toLowerCase().includes(query) ||
           (t.medicineId || '').toLowerCase().includes(query) ||
           (t.medicineTreatmentId || '').toLowerCase().includes(query)
  })

  return (
    <div className="space-y-6 relative">
      {toast.show && (
        <div className="fixed top-5 right-5 z-[100] flex items-center gap-3 bg-white border border-emerald-200 shadow-2xl rounded-2xl px-6 py-4 animate-in slide-in-from-top duration-300">
          <div className="w-10 h-10 bg-emerald-50 text-emerald-600 rounded-full flex items-center justify-center">
            <CheckCircle className="w-6 h-6" />
          </div>
          <div>
            <h4 className="font-semibold text-slate-800 text-sm">Operación Completada</h4>
            <p className="text-xs text-slate-500">{toast.message}</p>
          </div>
        </div>
      )}

      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Tratamientos Médicos</h1>
          <p className="text-slate-500 mt-1">Gestión de tratamientos y medicación de estudiantes</p>
        </div>
        {isDoctor && (
          <button
            onClick={() => setShowModal(true)}
            className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/20 hover:shadow-clinical-600/30 transition-all duration-300"
          >
            <Plus className="w-4 h-4" />
            Nuevo Tratamiento
          </button>
        )}
      </div>

      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
        <input
          type="text"
          placeholder="Buscar por estudiante, medicamento o ID..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="input-field pl-10 focus:ring-clinical-500 focus:border-clinical-500"
        />
      </div>

      {loading ? (
        <div className="flex justify-center py-16">
          <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-clinical-600"></div>
        </div>
      ) : error ? (
        <div className="bg-red-50 border border-red-200 text-red-600 p-4 rounded-xl text-center">
          {error}
        </div>
      ) : (
        <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-100 bg-slate-50/50">
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">ID Tratamiento</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Estudiante</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Medicamento</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Inicio</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Fin</th>
                  <th className="text-right text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-50">
                {filteredTreatments.map((t) => (
                  <tr key={t.medicineTreatmentId} className="hover:bg-slate-50/50 transition-colors group">
                    <td className="px-6 py-4 font-mono text-xs text-slate-500">{t.medicineTreatmentId}</td>
                    <td className="px-6 py-4 font-semibold text-slate-800">{t.studentId}</td>
                    <td className="px-6 py-4 text-slate-600">{t.medicineId}</td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <Calendar className="w-3.5 h-3.5 text-slate-400" />
                        <span className="text-slate-700">{formatDate(t.startMedication)}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <Calendar className="w-3.5 h-3.5 text-slate-400" />
                        <span className="text-slate-700">{formatDate(t.endMedication)}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <button
                        onClick={() => { setDeleteTarget(t); setShowDeleteConfirm(true) }}
                        className="p-2 hover:bg-red-50 rounded-lg transition-colors opacity-0 group-hover:opacity-100"
                        title="Eliminar tratamiento"
                      >
                        <Trash2 className="w-4 h-4 text-slate-400 hover:text-red-500" />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {!loading && filteredTreatments.length === 0 && (
        <div className="text-center py-16 bg-white rounded-2xl border border-slate-100 shadow-sm">
          <Pill className="w-12 h-12 text-slate-300 mx-auto mb-4" />
          <p className="text-slate-500 font-medium">No se encontraron tratamientos registrados</p>
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50">
              <div className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Nuevo Tratamiento</h2>
              </div>
              <button
                onClick={() => setShowModal(false)}
                className="p-2 hover:bg-slate-100 rounded-xl transition-colors"
              >
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>

            <form onSubmit={handleCreate}>
              <div className="p-6 space-y-4">
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">DNI del Estudiante *</label>
                  <input
                    required
                    type="text"
                    placeholder="DNI del estudiante"
                    className="input-field py-3"
                    value={newTreatment.studentId}
                    onChange={(e) => setNewTreatment({ ...newTreatment, studentId: e.target.value })}
                  />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">ID del Medicamento *</label>
                  <input
                    required
                    type="text"
                    placeholder="ID del medicamento"
                    className="input-field py-3"
                    value={newTreatment.medicineId}
                    onChange={(e) => setNewTreatment({ ...newTreatment, medicineId: e.target.value })}
                  />
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fecha Inicio *</label>
                    <input
                      required
                      type="date"
                      className="input-field py-3"
                      value={newTreatment.startMedication}
                      onChange={(e) => setNewTreatment({ ...newTreatment, startMedication: e.target.value })}
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fecha Fin *</label>
                    <input
                      required
                      type="date"
                      className="input-field py-3"
                      value={newTreatment.endMedication}
                      onChange={(e) => setNewTreatment({ ...newTreatment, endMedication: e.target.value })}
                    />
                  </div>
                </div>
              </div>

              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">Cancelar</button>
                <button type="submit" disabled={submitting} className="btn-primary shadow-lg shadow-clinical-600/10 disabled:opacity-50">
                  {submitting ? 'Creando...' : 'Crear Tratamiento'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showDeleteConfirm && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-md border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center gap-3 p-6 border-b border-slate-100">
              <div className="w-10 h-10 bg-red-50 text-red-600 rounded-full flex items-center justify-center">
                <AlertTriangle className="w-5 h-5" />
              </div>
              <div>
                <h2 className="text-lg font-bold text-slate-800">Confirmar Eliminación</h2>
                <p className="text-sm text-slate-500">Esta acción no se puede deshacer</p>
              </div>
            </div>
            <div className="p-6">
              <p className="text-slate-600">
                ¿Estás seguro de eliminar el tratamiento <strong>{deleteTarget?.medicineTreatmentId}</strong>?
              </p>
            </div>
            <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50">
              <button onClick={() => { setShowDeleteConfirm(false); setDeleteTarget(null) }} className="btn-secondary">Cancelar</button>
              <button onClick={handleDelete} disabled={deleting} className="px-4 py-2.5 bg-red-600 hover:bg-red-700 text-white font-semibold rounded-xl transition-colors disabled:opacity-50 shadow-lg shadow-red-600/10">
                {deleting ? 'Eliminando...' : 'Eliminar'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
