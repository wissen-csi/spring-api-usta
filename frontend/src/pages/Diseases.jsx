import { useState, useEffect } from 'react'
import { Plus, Search, Pencil, Trash2, X, Sparkles, CheckCircle, AlertTriangle, Activity } from 'lucide-react'
import { diseaseService } from '../services/api'
import { useAuth } from '../context/AuthContext'

const initialForm = { id: '', code: '', name: '', definition: '' }

export default function Diseases() {
  const { user } = useAuth()
  const isAdmin = user?.role?.includes('ADMIN')

  const [showModal, setShowModal] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')
  const [diseases, setDiseases] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [toast, setToast] = useState({ show: false, message: '' })
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)
  const [editTarget, setEditTarget] = useState(null)
  const [form, setForm] = useState(initialForm)
  const [submitting, setSubmitting] = useState(false)

  const showToast = (message) => {
    setToast({ show: true, message })
    setTimeout(() => setToast({ show: false, message: '' }), 4000)
  }

  const loadData = async () => {
    setLoading(true)
    setError('')
    try {
      const res = await diseaseService.findAll({ page: 0, size: 100 })
      setDiseases(res.data.content || [])
    } catch (err) {
      console.error('Error loading diseases:', err)
      setError('Error al conectar con el servidor. Por favor, intente de nuevo.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadData() }, [])

  const openCreateModal = () => {
    setEditTarget(null)
    setForm(initialForm)
    setShowModal(true)
  }

  const openEditModal = (disease) => {
    setEditTarget(disease)
    setForm({
      id: disease.id || '',
      code: disease.code || '',
      name: disease.name || '',
      definition: disease.definition || ''
    })
    setShowModal(true)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!form.code || !form.name) {
      alert('Por favor complete los campos obligatorios.')
      return
    }
    setSubmitting(true)
    try {
      if (editTarget) {
        await diseaseService.update(editTarget.id, form)
        showToast('Enfermedad actualizada exitosamente')
      } else {
        await diseaseService.create(form)
        showToast('Enfermedad creada exitosamente')
      }
      setShowModal(false)
      setEditTarget(null)
      setForm(initialForm)
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al guardar la enfermedad'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setSubmitting(false)
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      await diseaseService.delete(deleteTarget.id)
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
      showToast('Enfermedad eliminada exitosamente')
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar la enfermedad'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setDeleting(false)
    }
  }

  const filteredDiseases = diseases.filter(d => {
    const q = searchTerm.toLowerCase()
    return (d.code || '').toLowerCase().includes(q) ||
           (d.name || '').toLowerCase().includes(q) ||
           (d.definition || '').toLowerCase().includes(q)
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
          <h1 className="text-2xl font-bold text-slate-800">Catálogo de Enfermedades</h1>
          <p className="text-slate-500 mt-1">Gestión del catálogo de enfermedades CIE</p>
        </div>
        {isAdmin && (
          <button
            onClick={openCreateModal}
            className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/20 hover:shadow-clinical-600/30 transition-all duration-300"
          >
            <Plus className="w-4 h-4" />
            Nueva Enfermedad
          </button>
        )}
      </div>

      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
        <input
          type="text"
          placeholder="Buscar por código, nombre o definición..."
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
                <tr className="border-b border-slate-100 bg-clinical-50/50">
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Código</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Nombre</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Definición</th>
                  <th className="text-right text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-50">
                {filteredDiseases.map((d) => (
                  <tr key={d.id} className="hover:bg-clinical-50/50 transition-colors group">
                    <td className="px-6 py-4">
                      <span className="font-mono text-xs font-bold text-clinical-600 bg-clinical-50 px-2 py-0.5 rounded">{d.code}</span>
                    </td>
                    <td className="px-6 py-4 font-semibold text-slate-800 max-w-xs truncate">{d.name}</td>
                    <td className="px-6 py-4 text-slate-500 max-w-md truncate">{d.definition}</td>
                    <td className="px-6 py-4 text-right">
                      {isAdmin && (
                        <div className="flex items-center justify-end gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                          <button onClick={() => openEditModal(d)} className="p-2 hover:bg-clinical-50 rounded-lg transition-colors" title="Editar">
                            <Pencil className="w-4 h-4 text-slate-400 hover:text-clinical-600" />
                          </button>
                          <button onClick={() => { setDeleteTarget(d); setShowDeleteConfirm(true) }} className="p-2 hover:bg-red-50 rounded-lg transition-colors" title="Eliminar">
                            <Trash2 className="w-4 h-4 text-slate-400 hover:text-red-500" />
                          </button>
                        </div>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {!loading && filteredDiseases.length === 0 && (
        <div className="text-center py-16 bg-white rounded-2xl border border-slate-100 shadow-sm">
          <Activity className="w-12 h-12 text-slate-300 mx-auto mb-4" />
          <p className="text-slate-500 font-medium">No se encontraron enfermedades en el catálogo</p>
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-clinical-900/40 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-white">
              <div className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">{editTarget ? 'Editar Enfermedad' : 'Nueva Enfermedad'}</h2>
              </div>
              <button onClick={() => { setShowModal(false); setEditTarget(null); setForm(initialForm) }} className="p-2 hover:bg-slate-100 rounded-xl transition-colors">
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="p-6 space-y-4">
                {!editTarget && (
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">ID CIE *</label>
                    <input
                      required
                      type="text"
                      placeholder="Ej: http://id.who.int/icd/entity/123"
                      className="input-field py-3"
                      value={form.id}
                      onChange={(e) => setForm({ ...form, id: e.target.value })}
                    />
                  </div>
                )}
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Código CIE *</label>
                  <input
                    required
                    type="text"
                    placeholder="Ej: A00.0"
                    className="input-field py-3"
                    value={form.code}
                    onChange={(e) => setForm({ ...form, code: e.target.value })}
                  />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Nombre *</label>
                  <input
                    required
                    type="text"
                    placeholder="Nombre de la enfermedad"
                    className="input-field py-3"
                    value={form.name}
                    onChange={(e) => setForm({ ...form, name: e.target.value })}
                  />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Definición</label>
                  <textarea
                    placeholder="Definición de la enfermedad"
                    className="input-field py-3 resize-none"
                    rows={3}
                    value={form.definition}
                    onChange={(e) => setForm({ ...form, definition: e.target.value })}
                  />
                </div>
              </div>

              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-white">
                <button type="button" onClick={() => { setShowModal(false); setEditTarget(null); setForm(initialForm) }} className="btn-secondary">Cancelar</button>
                <button type="submit" disabled={submitting} className="btn-primary shadow-lg shadow-clinical-600/10 disabled:opacity-50">
                  {submitting ? 'Guardando...' : (editTarget ? 'Actualizar Enfermedad' : 'Crear Enfermedad')}
                </button>
              </div>
            </form>
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
                ¿Estás seguro de eliminar la enfermedad <strong>{deleteTarget?.code} - {deleteTarget?.name}</strong>?
              </p>
            </div>
            <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-white">
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
