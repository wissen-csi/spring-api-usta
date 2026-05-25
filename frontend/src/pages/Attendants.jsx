import { useState, useEffect } from 'react'
import { Plus, Search, Pencil, Trash2, X, Sparkles, CheckCircle, AlertTriangle, Users } from 'lucide-react'
import { attendantService } from '../services/api'
import { useAuth } from '../context/AuthContext'

const typeAttendantOptions = [
  { value: 'FATHER', label: 'Padre' },
  { value: 'MOTHER', label: 'Madre' },
  { value: 'BROTHER', label: 'Hermano' },
  { value: 'SISTER', label: 'Hermana' },
  { value: 'CHILDREN', label: 'Hijo/a' },
  { value: 'ATTENDANT', label: 'Acudiente' }
]

const typeAttendantMap = Object.fromEntries(typeAttendantOptions.map(o => [o.value, o.label]))

const initialForm = {
  name: '',
  lastName: '',
  phoneNumber: '',
  dni: '',
  typeAttendant: 'FATHER',
  studentId: ''
}

export default function Attendants() {
  const { user } = useAuth()
  const isAdmin = user?.role === 'ADMIN' || user?.role === 'ROLE_ADMIN'

  const [showModal, setShowModal] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [attendants, setAttendants] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [toast, setToast] = useState({ show: false, message: '' })
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)
  const [editTarget, setEditTarget] = useState(null)
  const [form, setForm] = useState(initialForm)
  const [submitting, setSubmitting] = useState(false)

  const loadData = async () => {
    setLoading(true)
    setError('')
    try {
      const res = await attendantService.findAll({ page: 0, size: 100 })
      setAttendants(Array.isArray(res.data) ? res.data : res.data.content || [])
    } catch (err) {
      console.error('Error loading attendants:', err)
      setError('Error al conectar con el servidor. Por favor, intente de nuevo.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadData() }, [])

  const openCreateModal = () => {
    setEditTarget(null)
    setForm(isAdmin ? initialForm : { ...initialForm, studentId: user.dni })
    setShowModal(true)
  }

  const openEditModal = (attendant) => {
    setEditTarget(attendant)
    setForm({
      name: attendant.name || '',
      lastName: attendant.lastName || '',
      phoneNumber: attendant.phoneNumber || '',
      dni: attendant.dni || '',
      typeAttendant: attendant.typeAttendant || 'FATHER',
      studentId: attendant.studentId || (isAdmin ? '' : user.dni)
    })
    setShowModal(true)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!form.name || !form.lastName || !form.dni || !form.typeAttendant) {
      alert('Por favor complete todos los campos obligatorios.')
      return
    }
    setSubmitting(true)
    try {
      const payload = {
        name: form.name,
        lastName: form.lastName,
        phoneNumber: form.phoneNumber,
        dni: form.dni,
        typeAttendant: form.typeAttendant,
        studentId: form.studentId
      }

      if (editTarget) {
        await attendantService.update(editTarget.id, payload)
        setToast({ show: true, message: 'Familiar actualizado exitosamente' })
      } else {
        await attendantService.create(payload)
        setToast({ show: true, message: 'Familiar agregado exitosamente' })
      }

      setTimeout(() => setToast({ show: false, message: '' }), 4000)
      setShowModal(false)
      setEditTarget(null)
      setForm(initialForm)
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al guardar el familiar'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setSubmitting(false)
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      await attendantService.delete(deleteTarget.id)
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
      setToast({ show: true, message: 'Familiar eliminado exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 4000)
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar el familiar'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setDeleting(false)
    }
  }

  const filteredAttendants = attendants.filter(a => {
    const query = searchTerm.toLowerCase()
    return (a.name || '').toLowerCase().includes(query) ||
           (a.lastName || '').toLowerCase().includes(query) ||
           (a.dni || '').toLowerCase().includes(query) ||
           (a.phoneNumber || '').toLowerCase().includes(query)
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
          <h1 className="text-2xl font-bold text-slate-800">Familiares / Acudientes</h1>
          <p className="text-slate-500 mt-1">Gestión de familiares y personas autorizadas</p>
        </div>
        <button
          onClick={openCreateModal}
          className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/20 hover:shadow-clinical-600/30 transition-all duration-300"
        >
          <Plus className="w-4 h-4" />
          Agregar Familiar
        </button>
      </div>

      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
        <input
          type="text"
          placeholder="Buscar por nombre, DNI o teléfono..."
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
            <table className="w-full">
              <thead>
                <tr className="border-b border-slate-100 bg-slate-50/50">
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Nombre</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Apellido</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Teléfono</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">DNI</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Parentesco</th>
                  <th className="text-right text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-50">
                {filteredAttendants.map((attendant) => (
                  <tr key={attendant.id} className="hover:bg-slate-50/50 transition-colors group">
                    <td className="px-6 py-4 text-sm font-semibold text-slate-800">{attendant.name}</td>
                    <td className="px-6 py-4 text-sm text-slate-600">{attendant.lastName}</td>
                    <td className="px-6 py-4 text-sm text-slate-600">{attendant.phoneNumber}</td>
                    <td className="px-6 py-4 text-sm text-slate-600">{attendant.dni}</td>
                    <td className="px-6 py-4">
                      <span className="px-3 py-1 rounded-full text-xs font-semibold bg-clinical-50 text-clinical-700 border border-clinical-200">
                        {typeAttendantMap[attendant.typeAttendant] || attendant.typeAttendant}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <div className="flex items-center justify-end gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                        <button
                          onClick={() => openEditModal(attendant)}
                          className="p-2 hover:bg-clinical-50 rounded-lg transition-colors" title="Editar"
                        >
                          <Pencil className="w-4 h-4 text-slate-400 hover:text-clinical-600" />
                        </button>
                        <button
                          onClick={() => { setDeleteTarget(attendant); setShowDeleteConfirm(true) }}
                          className="p-2 hover:bg-red-50 rounded-lg transition-colors" title="Eliminar"
                        >
                          <Trash2 className="w-4 h-4 text-slate-400 hover:text-red-500" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {!loading && filteredAttendants.length === 0 && (
        <div className="text-center py-16 bg-white rounded-2xl border border-slate-100 shadow-sm">
          <Users className="w-12 h-12 text-slate-300 mx-auto mb-4" />
          <p className="text-slate-500 font-medium">
            {searchTerm ? 'No se encontraron familiares con ese criterio' : 'No hay familiares registrados'}
          </p>
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50">
              <div className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">
                  {editTarget ? 'Editar Familiar' : 'Agregar Familiar'}
                </h2>
              </div>
              <button
                onClick={() => { setShowModal(false); setEditTarget(null); setForm(initialForm) }}
                className="p-2 hover:bg-slate-100 rounded-xl transition-colors"
              >
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="p-6 space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Nombre *</label>
                    <input
                      required
                      type="text"
                      className="input-field py-3"
                      placeholder="Nombre"
                      value={form.name}
                      onChange={(e) => setForm({ ...form, name: e.target.value })}
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Apellido *</label>
                    <input
                      required
                      type="text"
                      className="input-field py-3"
                      placeholder="Apellido"
                      value={form.lastName}
                      onChange={(e) => setForm({ ...form, lastName: e.target.value })}
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Teléfono</label>
                    <input
                      type="text"
                      className="input-field py-3"
                      placeholder="Teléfono"
                      value={form.phoneNumber}
                      onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })}
                    />
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">DNI *</label>
                    <input
                      required
                      type="text"
                      className="input-field py-3"
                      placeholder="Número de documento"
                      value={form.dni}
                      onChange={(e) => setForm({ ...form, dni: e.target.value })}
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Parentesco *</label>
                  <select
                    required
                    className="input-field py-3"
                    value={form.typeAttendant}
                    onChange={(e) => setForm({ ...form, typeAttendant: e.target.value })}
                  >
                    {typeAttendantOptions.map(o => (
                      <option key={o.value} value={o.value}>{o.label}</option>
                    ))}
                  </select>
                </div>

                {(!editTarget && !isAdmin) ? (
                  <div className="p-3 bg-clinical-50/40 rounded-xl border border-clinical-100/50">
                    <p className="text-xs text-slate-500">
                      Se registrará con tu DNI: <strong className="text-clinical-700">{user?.dni}</strong>
                    </p>
                  </div>
                ) : (isAdmin ? (
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">DNI del Estudiante *</label>
                    <input
                      required
                      type="text"
                      className="input-field py-3"
                      placeholder="DNI del estudiante"
                      value={form.studentId}
                      onChange={(e) => setForm({ ...form, studentId: e.target.value })}
                    />
                  </div>
                ) : null)}
              </div>

              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50">
                <button
                  type="button"
                  onClick={() => { setShowModal(false); setEditTarget(null); setForm(initialForm) }}
                  className="btn-secondary"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  disabled={submitting}
                  className="btn-primary shadow-lg shadow-clinical-600/10 disabled:opacity-50"
                >
                  {submitting ? 'Guardando...' : (editTarget ? 'Actualizar Familiar' : 'Agregar Familiar')}
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
                ¿Estás seguro de eliminar a <strong>{deleteTarget?.name} {deleteTarget?.lastName}</strong>?
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
