import { useState, useEffect } from 'react'
import { Search, Plus, X, CheckCircle, Building2, Trash2, AlertTriangle, Pencil } from 'lucide-react'
import { universityService } from '../services/api'

export default function Universities() {
  const [universities, setUniversities] = useState([])
  const [searchTerm, setSearchTerm] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [creating, setCreating] = useState(false)
  const [toast, setToast] = useState({ show: false, message: '' })
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)
  const [showEditModal, setShowEditModal] = useState(false)
  const [editTarget, setEditTarget] = useState(null)
  const [editing, setEditing] = useState(false)
  const [newUniversity, setNewUniversity] = useState({
    name: '',
    email: '',
    phoneNumber: '',
    address: { address: '', city: '', department: '' }
  })

  const fetchUniversities = async () => {
    setLoading(true)
    setError('')
    try {
      const { data } = await universityService.findAll()
      setUniversities(data.content || data)
    } catch {
      setError('Error al cargar las universidades')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { fetchUniversities() }, [])

  const filteredUniversities = universities.filter(u =>
    u.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    u.email?.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const handleCreate = async (e) => {
    e.preventDefault()
    if (!newUniversity.name || !newUniversity.email) {
      alert('Por favor complete todos los campos obligatorios.')
      return
    }
    setCreating(true)
    try {
      await universityService.create(newUniversity)
      setShowModal(false)
      setNewUniversity({
        name: '',
        email: '',
        phoneNumber: '',
        address: { address: '', city: '', department: '' }
      })
      setToast({ show: true, message: 'Universidad creada exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 4000)
      await fetchUniversities()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al crear la universidad'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setCreating(false)
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      await universityService.delete(deleteTarget.id)
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
      setToast({ show: true, message: 'Universidad eliminada exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 4000)
      await fetchUniversities()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar la universidad'
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
          <h1 className="text-2xl font-bold text-slate-800">Universidades</h1>
          <p className="text-slate-500 mt-1">Gestión de universidades convenio</p>
        </div>
        <button
          onClick={() => setShowModal(true)}
          className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/15 hover:shadow-clinical-600/25 transition-all duration-300"
        >
          <Plus className="w-4 h-4" />
          Crear Universidad
        </button>
      </div>

      <div className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm hover:shadow-md transition-shadow duration-300">
        <div className="flex flex-col sm:flex-row gap-4 mb-6">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
            <input
              type="text"
              placeholder="Buscar universidades por nombre o correo..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="input-field pl-10 focus:ring-clinical-500 focus:border-clinical-500"
            />
          </div>
        </div>

        {loading ? (
          <div className="flex justify-center py-16">
            <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-clinical-600"></div>
          </div>
        ) : error ? (
          <div className="text-center py-12 text-red-600 font-medium">{error}</div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-slate-200">
                  <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Universidad</th>
                  <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Correo</th>
                  <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Teléfono</th>
                  <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Estado</th>
                  <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Fecha Creación</th>
                  <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100/80">
                {filteredUniversities.map((u) => (
                  <tr key={u.id} className="hover:bg-slate-50/40 transition-colors group">
                    <td className="py-4 px-4">
                      <div className="flex items-center gap-3">
                        <div className="w-9 h-9 bg-clinical-50 text-clinical-600 rounded-xl flex items-center justify-center font-bold text-sm group-hover:bg-clinical-600 group-hover:text-white transition-all duration-300">
                          <Building2 className="w-4 h-4" />
                        </div>
                        <div>
                          <p className="text-sm font-semibold text-slate-800 group-hover:text-clinical-700 transition-colors">{u.name}</p>
                        </div>
                      </div>
                    </td>
                    <td className="py-4 px-4 text-sm text-slate-600 font-medium">{u.email}</td>
                    <td className="py-4 px-4 text-sm text-slate-600">{u.phoneNumber || 'N/A'}</td>
                    <td className="py-4 px-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider ${u.isActive ? 'bg-emerald-100 text-emerald-700' : 'bg-red-100 text-red-700'}`}>
                        {u.isActive ? 'Activo' : 'Inactivo'}
                      </span>
                    </td>
                    <td className="py-4 px-4 text-sm text-slate-500 font-medium">
                      {u.creationDate ? new Date(u.creationDate).toLocaleDateString('es-CO') : 'N/A'}
                    </td>
                    <td className="py-4 px-4">
                      <button
                        onClick={() => { setDeleteTarget(u); setShowDeleteConfirm(true) }}
                        className="p-2 hover:bg-red-50 rounded-lg transition-colors group" title="Eliminar universidad"
                      >
                        <Trash2 className="w-4 h-4 text-slate-400 group-hover:text-red-500" />
                      </button>
                    </td>
                  </tr>
                ))}
                {filteredUniversities.length === 0 && (
                  <tr>
                    <td colSpan={6} className="py-12 text-center text-slate-400 font-medium">
                      No se encontraron universidades.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg max-h-[90vh] overflow-y-auto border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50 sticky top-0 z-10">
              <div className="flex items-center gap-2">
                <Building2 className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Crear Universidad</h2>
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
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información General</h3>
                  <div className="grid grid-cols-1 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Nombre *</label>
                      <input required type="text" placeholder="Universidad Santo Tomás" className="input-field py-3"
                        value={newUniversity.name}
                        onChange={(e) => setNewUniversity({ ...newUniversity, name: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Correo Electrónico *</label>
                      <input required type="email" placeholder="contacto@universidad.edu" className="input-field py-3"
                        value={newUniversity.email}
                        onChange={(e) => setNewUniversity({ ...newUniversity, email: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Teléfono</label>
                      <input type="text" placeholder="3001234567" maxLength={11} className="input-field py-3"
                        value={newUniversity.phoneNumber}
                        onChange={(e) => setNewUniversity({ ...newUniversity, phoneNumber: e.target.value })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Dirección</h3>
                  <div className="grid grid-cols-1 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Dirección</label>
                      <input type="text" placeholder="Calle 123 #45-67" className="input-field py-3"
                        value={newUniversity.address.address}
                        onChange={(e) => setNewUniversity({ ...newUniversity, address: { ...newUniversity.address, address: e.target.value } })} />
                    </div>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Ciudad</label>
                        <input type="text" placeholder="Bogotá" className="input-field py-3"
                          value={newUniversity.address.city}
                          onChange={(e) => setNewUniversity({ ...newUniversity, address: { ...newUniversity.address, city: e.target.value } })} />
                      </div>
                      <div>
                        <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Departamento</label>
                        <input type="text" placeholder="Cundinamarca" className="input-field py-3"
                          value={newUniversity.address.department}
                          onChange={(e) => setNewUniversity({ ...newUniversity, address: { ...newUniversity.address, department: e.target.value } })} />
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50 sticky bottom-0">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">
                  Cancelar
                </button>
                <button type="submit" disabled={creating} className="btn-primary shadow-lg shadow-clinical-600/10 disabled:opacity-50">
                  {creating ? 'Creando...' : 'Crear Universidad'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showEditModal && editTarget && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50">
              <div className="flex items-center gap-2">
                <Pencil className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Editar Universidad</h2>
              </div>
              <button onClick={() => { setShowEditModal(false); setEditTarget(null) }} className="p-2 hover:bg-slate-100 rounded-xl transition-colors">
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>
            <form onSubmit={async (e) => {
              e.preventDefault()
              setEditing(true)
              try {
                await universityService.update(editTarget.id, {
                  name: editTarget.name,
                  email: editTarget.email,
                  phoneNumber: editTarget.phoneNumber
                })
                setShowEditModal(false)
                setEditTarget(null)
                setToast({ show: true, message: 'Universidad actualizada exitosamente' })
                setTimeout(() => setToast({ show: false, message: '' }), 4000)
                await fetchUniversities()
              } catch (err) {
                const msg = err.response?.data?.message || err.response?.data || 'Error al actualizar'
                alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
              } finally {
                setEditing(false)
              }
            }}>
              <div className="p-6 space-y-4">
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Nombre</label>
                  <input required type="text" className="input-field py-3"
                    value={editTarget.name || ''}
                    onChange={(e) => setEditTarget({ ...editTarget, name: e.target.value })} />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Email</label>
                  <input required type="email" className="input-field py-3"
                    value={editTarget.email || ''}
                    onChange={(e) => setEditTarget({ ...editTarget, email: e.target.value })} />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Teléfono</label>
                  <input type="text" className="input-field py-3"
                    value={editTarget.phoneNumber || ''}
                    onChange={(e) => setEditTarget({ ...editTarget, phoneNumber: e.target.value })} />
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
                ¿Estás seguro de eliminar la universidad <strong>{deleteTarget?.name}</strong>?
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
