import { useState, useEffect } from 'react'
import { Users, Stethoscope, UserCog, ClipboardList, BarChart3, Plus, X, Trash2, AlertTriangle } from 'lucide-react'
import { studentService, doctorService, adminService, rotationService } from '../services/api'
import { useAuth } from '../context/AuthContext'
import Toast from '../components/Toast'

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
  phoneNumber: '',
  email: '',
  password: '',
  maritalStatus: 'OTHER',
  typeBlood: 'O_POSITIVE',
  weight: '',
  imc: '',
  hiringDate: '',
  endDate: '',
  placeBirth: { address: '', city: '', department: '' },
  residenceAddress: { address: '', city: '', department: '' }
}

export default function Admin() {
  const { user } = useAuth()
  const [stats, setStats] = useState({
    students: 0,
    doctors: 0,
    admins: 0,
    rotations: 0
  })
  const [loading, setLoading] = useState(true)
  const [admins, setAdmins] = useState([])
  const [adminsLoading, setAdminsLoading] = useState(false)
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [showModal, setShowModal] = useState(false)
  const [creating, setCreating] = useState(false)
  const [newAdmin, setNewAdmin] = useState(emptyForm)
  const [toast, setToast] = useState({ show: false, message: '' })
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)
  const itemsPerPage = 8

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true)
      try {
        const [studentsRes, doctorsRes, adminsRes, rotationsRes] = await Promise.all([
          studentService.findAll({ page: 0, size: 1 }),
          doctorService.findAll({ page: 0, size: 1 }),
          adminService.findAll({ page: 0, size: 1 }),
          rotationService.findAll({ page: 0, size: 1 })
        ])
        setStats({
          students: studentsRes.data.totalElements,
          doctors: doctorsRes.data.totalElements,
          admins: adminsRes.data.totalElements,
          rotations: rotationsRes.data.totalElements
        })
      } catch (err) {
        console.error('Error loading admin stats:', err)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  useEffect(() => {
    const fetchAdmins = async () => {
      setAdminsLoading(true)
      try {
        const res = await adminService.findAll({ page: currentPage, size: itemsPerPage })
        setAdmins(res.data.content || [])
        setTotalPages(res.data.totalPages)
        setTotalElements(res.data.totalElements)
      } catch (err) {
        console.error('Error loading admins:', err)
      } finally {
        setAdminsLoading(false)
      }
    }
    fetchAdmins()
  }, [currentPage])

  const total = stats.students + stats.doctors + stats.admins

  const openCreateModal = () => {
    setNewAdmin(emptyForm)
    setShowModal(true)
  }

  const handleCreate = async (e) => {
    e.preventDefault()
    setCreating(true)
    try {
      await adminService.create({
        ...newAdmin,
        weight: parseFloat(newAdmin.weight),
        imc: parseFloat(newAdmin.imc)
      })
      setShowModal(false)
      setToast({ show: true, message: 'Administrador creado exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 3000)
      const res = await adminService.findAll({ page: currentPage, size: itemsPerPage })
      setAdmins(res.data.content || [])
      setTotalPages(res.data.totalPages)
      setTotalElements(res.data.totalElements)
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al crear administrador'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setCreating(false)
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      await adminService.delete(deleteTarget.id)
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
      setToast({ show: true, message: 'Administrador eliminado exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 3000)
      const res = await adminService.findAll({ page: currentPage, size: itemsPerPage })
      setAdmins(res.data.content || [])
      setTotalPages(res.data.totalPages)
      setTotalElements(res.data.totalElements)
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar administrador'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setDeleting(false)
    }
  }

  const startIndex = currentPage * itemsPerPage

  return (
    <div className="space-y-6">
      <Toast show={toast.show} message={toast.message} />

      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Panel de Administración</h1>
          <p className="text-slate-500 mt-1">Gestión y estadísticas del sistema</p>
        </div>
        <div className="flex items-center gap-2 text-sm text-slate-500 bg-white px-4 py-2 rounded-lg border border-slate-200">
          <UserCog className="w-4 h-4" />
          <span>Administrador: {user?.dni}</span>
        </div>
      </div>

      <div className="bg-gradient-to-r from-clinical-800 to-clinical-900 rounded-3xl p-6 text-white shadow-xl">
        <div className="flex items-center gap-3 mb-4">
          <BarChart3 className="w-6 h-6" />
          <h2 className="text-lg font-semibold">Resumen Total de Personas</h2>
        </div>
        <p className="text-5xl font-bold tracking-tight">
          {loading ? '...' : total}
        </p>
        <p className="text-clinical-200 mt-2">personas registradas en el sistema</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {[
          { title: 'Estudiantes', value: stats.students, icon: Users, color: 'text-blue-600', hoverBorder: 'hover:border-blue-200', bgColor: 'bg-blue-50' },
          { title: 'Médicos', value: stats.doctors, icon: Stethoscope, color: 'text-emerald-600', hoverBorder: 'hover:border-emerald-200', bgColor: 'bg-emerald-50' },
          { title: 'Administradores', value: stats.admins, icon: UserCog, color: 'text-purple-600', hoverBorder: 'hover:border-purple-200', bgColor: 'bg-purple-50' },
          { title: 'Rotaciones', value: stats.rotations, icon: ClipboardList, color: 'text-amber-600', hoverBorder: 'hover:border-amber-200', bgColor: 'bg-amber-50' },
        ].map((card, index) => (
          <div key={index} className={`bg-white rounded-3xl p-6 border border-slate-100 shadow-sm hover:shadow-xl hover:-translate-y-1 transform transition-all duration-300 group overflow-hidden relative ${card.hoverBorder}`}>
            <div className="flex items-start justify-between">
              <div className={`p-3 rounded-2xl ${card.bgColor} group-hover:scale-110 transition-transform duration-300`}>
                <card.icon className={`w-6 h-6 ${card.color}`} />
              </div>
            </div>
            <div className="mt-4">
              <p className="text-3xl font-bold text-slate-800 tracking-tight">
                {loading ? '...' : card.value}
              </p>
              <p className="text-sm font-medium text-slate-500 mt-1">{card.title}</p>
            </div>
            <div className={`absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r ${card.color.replace('text-', 'from-').split(' ')[0]} to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300`} />
          </div>
        ))}
      </div>

      <div className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="text-lg font-bold text-slate-800">Administradores</h2>
            <p className="text-sm text-slate-500">Gestión de usuarios administradores</p>
          </div>
          <button
            onClick={openCreateModal}
            className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/15"
          >
            <Plus className="w-4 h-4" />
            Crear Administrador
          </button>
        </div>

        {adminsLoading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-clinical-600"></div>
          </div>
        ) : (
          <>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-slate-200">
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Administrador</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">DNI</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Teléfono</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Tipo Sangre</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Contratación</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100/80">
                  {admins.map((admin) => (
                    <tr key={admin.id} className="hover:bg-slate-50/40 transition-colors group">
                      <td className="py-4 px-4">
                        <div className="flex items-center gap-3">
                          <div className="w-9 h-9 bg-purple-50 text-purple-600 rounded-xl flex items-center justify-center font-bold text-sm group-hover:bg-purple-600 group-hover:text-white transition-all duration-300">
                            {`${admin.name?.[0] || ''}${admin.lastName?.[0] || ''}`.toUpperCase()}
                          </div>
                          <div>
                            <p className="text-sm font-semibold text-slate-800">{admin.name} {admin.lastName}</p>
                            <p className="text-xs text-slate-400 font-medium">{admin.email}</p>
                          </div>
                        </div>
                      </td>
                      <td className="py-4 px-4 text-sm text-slate-600 font-medium">{admin.dni}</td>
                      <td className="py-4 px-4 text-sm text-slate-600">{admin.phoneNumber}</td>
                      <td className="py-4 px-4">
                        <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider bg-purple-50 text-purple-700">
                          {typeBloodOptions.find(o => o.value === admin.typeBlood)?.label || admin.typeBlood}
                        </span>
                      </td>
                      <td className="py-4 px-4 text-sm text-slate-500 font-medium">
                        {admin.hiringDate ? new Date(admin.hiringDate).toLocaleDateString('es-CO') : 'N/A'}
                      </td>
                      <td className="py-4 px-4">
                        <button
                          onClick={() => { setDeleteTarget(admin); setShowDeleteConfirm(true) }}
                          className="p-2 hover:bg-red-50 rounded-lg transition-colors group"
                          title="Eliminar administrador"
                        >
                          <Trash2 className="w-4 h-4 text-slate-400 group-hover:text-red-500" />
                        </button>
                      </td>
                    </tr>
                  ))}
                  {admins.length === 0 && (
                    <tr>
                      <td colSpan={6} className="py-12 text-center text-slate-400 font-medium">
                        No hay administradores registrados.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>

            <div className="flex flex-col sm:flex-row items-center justify-between gap-4 mt-6 pt-4 border-t border-slate-100">
              <p className="text-xs font-semibold text-slate-400 uppercase tracking-wider">
                Mostrando {startIndex + 1}-{Math.min(startIndex + itemsPerPage, totalElements)} de {totalElements}
              </p>
              <div className="flex items-center gap-2">
                <button
                  onClick={() => setCurrentPage(p => Math.max(0, p - 1))}
                  disabled={currentPage === 0}
                  className="p-2 rounded-lg border border-slate-100 hover:bg-slate-50 hover:border-slate-200 disabled:opacity-40 disabled:cursor-not-allowed transition-all"
                >
                  <svg className="w-4 h-4 text-slate-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" /></svg>
                </button>
                {Array.from({ length: totalPages }, (_, i) => i + 1).map(page => (
                  <button
                    key={page}
                    onClick={() => setCurrentPage(page - 1)}
                    className={`w-8.5 h-8.5 rounded-lg text-xs font-bold transition-all duration-200
                      ${currentPage === page - 1
                        ? 'bg-clinical-600 text-white shadow-md shadow-clinical-600/10'
                        : 'hover:bg-slate-50 border border-transparent hover:border-slate-100 text-slate-600'}`}
                  >
                    {page}
                  </button>
                ))}
                <button
                  onClick={() => setCurrentPage(p => Math.min(totalPages - 1, p + 1))}
                  disabled={currentPage === totalPages - 1}
                  className="p-2 rounded-lg border border-slate-100 hover:bg-slate-50 hover:border-slate-200 disabled:opacity-40 disabled:cursor-not-allowed transition-all"
                >
                  <svg className="w-4 h-4 text-slate-600" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" /></svg>
                </button>
              </div>
            </div>
          </>
        )}
      </div>

      {loading && (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-clinical-600"></div>
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-clinical-900/40 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl overflow-hidden border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="max-h-[90vh] overflow-y-auto">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-white sticky top-0 z-10">
              <div className="flex items-center gap-2">
                <UserCog className="w-5 h-5 text-purple-600" />
                <h2 className="text-xl font-bold text-slate-800">Crear Administrador</h2>
              </div>
              <button
                onClick={() => setShowModal(false)}
                className="p-2 hover:bg-slate-100 rounded-xl transition-colors"
              >
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>

            <form onSubmit={handleCreate}>
              <div className="p-6 space-y-6">
                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Personal</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Nombre *</label>
                      <input required type="text" placeholder="Nombre" className="input-field py-3"
                        value={newAdmin.name}
                        onChange={(e) => setNewAdmin({ ...newAdmin, name: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Apellido *</label>
                      <input required type="text" placeholder="Apellido" className="input-field py-3"
                        value={newAdmin.lastName}
                        onChange={(e) => setNewAdmin({ ...newAdmin, lastName: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">DNI *</label>
                      <input required type="text" placeholder="1234567890" className="input-field py-3"
                        value={newAdmin.dni}
                        onChange={(e) => setNewAdmin({ ...newAdmin, dni: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Teléfono *</label>
                      <input required type="text" placeholder="3001234567" maxLength={11} className="input-field py-3"
                        value={newAdmin.phoneNumber}
                        onChange={(e) => setNewAdmin({ ...newAdmin, phoneNumber: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Correo Electrónico *</label>
                      <input required type="email" placeholder="admin@correo.com" className="input-field py-3"
                        value={newAdmin.email}
                        onChange={(e) => setNewAdmin({ ...newAdmin, email: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Contraseña *</label>
                      <input required type="password" placeholder="Mínimo 8 caracteres" className="input-field py-3"
                        value={newAdmin.password}
                        onChange={(e) => setNewAdmin({ ...newAdmin, password: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Estado Civil *</label>
                      <select required className="input-field py-3"
                        value={newAdmin.maritalStatus}
                        onChange={(e) => setNewAdmin({ ...newAdmin, maritalStatus: e.target.value })}>
                        {maritalStatusOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Tipo de Sangre *</label>
                      <select required className="input-field py-3"
                        value={newAdmin.typeBlood}
                        onChange={(e) => setNewAdmin({ ...newAdmin, typeBlood: e.target.value })}>
                        {typeBloodOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Peso (kg) *</label>
                      <input required type="number" step="0.1" placeholder="70.0" className="input-field py-3"
                        value={newAdmin.weight}
                        onChange={(e) => setNewAdmin({ ...newAdmin, weight: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">IMC *</label>
                      <input required type="number" step="0.1" placeholder="24.5" className="input-field py-3"
                        value={newAdmin.imc}
                        onChange={(e) => setNewAdmin({ ...newAdmin, imc: e.target.value })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información de Ubicación</h3>
                  <div className="mb-4">
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Lugar de Nacimiento *</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input required type="text" placeholder="Dirección" className="input-field py-3"
                        value={newAdmin.placeBirth.address}
                        onChange={(e) => setNewAdmin({ ...newAdmin, placeBirth: { ...newAdmin.placeBirth, address: e.target.value } })} />
                      <input required type="text" placeholder="Ciudad" className="input-field py-3"
                        value={newAdmin.placeBirth.city}
                        onChange={(e) => setNewAdmin({ ...newAdmin, placeBirth: { ...newAdmin.placeBirth, city: e.target.value } })} />
                      <input required type="text" placeholder="Departamento" className="input-field py-3"
                        value={newAdmin.placeBirth.department}
                        onChange={(e) => setNewAdmin({ ...newAdmin, placeBirth: { ...newAdmin.placeBirth, department: e.target.value } })} />
                    </div>
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Dirección de Residencia *</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input required type="text" placeholder="Dirección" className="input-field py-3"
                        value={newAdmin.residenceAddress.address}
                        onChange={(e) => setNewAdmin({ ...newAdmin, residenceAddress: { ...newAdmin.residenceAddress, address: e.target.value } })} />
                      <input required type="text" placeholder="Ciudad" className="input-field py-3"
                        value={newAdmin.residenceAddress.city}
                        onChange={(e) => setNewAdmin({ ...newAdmin, residenceAddress: { ...newAdmin.residenceAddress, city: e.target.value } })} />
                      <input required type="text" placeholder="Departamento" className="input-field py-3"
                        value={newAdmin.residenceAddress.department}
                        onChange={(e) => setNewAdmin({ ...newAdmin, residenceAddress: { ...newAdmin.residenceAddress, department: e.target.value } })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información del Contrato</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fecha de Contratación *</label>
                      <input required type="date" className="input-field py-3"
                        value={newAdmin.hiringDate}
                        onChange={(e) => setNewAdmin({ ...newAdmin, hiringDate: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fecha de Finalización *</label>
                      <input required type="date" className="input-field py-3"
                        value={newAdmin.endDate}
                        onChange={(e) => setNewAdmin({ ...newAdmin, endDate: e.target.value })} />
                    </div>
                  </div>
                </div>
              </div>

              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-white sticky bottom-0">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">
                  Cancelar
                </button>
                <button type="submit" disabled={creating} className="btn-primary shadow-lg shadow-clinical-600/10 disabled:opacity-50">
                  {creating ? 'Creando...' : 'Crear Administrador'}
                </button>
              </div>
            </form>
            </div>
          </div>
        </div>
      )}

      {showDeleteConfirm && (
        <div className="fixed inset-0 bg-clinical-900/40 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
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
                ¿Estás seguro de eliminar al administrador <strong>{deleteTarget?.name} {deleteTarget?.lastName}</strong>?
              </p>
            </div>
            <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-white">
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
