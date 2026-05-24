import { useState, useEffect } from 'react'
import { Plus, Search, X, Sparkles, CheckCircle, Trash2, AlertTriangle, QrCode, Clock } from 'lucide-react'
import { entryPracticeService, groupService } from '../services/api'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const QR_BASE_URL = `${API_URL}/qr/generate`

export default function EntryPractices() {
  const [showModal, setShowModal] = useState(false)
  const [showQrModal, setShowQrModal] = useState(false)
  const [qrId, setQrId] = useState(null)
  const [searchTerm, setSearchTerm] = useState('')
  const [practices, setPractices] = useState([])
  const [groups, setGroups] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [toast, setToast] = useState({ show: false, message: '' })
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)

  const [newPractice, setNewPractice] = useState({
    title: '',
    startTime: '',
    endTime: '',
    idGroup: ''
  })

  const loadData = async () => {
    setLoading(true)
    setError('')
    try {
      const [practicesRes, groupsRes] = await Promise.all([
        entryPracticeService.findAll({ page: 0, size: 50 }),
        groupService.findAll({ page: 0, size: 100 })
      ])
      setPractices(practicesRes.data.content || [])
      setGroups(groupsRes.data.content || [])
    } catch (err) {
      console.error('Error loading data in EntryPractices page:', err)
      setError('Error al conectar con el servidor. Por favor, intente de nuevo.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadData() }, [])

  const getGroupName = (groupId) => {
    const group = groups.find(g => g.id === groupId)
    return group ? group.name : '—'
  }

  const handleCreate = async (e) => {
    e.preventDefault()
    if (!newPractice.title || !newPractice.startTime || !newPractice.endTime || !newPractice.idGroup) {
      alert('Por favor complete todos los campos obligatorios.')
      return
    }
    try {
      const res = await entryPracticeService.create(newPractice)
      const created = res.data
      const id = created.id || created
      setShowModal(false)
      setQrId(id)
      setShowQrModal(true)
      setToast({ show: true, message: 'Práctica creada exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 4000)
      setNewPractice({ title: '', startTime: '', endTime: '', idGroup: '' })
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al crear la práctica'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      await entryPracticeService.delete(deleteTarget.id)
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
      setToast({ show: true, message: 'Práctica eliminada exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 4000)
      await loadData()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar la práctica'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setDeleting(false)
    }
  }

  const formatDateTime = (dateStr) => {
    if (!dateStr) return ''
    const date = new Date(dateStr)
    return date.toLocaleDateString('es-CO', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  const filteredPractices = practices.filter(p => {
    const groupName = getGroupName(p.groupId).toLowerCase()
    const title = (p.title || '').toLowerCase()
    return groupName.includes(searchTerm.toLowerCase()) || title.includes(searchTerm.toLowerCase())
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
          <h1 className="text-2xl font-bold text-slate-800">Gestión de Prácticas</h1>
          <p className="text-slate-500 mt-1">Administración de sesiones de práctica médica</p>
        </div>
        <button
          onClick={() => setShowModal(true)}
          className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/20 hover:shadow-clinical-600/30 transition-all duration-300"
        >
          <Plus className="w-4 h-4" />
          Nueva Práctica
        </button>
      </div>

      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
        <input
          type="text"
          placeholder="Buscar por nombre del grupo..."
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
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Título</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Inicio</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Fin</th>
                  <th className="text-left text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Grupo</th>
                  <th className="text-center text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Código QR</th>
                  <th className="text-right text-xs font-bold text-slate-500 uppercase tracking-wider px-6 py-4">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filteredPractices.map((practice) => (
                  <tr key={practice.id} className="hover:bg-slate-50/50 transition-colors group">
                    <td className="px-6 py-4">
                      <span className="text-slate-700 font-medium">{practice.title || '—'}</span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <Clock className="w-4 h-4 text-slate-400 shrink-0" />
                        <span className="text-slate-700 font-medium">{formatDateTime(practice.startTime)}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <Clock className="w-4 h-4 text-slate-400 shrink-0" />
                        <span className="text-slate-700 font-medium">{formatDateTime(practice.endTime)}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <span className="text-slate-700 font-medium">{getGroupName(practice.groupId)}</span>
                    </td>
                    <td className="px-6 py-4 text-center">
                      <img
                        src={`${QR_BASE_URL}/${practice.id}`}
                        alt={`QR ${practice.id}`}
                        className="w-10 h-10 inline-block rounded border border-slate-200"
                        onError={(e) => {
                          e.target.onerror = null
                          e.target.style.display = 'none'
                          e.target.nextSibling.style.display = 'flex'
                        }}
                      />
                      <div className="w-10 h-10 bg-slate-100 rounded items-center justify-center hidden">
                        <QrCode className="w-5 h-5 text-slate-400" />
                      </div>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <button
                        onClick={() => { setDeleteTarget(practice); setShowDeleteConfirm(true) }}
                        className="p-2 hover:bg-red-50 rounded-lg transition-colors opacity-0 group-hover:opacity-100"
                        title="Eliminar práctica"
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

      {!loading && filteredPractices.length === 0 && (
        <div className="text-center py-16 bg-white rounded-2xl border border-slate-100 shadow-sm">
          <Clock className="w-12 h-12 text-slate-300 mx-auto mb-4" />
          <p className="text-slate-500 font-medium">No se encontraron prácticas registradas</p>
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50">
              <div className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Nueva Práctica</h2>
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
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Título *</label>
                  <input
                    required
                    type="text"
                    placeholder="Ej: Práctica de cardiología"
                    className="input-field py-3"
                    value={newPractice.title}
                    onChange={(e) => setNewPractice({ ...newPractice, title: e.target.value })}
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Grupo *</label>
                  <select
                    required
                    className="input-field py-3"
                    value={newPractice.idGroup}
                    onChange={(e) => setNewPractice({ ...newPractice, idGroup: e.target.value })}
                  >
                    <option value="">Seleccionar grupo...</option>
                    {groups.map(g => (
                      <option key={g.id} value={g.id}>{g.name}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fecha y Hora Inicio *</label>
                  <input
                    required
                    type="datetime-local"
                    className="input-field py-3"
                    value={newPractice.startTime}
                    onChange={(e) => setNewPractice({ ...newPractice, startTime: e.target.value })}
                  />
                </div>

                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fecha y Hora Fin *</label>
                  <input
                    required
                    type="datetime-local"
                    className="input-field py-3"
                    value={newPractice.endTime}
                    min={newPractice.startTime || ''}
                    onChange={(e) => setNewPractice({ ...newPractice, endTime: e.target.value })}
                  />
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
                  Crear Práctica
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showQrModal && qrId && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-md border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50">
              <div className="flex items-center gap-2">
                <QrCode className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Código QR de la Práctica</h2>
              </div>
              <button
                onClick={() => { setShowQrModal(false); setQrId(null) }}
                className="p-2 hover:bg-slate-100 rounded-xl transition-colors"
              >
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>
            <div className="p-8 flex flex-col items-center gap-4">
              <img
                src={`${QR_BASE_URL}/${qrId}`}
                alt={`Código QR práctica #${qrId}`}
                className="w-48 h-48 rounded-xl border border-slate-200 shadow-sm"
                onError={(e) => {
                  e.target.onerror = null
                  e.target.style.display = 'none'
                  e.target.nextSibling.style.display = 'flex'
                }}
              />
              <div className="w-48 h-48 bg-slate-100 rounded-xl items-center justify-center hidden">
                <QrCode className="w-12 h-12 text-slate-400" />
              </div>
              <p className="text-sm text-slate-500 text-center">
                Escanee este código QR para registrar su asistencia a la práctica
              </p>
            </div>
            <div className="flex items-center justify-end p-6 border-t border-slate-100 bg-slate-50/50">
              <button
                onClick={() => { setShowQrModal(false); setQrId(null) }}
                className="btn-primary shadow-lg shadow-clinical-600/10"
              >
                Cerrar
              </button>
            </div>
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
                ¿Estás seguro de eliminar la práctica <strong>"{deleteTarget?.title}"</strong> del grupo <strong>{getGroupName(deleteTarget?.groupId)}</strong>?
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
