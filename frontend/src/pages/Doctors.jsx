import { useState, useEffect } from 'react'
import { Search, Plus, Mail, Phone, Stethoscope, Users, X, CheckCircle, Sparkles, Trash2, AlertTriangle, Pencil } from 'lucide-react'
import { doctorService, universityService } from '../services/api'

const maritalStatusOptions = [
  { value: 'MARRIED', label: 'Casado/a' },
  { value: 'DIVORCED', label: 'Divorciado/a' },
  { value: 'FREE_UNION', label: 'Unión Libre' },
  { value: 'OTHER', label: 'Otro' }
]

const typeBloodOptions = [
  { value: 'O_POSITIVE', label: 'O+' },
  { value: 'O_NEGATIVE', label: 'O-' },
  { value: 'A_POSITIVE', label: 'A+' },
  { value: 'A_NEGATIVE', label: 'A-' },
  { value: 'B_POSITIVE', label: 'B+' },
  { value: 'B_NEGATIVE', label: 'B-' },
  { value: 'AB_POSITIVE', label: 'AB+' },
  { value: 'AB_NEGATIVE', label: 'AB-' }
]

const emptyForm = {
  name: '',
  lastName: '',
  dni: '',
  email: '',
  phoneNumber: '',
  password: '',
  maritalStatus: 'OTHER',
  placeBirth: { address: '', city: '', department: '' },
  residenceAddress: { address: '', city: '', department: '' },
  typeBlood: 'O_POSITIVE',
  weight: '',
  imc: '',
  specialty: 'OTHER',
  universityId: ''
}

export default function Doctors() {
  const [searchTerm, setSearchTerm] = useState('')
  const [doctors, setDoctors] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [creating, setCreating] = useState(false)
  const [toast, setToast] = useState({ show: false, message: '' })
  const [universities, setUniversities] = useState([])
  const [newDoctor, setNewDoctor] = useState(emptyForm)
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)
  const [showEditModal, setShowEditModal] = useState(false)
  const [editTarget, setEditTarget] = useState(null)
  const [editing, setEditing] = useState(false)

  useEffect(() => {
    const fetchDoctors = async () => {
      setLoading(true)
      setError('')
      try {
        const { data } = await doctorService.findAll({ page: 0, size: 50 })
        setDoctors(data.content)
      } catch {
        setError('Error al cargar los médicos')
      } finally {
        setLoading(false)
      }
    }
    fetchDoctors()
  }, [])

  const openCreateModal = async () => {
    setShowModal(true)
    setNewDoctor(emptyForm)
    try {
      const { data } = await universityService.findAll()
      setUniversities(data.content || data)
    } catch (err) {
      console.error('Error loading universities:', err)
    }
  }

  const filteredDoctors = doctors.filter(doctor => {
    const fullName = `${doctor.name || ''} ${doctor.lastName || ''}`.trim()
    return fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      doctor.specialty?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      doctor.email?.toLowerCase().includes(searchTerm.toLowerCase())
  })

  const handleCreateDoctor = async (e) => {
    e.preventDefault()
    if (!newDoctor.name || !newDoctor.lastName || !newDoctor.dni || !newDoctor.email || !newDoctor.password) {
      alert('Por favor complete todos los campos obligatorios.')
      return
    }

    setCreating(true)
    try {
      await doctorService.create({
        ...newDoctor,
        weight: parseFloat(newDoctor.weight) || 0,
        imc: parseFloat(newDoctor.imc) || 0
      })
      setShowModal(false)
      setToast({ show: true, message: 'Médico creado exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 4000)

      const { data } = await doctorService.findAll({ page: 0, size: 50 })
      setDoctors(data.content)
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al crear el médico'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setCreating(false)
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      await doctorService.delete(deleteTarget.id)
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
      setToast({ show: true, message: 'Médico eliminado exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 4000)
      const { data } = await doctorService.findAll({ page: 0, size: 50 })
      setDoctors(data.content)
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar el médico'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setDeleting(false)
    }
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

      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Médicos Supervisores</h1>
          <p className="text-slate-500 mt-1">Gestión de médicos y supervisores del programa</p>
        </div>
        <button
          onClick={openCreateModal}
          className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/20 hover:shadow-clinical-600/30 transition-all duration-300"
        >
          <Plus className="w-4 h-4" />
          Agregar Médico
        </button>
      </div>

      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
        <input
          type="text"
          placeholder="Buscar médicos por nombre, especialidad o correo..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="input-field pl-10 focus:ring-clinical-500 focus:border-clinical-500"
        />
      </div>

      {loading ? (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-clinical-600"></div>
        </div>
      ) : error ? (
        <div className="text-center py-12 text-red-600">{error}</div>
      ) : (
        <>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {filteredDoctors.map((doctor) => {
              const fullName = `${doctor.name || ''} ${doctor.lastName || ''}`.trim();
              return (
                <div key={doctor.id} className="bg-white rounded-2xl p-6 border border-slate-100 shadow-sm hover:shadow-xl hover:border-clinical-200/60 hover:-translate-y-1 transform transition-all duration-300 group relative overflow-hidden">
                  <div className="absolute top-0 left-0 w-2 h-full bg-clinical-500 opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
                  <div className="flex items-start justify-between mb-4">
                    <div className="flex items-center gap-4">
                      <div className="w-14 h-14 bg-clinical-50 text-clinical-600 rounded-2xl flex items-center justify-center group-hover:bg-clinical-600 group-hover:text-white transition-all duration-300">
                        <Stethoscope className="w-7 h-7" />
                      </div>
                      <div>
                        <h3 className="font-semibold text-slate-800 group-hover:text-clinical-700 transition-colors text-base line-clamp-1">
                          {fullName || 'Sin Nombre'}
                        </h3>
                        <p className="text-sm font-medium text-slate-500">{doctor.specialty || 'General'}</p>
                      </div>
                    </div>
                    <button
                      onClick={() => { setEditTarget(doctor); setShowEditModal(true) }}
                      className="p-2 hover:bg-clinical-50 rounded-lg transition-colors opacity-0 group-hover:opacity-100" title="Editar médico"
                    >
                      <Pencil className="w-4 h-4 text-slate-400 hover:text-clinical-600" />
                    </button>
                    <button
                      onClick={() => { setDeleteTarget(doctor); setShowDeleteConfirm(true) }}
                      className="p-2 hover:bg-red-50 rounded-lg transition-colors opacity-0 group-hover:opacity-100" title="Eliminar médico"
                    >
                      <Trash2 className="w-4 h-4 text-slate-400 hover:text-red-500" />
                    </button>
                  </div>

                  <div className="space-y-3 mb-5">
                    <div className="flex items-center gap-3 text-sm text-slate-600">
                      <div className="w-8 h-8 bg-slate-50 rounded-lg flex items-center justify-center">
                        <Mail className="w-4 h-4 text-slate-400" />
                      </div>
                      <span className="truncate max-w-[200px]" title={doctor.email}>{doctor.email || 'N/A'}</span>
                    </div>
                    <div className="flex items-center gap-3 text-sm text-slate-600">
                      <div className="w-8 h-8 bg-slate-50 rounded-lg flex items-center justify-center">
                        <Phone className="w-4 h-4 text-slate-400" />
                      </div>
                      <span>{doctor.phoneNumber || 'N/A'}</span>
                    </div>
                  </div>

                  <div className="flex items-center justify-between pt-4 border-t border-slate-100">
                    <div className="flex items-center gap-2 text-sm text-slate-500">
                      <Users className="w-4 h-4 text-slate-400" />
                      <span className="font-medium text-slate-600 truncate max-w-[120px]">{doctor.universityName || 'USTA'}</span>
                    </div>
                    <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-emerald-100 text-emerald-700">
                      Activo
                    </span>
                  </div>
                </div>
              );
            })}
          </div>

          {filteredDoctors.length === 0 && (
            <div className="text-center py-12">
              <Stethoscope className="w-12 h-12 text-slate-300 mx-auto mb-4" />
              <p className="text-slate-500">No se encontraron médicos</p>
            </div>
          )}
        </>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50 sticky top-0 z-10">
              <div className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Agregar Médico</h2>
              </div>
              <button
                onClick={() => setShowModal(false)}
                className="p-2 hover:bg-slate-100 rounded-xl transition-colors"
              >
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>

            <form onSubmit={handleCreateDoctor}>
              <div className="p-6 space-y-6">
                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Personal</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Nombre *</label>
                      <input required type="text" placeholder="Carlos" className="input-field py-3"
                        value={newDoctor.name}
                        onChange={(e) => setNewDoctor({ ...newDoctor, name: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Apellido *</label>
                      <input required type="text" placeholder="Mendoza" className="input-field py-3"
                        value={newDoctor.lastName}
                        onChange={(e) => setNewDoctor({ ...newDoctor, lastName: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">DNI *</label>
                      <input required type="text" placeholder="1234567890" className="input-field py-3"
                        value={newDoctor.dni}
                        onChange={(e) => setNewDoctor({ ...newDoctor, dni: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Teléfono *</label>
                      <input required type="text" placeholder="3001234567" maxLength={11} className="input-field py-3"
                        value={newDoctor.phoneNumber}
                        onChange={(e) => setNewDoctor({ ...newDoctor, phoneNumber: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Correo Electrónico *</label>
                      <input required type="email" placeholder="carlos@correo.com" className="input-field py-3"
                        value={newDoctor.email}
                        onChange={(e) => setNewDoctor({ ...newDoctor, email: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Contraseña *</label>
                      <input required type="password" placeholder="Contraseña" className="input-field py-3"
                        value={newDoctor.password}
                        onChange={(e) => setNewDoctor({ ...newDoctor, password: e.target.value })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Demográfica</h3>
                  <div className="grid grid-cols-2 gap-4 mb-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Estado Civil *</label>
                      <select className="input-field py-3"
                        value={newDoctor.maritalStatus}
                        onChange={(e) => setNewDoctor({ ...newDoctor, maritalStatus: e.target.value })}>
                        {maritalStatusOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Tipo de Sangre *</label>
                      <select className="input-field py-3"
                        value={newDoctor.typeBlood}
                        onChange={(e) => setNewDoctor({ ...newDoctor, typeBlood: e.target.value })}>
                        {typeBloodOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Peso (kg) *</label>
                      <input required type="number" step="0.1" placeholder="70.5" className="input-field py-3"
                        value={newDoctor.weight}
                        onChange={(e) => setNewDoctor({ ...newDoctor, weight: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">IMC *</label>
                      <input required type="number" step="0.1" placeholder="22.5" className="input-field py-3"
                        value={newDoctor.imc}
                        onChange={(e) => setNewDoctor({ ...newDoctor, imc: e.target.value })} />
                    </div>
                  </div>

                  <div className="mb-4">
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Lugar de Nacimiento *</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input required type="text" placeholder="Dirección" className="input-field py-3"
                        value={newDoctor.placeBirth.address}
                        onChange={(e) => setNewDoctor({ ...newDoctor, placeBirth: { ...newDoctor.placeBirth, address: e.target.value } })} />
                      <input required type="text" placeholder="Ciudad" className="input-field py-3"
                        value={newDoctor.placeBirth.city}
                        onChange={(e) => setNewDoctor({ ...newDoctor, placeBirth: { ...newDoctor.placeBirth, city: e.target.value } })} />
                      <input required type="text" placeholder="Departamento" className="input-field py-3"
                        value={newDoctor.placeBirth.department}
                        onChange={(e) => setNewDoctor({ ...newDoctor, placeBirth: { ...newDoctor.placeBirth, department: e.target.value } })} />
                    </div>
                  </div>

                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Dirección de Residencia *</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input required type="text" placeholder="Dirección" className="input-field py-3"
                        value={newDoctor.residenceAddress.address}
                        onChange={(e) => setNewDoctor({ ...newDoctor, residenceAddress: { ...newDoctor.residenceAddress, address: e.target.value } })} />
                      <input required type="text" placeholder="Ciudad" className="input-field py-3"
                        value={newDoctor.residenceAddress.city}
                        onChange={(e) => setNewDoctor({ ...newDoctor, residenceAddress: { ...newDoctor.residenceAddress, city: e.target.value } })} />
                      <input required type="text" placeholder="Departamento" className="input-field py-3"
                        value={newDoctor.residenceAddress.department}
                        onChange={(e) => setNewDoctor({ ...newDoctor, residenceAddress: { ...newDoctor.residenceAddress, department: e.target.value } })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Profesional</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Universidad *</label>
                      <select required className="input-field py-3"
                        value={newDoctor.universityId}
                        onChange={(e) => setNewDoctor({ ...newDoctor, universityId: e.target.value })}>
                        <option value="">Seleccionar universidad...</option>
                        {universities.map(u => <option key={u.id} value={u.id}>{u.name}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Especialidad *</label>
                      <select className="input-field py-3"
                        value={newDoctor.specialty}
                        onChange={(e) => setNewDoctor({ ...newDoctor, specialty: e.target.value })}>
                        <option value="OTHER">Otra</option>
                        <option value="MEDICINA_INTERNA">Medicina Interna</option>
                        <option value="CIRUGIA_GENERAL">Cirugía General</option>
                        <option value="PEDIATRIA">Pediatría</option>
                        <option value="GINECOLOGIA_OBSTETRICIA">Ginecología y Obstetricia</option>
                        <option value="URGENCIAS">Urgencias</option>
                        <option value="CARDIOLOGIA">Cardiología</option>
                        <option value="NEUROLOGIA">Neurología</option>
                        <option value="TRAUMATOLOGIA">Traumatología</option>
                        <option value="CUIDADOS_INTENSIVOS">Cuidados Intensivos</option>
                        <option value="MEDICINA_FAMILIAR">Medicina Familiar</option>
                        <option value="ONCOLOGIA">Oncología</option>
                        <option value="ANESTESIOLOGIA">Anestesiología</option>
                        <option value="RADIOLOGIA">Radiología</option>
                        <option value="PSIQUIATRIA">Psiquiatría</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>

              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50 sticky bottom-0">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">
                  Cancelar
                </button>
                <button type="submit" disabled={creating} className="btn-primary shadow-lg shadow-clinical-600/10 disabled:opacity-50">
                  {creating ? 'Creando...' : 'Agregar Médico'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showEditModal && editTarget && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50 sticky top-0 z-10">
              <div className="flex items-center gap-2">
                <Pencil className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Editar Médico</h2>
              </div>
              <button onClick={() => { setShowEditModal(false); setEditTarget(null) }} className="p-2 hover:bg-slate-100 rounded-xl transition-colors">
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>
            <form onSubmit={async (e) => {
              e.preventDefault()
              setEditing(true)
              try {
                await doctorService.update(editTarget.id, {
                  name: editTarget.name,
                  lastName: editTarget.lastName,
                  dni: editTarget.dni,
                  email: editTarget.email,
                  phoneNumber: editTarget.phoneNumber,
                  maritalStatus: editTarget.maritalStatus,
                  typeBlood: editTarget.typeBlood,
                  weight: editTarget.weight ? parseFloat(editTarget.weight) : null,
                  imc: editTarget.imc ? parseFloat(editTarget.imc) : null,
                  specialty: editTarget.specialty,
                  universityId: editTarget.universityId
                })
                setShowEditModal(false)
                setEditTarget(null)
                setToast({ show: true, message: 'Médico actualizado exitosamente' })
                setTimeout(() => setToast({ show: false, message: '' }), 4000)
                const { data } = await doctorService.findAll({ page: 0, size: 50 })
                setDoctors(data.content)
              } catch (err) {
                const msg = err.response?.data?.message || err.response?.data || 'Error al actualizar'
                alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
              } finally {
                setEditing(false)
              }
            }}>
              <div className="p-6 space-y-6">
                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Personal</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Nombre *</label>
                      <input required type="text" className="input-field py-3"
                        value={editTarget.name || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, name: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Apellido *</label>
                      <input required type="text" className="input-field py-3"
                        value={editTarget.lastName || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, lastName: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">DNI *</label>
                      <input required type="text" className="input-field py-3"
                        value={editTarget.dni || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, dni: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Teléfono *</label>
                      <input required type="text" maxLength={11} className="input-field py-3"
                        value={editTarget.phoneNumber || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, phoneNumber: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Correo Electrónico *</label>
                      <input required type="email" className="input-field py-3"
                        value={editTarget.email || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, email: e.target.value })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Demográfica</h3>
                  <div className="grid grid-cols-2 gap-4 mb-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Estado Civil *</label>
                      <select className="input-field py-3"
                        value={editTarget.maritalStatus || 'OTHER'}
                        onChange={(e) => setEditTarget({ ...editTarget, maritalStatus: e.target.value })}>
                        {maritalStatusOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Tipo de Sangre *</label>
                      <select className="input-field py-3"
                        value={editTarget.typeBlood || 'O_POSITIVE'}
                        onChange={(e) => setEditTarget({ ...editTarget, typeBlood: e.target.value })}>
                        {typeBloodOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Peso (kg)</label>
                      <input type="number" step="0.1" placeholder="70.5" className="input-field py-3"
                        value={editTarget.weight ?? ''}
                        onChange={(e) => setEditTarget({ ...editTarget, weight: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">IMC</label>
                      <input type="number" step="0.1" placeholder="22.5" className="input-field py-3"
                        value={editTarget.imc ?? ''}
                        onChange={(e) => setEditTarget({ ...editTarget, imc: e.target.value })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Profesional</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Especialidad *</label>
                      <select className="input-field py-3"
                        value={editTarget.specialty || 'OTHER'}
                        onChange={(e) => setEditTarget({ ...editTarget, specialty: e.target.value })}>
                        <option value="OTHER">Otra</option>
                        <option value="MEDICINA_INTERNA">Medicina Interna</option>
                        <option value="CIRUGIA_GENERAL">Cirugía General</option>
                        <option value="PEDIATRIA">Pediatría</option>
                        <option value="GINECOLOGIA_OBSTETRICIA">Ginecología y Obstetricia</option>
                        <option value="URGENCIAS">Urgencias</option>
                        <option value="CARDIOLOGIA">Cardiología</option>
                        <option value="NEUROLOGIA">Neurología</option>
                        <option value="TRAUMATOLOGIA">Traumatología</option>
                        <option value="CUIDADOS_INTENSIVOS">Cuidados Intensivos</option>
                        <option value="MEDICINA_FAMILIAR">Medicina Familiar</option>
                        <option value="ONCOLOGIA">Oncología</option>
                        <option value="ANESTESIOLOGIA">Anestesiología</option>
                        <option value="RADIOLOGIA">Radiología</option>
                        <option value="PSIQUIATRIA">Psiquiatría</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50">
                <button type="button" onClick={() => { setShowEditModal(false); setEditTarget(null) }} className="btn-secondary">Cancelar</button>
                <button type="submit" disabled={editing} className="btn-primary disabled:opacity-50">
                  {editing ? 'Guardando...' : 'Guardar Cambios'}
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
                ¿Estás seguro de eliminar al médico <strong>{deleteTarget?.name} {deleteTarget?.lastName}</strong>?
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
