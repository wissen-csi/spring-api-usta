import { useState, useEffect } from 'react'
import { Plus, Trash2, X, CheckCircle, AlertTriangle, Pill, Calendar, FlaskConical } from 'lucide-react'
import { medicineService, medicalTreatmentService } from '../services/api'
import { useAuth } from '../context/AuthContext'

export default function MedicalTreatment() {
  const { user } = useAuth()
  const isStudent = user?.role?.includes('STUDENT')
  const isDoctor = user?.role?.includes('DOCTOR')

  const [activeTab, setActiveTab] = useState(isStudent ? 'medicines' : 'treatments')

  const [medicines, setMedicines] = useState([])
  const [treatments, setTreatments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [toast, setToast] = useState({ show: false, message: '' })

  const [showMedicineModal, setShowMedicineModal] = useState(false)
  const [newMedicine, setNewMedicine] = useState({ name: '', gramaje: '' })
  const [submittingMedicine, setSubmittingMedicine] = useState(false)

  const [showTreatmentModal, setShowTreatmentModal] = useState(false)
  const [newTreatment, setNewTreatment] = useState({ title: '', medicineId: '', startMedication: '', endMedication: '' })
  const [submittingTreatment, setSubmittingTreatment] = useState(false)

  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)

  const showToast = (message) => {
    setToast({ show: true, message })
    setTimeout(() => setToast({ show: false, message: '' }), 4000)
  }

  const loadData = async () => {
    setLoading(true)
    setError('')
    try {
      const [medRes, treatRes] = await Promise.all([
        medicineService.findAll({ page: 0, size: 100 }),
        medicalTreatmentService.findAll({ page: 0, size: 100 })
      ])
      setMedicines(medRes.data.content || [])
      let treatmentsData = treatRes.data.content || []
      if (isStudent && user?.dni) {
        treatmentsData = treatmentsData.filter(t => String(t.studentDni) === String(user.dni))
      }
      setTreatments(treatmentsData)
    } catch (err) {
      console.error('Error loading data:', err)
      setError('Error al conectar con el servidor.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadData() }, [])

  const handleCreateMedicine = async (e) => {
    e.preventDefault()
    if (!newMedicine.name || !newMedicine.gramaje) {
      alert('Complete todos los campos obligatorios.')
      return
    }
    setSubmittingMedicine(true)
    try {
      await medicineService.create(newMedicine)
      setShowMedicineModal(false)
      setNewMedicine({ name: '', gramaje: '' })
      showToast('Medicamento creado exitosamente')
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al crear medicamento'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setSubmittingMedicine(false)
    }
  }

  const handleCreateTreatment = async (e) => {
    e.preventDefault()
    if (!newTreatment.title || !newTreatment.medicineId || !newTreatment.startMedication || !newTreatment.endMedication) {
      alert('Complete todos los campos obligatorios.')
      return
    }
    if (newTreatment.endMedication < newTreatment.startMedication) {
      alert('La fecha de fin no puede ser anterior a la fecha de inicio.')
      return
    }
    setSubmittingTreatment(true)
    try {
      await medicalTreatmentService.create({
        title: newTreatment.title,
        medicineId: newTreatment.medicineId,
        startMedication: newTreatment.startMedication,
        endMedication: newTreatment.endMedication
      })
      setShowTreatmentModal(false)
      setNewTreatment({ title: '', medicineId: '', startMedication: '', endMedication: '' })
      showToast('Tratamiento creado exitosamente')
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al crear tratamiento'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setSubmittingTreatment(false)
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      if (showDeleteConfirm === 'medicine') {
        await medicineService.delete(deleteTarget.id)
        showToast('Medicamento eliminado exitosamente')
      } else {
        await medicalTreatmentService.delete(deleteTarget.medicineTreatmentId)
        showToast('Tratamiento eliminado exitosamente')
      }
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar'
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

  const getMedicineLabel = (id) => {
    const m = medicines.find(m => m.id === id)
    return m ? `${m.name} - ${m.gramaje}` : id
  }

  if (loading) {
    return (
      <div className="flex justify-center py-16">
        <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-clinical-600"></div>
      </div>
    )
  }

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

      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Tratamientos</h1>
          <p className="text-slate-500 mt-1">Gestión de medicamentos y tratamientos</p>
        </div>
        {isStudent && (
          <div className="flex gap-2">
            <button
              onClick={() => setShowMedicineModal(true)}
              className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/20"
            >
              <FlaskConical className="w-4 h-4" />
              Nuevo Medicamento
            </button>
            <button
              onClick={() => setShowTreatmentModal(true)}
              className="btn-secondary flex items-center gap-2"
            >
              <Plus className="w-4 h-4" />
              Nuevo Tratamiento
            </button>
          </div>
        )}
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-600 p-4 rounded-xl text-center">{error}</div>
      )}

      {isStudent && (
        <div className="flex gap-1 bg-slate-100 p-1 rounded-xl w-fit">
          <button
            onClick={() => setActiveTab('medicines')}
            className={`px-4 py-2 rounded-lg text-sm font-semibold transition-all ${activeTab === 'medicines' ? 'bg-white text-slate-800 shadow-sm' : 'text-slate-500 hover:text-slate-700'}`}
          >
            Medicamentos
          </button>
          <button
            onClick={() => setActiveTab('treatments')}
            className={`px-4 py-2 rounded-lg text-sm font-semibold transition-all ${activeTab === 'treatments' ? 'bg-white text-slate-800 shadow-sm' : 'text-slate-500 hover:text-slate-700'}`}
          >
            Mis Tratamientos
          </button>
        </div>
      )}

      {activeTab === 'medicines' && (
        <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
          {medicines.length === 0 ? (
            <div className="text-center py-16">
              <Pill className="w-12 h-12 text-slate-300 mx-auto mb-4" />
              <p className="text-slate-500 font-medium">No hay medicamentos registrados</p>
              <p className="text-slate-400 text-sm mt-1">Crea tu primer medicamento desde el botón superior.</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-100">
                    <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Nombre</th>
                    <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Gramaje</th>
                    <th className="text-right p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-50">
                  {medicines.map(m => (
                    <tr key={m.id} className="hover:bg-slate-50/50 transition-colors group">
                      <td className="p-4 font-medium text-slate-800">{m.name}</td>
                      <td className="p-4 text-slate-600">{m.gramaje}</td>
                      <td className="p-4 text-right">
                        <button
                          onClick={() => { setDeleteTarget(m); setShowDeleteConfirm('medicine') }}
                          className="p-2 hover:bg-red-50 rounded-lg transition-colors opacity-0 group-hover:opacity-100"
                          title="Eliminar medicamento"
                        >
                          <Trash2 className="w-4 h-4 text-slate-400 hover:text-red-500" />
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {activeTab === 'treatments' && (
        <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
          {treatments.length === 0 ? (
            <div className="text-center py-16">
              <Calendar className="w-12 h-12 text-slate-300 mx-auto mb-4" />
              <p className="text-slate-500 font-medium">No hay tratamientos registrados</p>
              <p className="text-slate-400 text-sm mt-1">Crea un medicamento primero y luego asigna un tratamiento.</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-100">
                    <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Título</th>
                    <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Medicamento</th>
                    <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Inicio</th>
                    <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Fin</th>
                    <th className="text-right p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-50">
                  {treatments.map(t => (
                    <tr key={t.medicineTreatmentId} className="hover:bg-slate-50/50 transition-colors group">
                      <td className="p-4 font-medium text-slate-800">{t.title}</td>
                      <td className="p-4 text-slate-600">{getMedicineLabel(t.medicineId)}</td>
                      <td className="p-4">
                        <div className="flex items-center gap-2">
                          <Calendar className="w-3.5 h-3.5 text-slate-400" />
                          <span className="text-slate-700">{formatDate(t.startMedication)}</span>
                        </div>
                      </td>
                      <td className="p-4">
                        <div className="flex items-center gap-2">
                          <Calendar className="w-3.5 h-3.5 text-slate-400" />
                          <span className="text-slate-700">{formatDate(t.endMedication)}</span>
                        </div>
                      </td>
                      <td className="p-4 text-right">
                        <button
                          onClick={() => { setDeleteTarget(t); setShowDeleteConfirm('treatment') }}
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
          )}
        </div>
      )}

      {!isStudent && (
        <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
          {treatments.length === 0 ? (
            <div className="text-center py-16">
              <Calendar className="w-12 h-12 text-slate-300 mx-auto mb-4" />
              <p className="text-slate-500 font-medium">No hay tratamientos registrados</p>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-100">
                    <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Título</th>
                    <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Estudiante</th>
                    <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Medicamento</th>
                    <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Inicio</th>
                    <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Fin</th>
                    <th className="text-right p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-50">
                  {treatments.map(t => (
                    <tr key={t.medicineTreatmentId} className="hover:bg-slate-50/50 transition-colors group">
                      <td className="p-4 font-medium text-slate-800">{t.title}</td>
                      <td className="p-4 text-slate-600">{t.studentDni || t.studentId}</td>
                      <td className="p-4 text-slate-600">{getMedicineLabel(t.medicineId)}</td>
                      <td className="p-4">
                        <div className="flex items-center gap-2">
                          <Calendar className="w-3.5 h-3.5 text-slate-400" />
                          <span className="text-slate-700">{formatDate(t.startMedication)}</span>
                        </div>
                      </td>
                      <td className="p-4">
                        <div className="flex items-center gap-2">
                          <Calendar className="w-3.5 h-3.5 text-slate-400" />
                          <span className="text-slate-700">{formatDate(t.endMedication)}</span>
                        </div>
                      </td>
                      <td className="p-4 text-right">
                        <button
                          onClick={() => { setDeleteTarget(t); setShowDeleteConfirm('treatment') }}
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
          )}
        </div>
      )}

      {showMedicineModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden border border-slate-100">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50">
              <div className="flex items-center gap-2">
                <Pill className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Nuevo Medicamento</h2>
              </div>
              <button onClick={() => setShowMedicineModal(false)} className="p-2 hover:bg-slate-100 rounded-xl transition-colors">
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>
            <form onSubmit={handleCreateMedicine}>
              <div className="p-6 space-y-4">
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Nombre *</label>
                  <input required type="text" placeholder="Ej: Paracetamol" className="input-field py-3"
                    value={newMedicine.name}
                    onChange={(e) => setNewMedicine({ ...newMedicine, name: e.target.value })} />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Gramaje *</label>
                  <input required type="text" placeholder="Ej: 500 mg" className="input-field py-3"
                    value={newMedicine.gramaje}
                    onChange={(e) => setNewMedicine({ ...newMedicine, gramaje: e.target.value })} />
                </div>
              </div>
              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50">
                <button type="button" onClick={() => setShowMedicineModal(false)} className="btn-secondary">Cancelar</button>
                <button type="submit" disabled={submittingMedicine} className="btn-primary disabled:opacity-50">
                  {submittingMedicine ? 'Creando...' : 'Crear Medicamento'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showTreatmentModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden border border-slate-100">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50">
              <div className="flex items-center gap-2">
                <Calendar className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Nuevo Tratamiento</h2>
              </div>
              <button onClick={() => setShowTreatmentModal(false)} className="p-2 hover:bg-slate-100 rounded-xl transition-colors">
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>
            <form onSubmit={handleCreateTreatment}>
              <div className="p-6 space-y-4">
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Título *</label>
                  <input required type="text" placeholder="Ej: Tratamiento para fiebre" className="input-field py-3"
                    value={newTreatment.title}
                    onChange={(e) => setNewTreatment({ ...newTreatment, title: e.target.value })} />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Medicamento *</label>
                  <select required className="input-field py-3"
                    value={newTreatment.medicineId}
                    onChange={(e) => setNewTreatment({ ...newTreatment, medicineId: e.target.value })}>
                    <option value="">Seleccione un medicamento</option>
                    {medicines.map(m => (
                      <option key={m.id} value={m.id}>{m.name} - {m.gramaje}</option>
                    ))}
                  </select>
                </div>
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fecha Inicio *</label>
                    <input required type="date" className="input-field py-3"
                      value={newTreatment.startMedication}
                      onChange={(e) => setNewTreatment({ ...newTreatment, startMedication: e.target.value })} />
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fecha Fin *</label>
                    <input required type="date" className="input-field py-3"
                      value={newTreatment.endMedication}
                      min={newTreatment.startMedication || ''}
                      onChange={(e) => setNewTreatment({ ...newTreatment, endMedication: e.target.value })} />
                  </div>
                </div>
              </div>
              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50">
                <button type="button" onClick={() => setShowTreatmentModal(false)} className="btn-secondary">Cancelar</button>
                <button type="submit" disabled={submittingTreatment} className="btn-primary disabled:opacity-50">
                  {submittingTreatment ? 'Creando...' : 'Crear Tratamiento'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showDeleteConfirm && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-md border border-slate-100">
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
                {showDeleteConfirm === 'medicine'
                  ? `¿Estás seguro de eliminar el medicamento "${deleteTarget?.name}"?`
                  : `¿Estás seguro de eliminar el tratamiento "${deleteTarget?.title || deleteTarget?.medicineTreatmentId}"?`
                }
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
