import { useState, useEffect } from 'react'
import { Plus, Search, Calendar, Clock, Stethoscope, ClipboardList, ArrowRight, X, Sparkles, CheckCircle, Trash2, AlertTriangle, Users } from 'lucide-react'
import { rotationService, doctorService, groupService } from '../services/api'

export default function Tasks() {
  const [showModal, setShowModal] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [rotations, setRotations] = useState([])
  const [doctors, setDoctors] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [toast, setToast] = useState({ show: false, message: '' })
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)

  const [newRotation, setNewRotation] = useState({
    doctorId: '',
    typeRotation: 'OTHER',
    hospitalLocation: 'OTHER',
    startDate: '',
    completionDate: ''
  })

  const [groups, setGroups] = useState([])
  const [creatingGroup, setCreatingGroup] = useState(null)
  const [newGroup, setNewGroup] = useState({ name: '', capacity: 5 })

  const loadData = async () => {
    setLoading(true)
    setError('')
    try {
      const [rotationsRes, doctorsRes, groupsRes] = await Promise.all([
        rotationService.findAll({ page: 0, size: 50 }),
        doctorService.findAll({ page: 0, size: 50 }),
        groupService.findAll({ page: 0, size: 100 })
      ])
      setRotations(rotationsRes.data.content || [])
      setDoctors(doctorsRes.data.content || [])
      setGroups(groupsRes.data.content || [])
    } catch (err) {
      console.error('Error loading data in Tasks page:', err)
      setError('Error al conectar con el servidor. Por favor, intente de nuevo.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadData() }, [])

  const handleCreateGroup = async (rotationId, e) => {
    e.preventDefault()
    if (!newGroup.name || !newGroup.capacity) return
    try {
      await groupService.create({
        name: newGroup.name,
        rotationId,
        capacity: parseInt(newGroup.capacity)
      })
      setCreatingGroup(null)
      setNewGroup({ name: '', capacity: 5 })
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al crear el grupo'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    }
  }

  const handleDeleteGroup = async (group) => {
    if (!confirm(`¿Eliminar el grupo "${group.name}"?`)) return
    try {
      await groupService.delete(group.id)
      await loadData()
    } catch (err) {
      alert('Error al eliminar el grupo')
    }
  }

  const handleCreateRotation = async (e) => {
    e.preventDefault()
    if (!newRotation.doctorId || !newRotation.startDate || !newRotation.completionDate) {
      alert('Por favor complete todos los campos obligatorios.')
      return
    }

    try {
      await rotationService.create(
        {
          typeRotation: newRotation.typeRotation,
          hospitalLocation: newRotation.hospitalLocation,
          startDate: newRotation.startDate,
          completionDate: newRotation.completionDate
        },
        newRotation.doctorId
      )
      setShowModal(false)
      setToast({ show: true, message: 'Rotación médica programada exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 4000)
      setNewRotation({
        doctorId: '',
        typeRotation: 'OTHER',
        hospitalLocation: 'OTHER',
        startDate: '',
        completionDate: ''
      })
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al crear la rotación'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      await rotationService.delete(deleteTarget.id)
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
      setToast({ show: true, message: 'Rotación eliminada exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 4000)
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar la rotación'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setDeleting(false)
    }
  }

  const filteredRotations = rotations.filter(r => {
    const doctorName = r.doctorName || ''
    const hospital = r.hospitalLocation || ''
    const type = r.typeRotation || ''
    const query = searchTerm.toLowerCase()
    return doctorName.toLowerCase().includes(query) ||
           hospital.toLowerCase().includes(query) ||
           type.toLowerCase().includes(query)
  })

  const typeRotationOptions = [
    { value: 'OTHER', label: 'Otra' },
    { value: 'MEDICINA_INTERNA', label: 'Medicina Interna' },
    { value: 'CIRUGIA_GENERAL', label: 'Cirugía General' },
    { value: 'PEDIATRIA', label: 'Pediatría' },
    { value: 'GINECOLOGIA_OBSTETRICIA', label: 'Ginecología y Obstetricia' },
    { value: 'URGENCIAS', label: 'Urgencias' },
    { value: 'CARDIOLOGIA', label: 'Cardiología' },
    { value: 'NEUROLOGIA', label: 'Neurología' },
    { value: 'TRAUMATOLOGIA', label: 'Traumatología' },
    { value: 'CUIDADOS_INTENSIVOS', label: 'Cuidados Intensivos' }
  ]

  const hospitalLocationOptions = [
    { value: 'OTHER', label: 'Otra' },
    { value: 'HOSPITAL_UNIVERSITARIO', label: 'Hospital Universitario' },
    { value: 'CLINICA_PRIVADA', label: 'Clínica Privada' },
    { value: 'CENTRO_SALUD', label: 'Centro de Salud' },
    { value: 'HOSPITAL_REGIONAL', label: 'Hospital Regional' },
    { value: 'HOSPITAL_LOCAL', label: 'Hospital Local' },
    { value: 'INSTITUTO_ESPECIALIZADO', label: 'Instituto Especializado' },
    { value: 'CENTRO_ATENCION_PRIMARIA', label: 'Centro de Atención Primaria' }
  ]

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
          <h1 className="text-2xl font-bold text-slate-800">Rotaciones Médicas</h1>
          <p className="text-slate-500 mt-1">Asignación, gestión y seguimiento de rotaciones clínicas</p>
        </div>
        <button 
          onClick={() => setShowModal(true)}
          className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/20 hover:shadow-clinical-600/30 transition-all duration-300"
        >
          <Plus className="w-4 h-4" />
          Nueva Rotación
        </button>
      </div>

      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
        <input
          type="text"
          placeholder="Buscar por médico, ubicación o especialidad..."
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
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {filteredRotations.map((rotation) => (
            <div key={rotation.id} className="bg-white rounded-2xl p-6 border border-slate-100 shadow-sm hover:shadow-xl hover:border-clinical-200/50 hover:-translate-y-1 transform transition-all duration-300 group relative overflow-hidden">
              <div className="absolute top-0 left-0 w-2 h-full bg-clinical-500 opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
              
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 bg-clinical-50 text-clinical-600 rounded-xl flex items-center justify-center group-hover:bg-clinical-600 group-hover:text-white transition-all duration-300">
                    <ClipboardList className="w-5 h-5" />
                  </div>
                  <div>
                    <h3 className="font-semibold text-slate-800 group-hover:text-clinical-700 transition-colors">{rotation.id}</h3>
                    <p className="text-xs font-semibold text-slate-500 uppercase tracking-wider">{rotation.typeRotation || 'OTHER'}</p>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <span className={`px-3 py-1 rounded-full text-xs font-semibold border ${new Date(rotation.completionDate) < new Date() ? 'bg-slate-100 text-slate-600 border-slate-200' : 'bg-emerald-100 text-emerald-700 border-emerald-200'}`}>
                    {new Date(rotation.completionDate) < new Date() ? 'Completada' : 'Activa'}
                  </span>
                  <button
                    onClick={() => { setDeleteTarget(rotation); setShowDeleteConfirm(true) }}
                    className="p-2 hover:bg-red-50 rounded-lg transition-colors opacity-0 group-hover:opacity-100" title="Eliminar rotación"
                  >
                    <Trash2 className="w-4 h-4 text-slate-400 hover:text-red-500" />
                  </button>
                </div>
              </div>

              <div className="flex flex-col sm:flex-row items-stretch sm:items-center gap-3 mb-4">
                <div className="flex-1 p-3 bg-slate-50 rounded-xl border border-slate-100">
                  <div className="flex items-center gap-2 mb-1">
                    <Stethoscope className="w-4 h-4 text-clinical-500" />
                    <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider">Supervisor</span>
                  </div>
                  <p className="text-sm font-semibold text-slate-800 line-clamp-1">{rotation.doctorName}</p>
                </div>
              </div>

              <div className="flex flex-wrap items-center justify-between gap-4 pt-4 border-t border-slate-100 text-xs text-slate-500">
                <div className="flex items-center gap-2">
                  <Calendar className="w-4 h-4 text-slate-400" />
                  <span>Inicio: <strong className="text-slate-700">{rotation.startDate}</strong></span>
                </div>
                <div className="flex items-center gap-2">
                  <Clock className="w-4 h-4 text-slate-400" />
                  <span>Fin: <strong className="text-slate-700">{rotation.completionDate}</strong></span>
                </div>
                <span className="text-[10px] font-bold text-clinical-600 bg-clinical-50 px-2 py-0.5 rounded uppercase tracking-wider">
                  {rotation.hospitalLocation || 'OTHER'}
                </span>
              </div>

              {/* Groups section */}
              <div className="mt-4 pt-4 border-t border-slate-100">
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center gap-2">
                    <Users className="w-4 h-4 text-slate-400" />
                    <span className="text-xs font-bold text-slate-500 uppercase tracking-wider">Grupos</span>
                  </div>
                  <button
                    onClick={() => setCreatingGroup(creatingGroup === rotation.id ? null : rotation.id)}
                    className="text-xs font-semibold text-clinical-600 hover:text-clinical-700 flex items-center gap-1"
                  >
                    <Plus className="w-3 h-3" />
                    {creatingGroup === rotation.id ? 'Cancelar' : 'Agregar Grupo'}
                  </button>
                </div>

                {creatingGroup === rotation.id && (
                  <form onSubmit={(e) => handleCreateGroup(rotation.id, e)} className="flex items-center gap-2 mb-3 p-3 bg-slate-50 rounded-xl border border-slate-100">
                    <input
                      type="text"
                      placeholder="Nombre del grupo"
                      value={newGroup.name}
                      onChange={(e) => setNewGroup({ ...newGroup, name: e.target.value })}
                      className="input-field py-2 text-sm flex-1"
                      required
                    />
                    <input
                      type="number"
                      placeholder="Capacidad"
                      value={newGroup.capacity}
                      onChange={(e) => setNewGroup({ ...newGroup, capacity: e.target.value })}
                      className="input-field py-2 text-sm w-24"
                      min="1"
                      required
                    />
                    <button type="submit" className="px-3 py-2 bg-clinical-600 text-white text-sm font-semibold rounded-xl hover:bg-clinical-700 transition-colors">
                      Crear
                    </button>
                  </form>
                )}

                <div className="space-y-2">
                  {groups.filter(g => g.rotationId === rotation.id).map(group => (
                    <div key={group.id} className="flex items-center justify-between p-3 bg-clinical-50/40 rounded-xl border border-clinical-100/50">
                      <div className="flex items-center gap-3">
                        <Users className="w-4 h-4 text-clinical-500" />
                        <div>
                          <span className="text-sm font-semibold text-slate-700">{group.name}</span>
                          <span className="text-xs text-slate-500 ml-2">Capacidad: {group.capacity}</span>
                        </div>
                      </div>
                      <button
                        onClick={() => handleDeleteGroup(group)}
                        className="p-1.5 hover:bg-red-50 rounded-lg transition-colors" title="Eliminar grupo"
                      >
                        <Trash2 className="w-3.5 h-3.5 text-slate-400 hover:text-red-500" />
                      </button>
                    </div>
                  ))}
                  {groups.filter(g => g.rotationId === rotation.id).length === 0 && (
                    <p className="text-xs text-slate-400 text-center py-2">Sin grupos creados</p>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {!loading && filteredRotations.length === 0 && (
        <div className="text-center py-16 bg-white rounded-2xl border border-slate-100 shadow-sm">
          <ClipboardList className="w-12 h-12 text-slate-300 mx-auto mb-4" />
          <p className="text-slate-500 font-medium">No se encontraron rotaciones programadas</p>
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50">
              <div className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Nueva Rotación Médica</h2>
              </div>
              <button 
                onClick={() => setShowModal(false)}
                className="p-2 hover:bg-slate-100 rounded-xl transition-colors"
              >
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>

            <form onSubmit={handleCreateRotation}>
              <div className="p-6 space-y-4">
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Médico Supervisor *</label>
                  <select 
                    required
                    className="input-field py-3"
                    value={newRotation.doctorId}
                    onChange={(e) => setNewRotation({ ...newRotation, doctorId: e.target.value })}
                  >
                    <option value="">Seleccionar tutor clínico...</option>
                    {doctors.map(d => (
                      <option key={d.id} value={d.id}>{d.name} {d.lastName}</option>
                    ))}
                  </select>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Tipo de Rotación</label>
                    <select 
                      className="input-field py-3"
                      value={newRotation.typeRotation}
                      onChange={(e) => setNewRotation({ ...newRotation, typeRotation: e.target.value })}
                    >
                      {typeRotationOptions.map(o => (
                        <option key={o.value} value={o.value}>{o.label}</option>
                      ))}
                    </select>
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Ubicación Clínica</label>
                    <select 
                      className="input-field py-3"
                      value={newRotation.hospitalLocation}
                      onChange={(e) => setNewRotation({ ...newRotation, hospitalLocation: e.target.value })}
                    >
                      {hospitalLocationOptions.map(o => (
                        <option key={o.value} value={o.value}>{o.label}</option>
                      ))}
                    </select>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fecha Inicio *</label>
                    <input 
                      required
                      type="date" 
                      className="input-field py-3"
                      value={newRotation.startDate}
                      onChange={(e) => setNewRotation({ ...newRotation, startDate: e.target.value })}
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fecha Fin *</label>
                    <input 
                      required
                      type="date" 
                      className="input-field py-3"
                      value={newRotation.completionDate}
                      onChange={(e) => setNewRotation({ ...newRotation, completionDate: e.target.value })}
                    />
                  </div>
                </div>
              </div>

              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50">
                <button 
                  type="button"
                  onClick={() => setShowModal(false)}
                  className="btn-secondary"
                >
                  Cancelar
                </button>
                <button 
                  type="submit"
                  className="btn-primary shadow-lg shadow-clinical-600/10"
                >
                  Programar Rotación
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
                ¿Estás seguro de eliminar la rotación <strong>{deleteTarget?.id}</strong>?
              </p>
            </div>
            <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50">
              <button
                onClick={() => { setShowDeleteConfirm(false); setDeleteTarget(null) }}
                className="btn-secondary"
              >
                Cancelar
              </button>
              <button
                onClick={handleDelete}
                disabled={deleting}
                className="px-4 py-2.5 bg-red-600 hover:bg-red-700 text-white font-semibold rounded-xl transition-colors disabled:opacity-50 shadow-lg shadow-red-600/10"
              >
                {deleting ? 'Eliminando...' : 'Eliminar'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
