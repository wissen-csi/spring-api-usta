import { useState, useEffect, useRef } from 'react'
import { Search, Plus, Trash2, X, Sparkles, CheckCircle, AlertTriangle, Activity, BookOpen, ExternalLink } from 'lucide-react'
import { studentDiseaseService, investigationService, cieService } from '../services/api'
import { useAuth } from '../context/AuthContext'

export default function Health() {
  const { user } = useAuth()
  const [activeTab, setActiveTab] = useState('diseases')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [toast, setToast] = useState({ show: false, message: '' })

  const [diseases, setDiseases] = useState([])
  const [investigations, setInvestigations] = useState([])

  const [showDiseaseModal, setShowDiseaseModal] = useState(false)
  const [showInvestigationModal, setShowInvestigationModal] = useState(false)
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)

  const [searchTerm, setSearchTerm] = useState('')
  const [cieResults, setCieResults] = useState([])
  const [searchingCie, setSearchingCie] = useState(false)
  const [selectedCie, setSelectedCie] = useState(null)
  const [showCieDropdown, setShowCieDropdown] = useState(false)
  const searchRef = useRef(null)

  const [newInvestigation, setNewInvestigation] = useState({
    repositoryUrl: '',
    description: '',
    publicationDate: ''
  })

  const showToast = (message) => {
    setToast({ show: true, message })
    setTimeout(() => setToast({ show: false, message: '' }), 4000)
  }

  const isStudent = user?.role?.includes('STUDENT')

  const loadDiseases = async () => {
    try {
      const res = await studentDiseaseService.findAll({ page: 0, size: 50 })
      let data = res.data.content || []
      if (isStudent && user?.dni) {
        data = data.filter(d => String(d.studentId) === String(user.dni))
      }
      setDiseases(data)
    } catch (err) {
      console.error('Error loading diseases:', err)
      throw err
    }
  }

  const loadInvestigations = async () => {
    try {
      const res = await investigationService.findAll({ page: 0, size: 50 })
      let data = res.data.content || []
      if (isStudent && user?.dni) {
        data = data.filter(i => String(i.studentId) === String(user.dni))
      }
      setInvestigations(data)
    } catch (err) {
      console.error('Error loading investigations:', err)
      throw err
    }
  }

  const loadData = async () => {
    setLoading(true)
    setError('')
    try {
      await Promise.all([loadDiseases(), loadInvestigations()])
    } catch (err) {
      setError('Error al conectar con el servidor. Por favor, intente de nuevo.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadData() }, [])

  useEffect(() => {
    if (searchTerm.length < 3) {
      setCieResults([])
      setShowCieDropdown(false)
      return
    }
    const timer = setTimeout(async () => {
      setSearchingCie(true)
      try {
        const res = await cieService.search(searchTerm)
        setCieResults(res.data || [])
        setShowCieDropdown(true)
      } catch (err) {
        console.error('Error searching CIE:', err)
        setCieResults([])
      } finally {
        setSearchingCie(false)
      }
    }, 400)
    return () => clearTimeout(timer)
  }, [searchTerm])

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (searchRef.current && !searchRef.current.contains(e.target)) {
        setShowCieDropdown(false)
      }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  const handleSelectCie = (item) => {
    setSelectedCie(item)
    setSearchTerm(`${item.code} - ${item.label}`)
    setShowCieDropdown(false)
  }

  const handleCreateDisease = async (e) => {
    e.preventDefault()
    if (!selectedCie) return
    try {
      await studentDiseaseService.create({
        studentId: user.dni,
        diseaseCieDTO: {
          fundationURI: selectedCie.fundationURI,
          code: selectedCie.code,
          label: selectedCie.label
        },
        isActive: true
      })
      setShowDiseaseModal(false)
      setSelectedCie(null)
      setSearchTerm('')
      setCieResults([])
      showToast('Enfermedad agregada exitosamente')
      await loadDiseases()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al agregar la enfermedad'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    }
  }

  const handleCreateInvestigation = async (e) => {
    e.preventDefault()
    if (!newInvestigation.repositoryUrl || !newInvestigation.description || !newInvestigation.publicationDate) {
      alert('Por favor complete todos los campos obligatorios.')
      return
    }
    try {
      await investigationService.create({
        repositoryUrl: newInvestigation.repositoryUrl,
        description: newInvestigation.description,
        publicationDate: newInvestigation.publicationDate,
        studentId: user.dni
      })
      setShowInvestigationModal(false)
      setNewInvestigation({ repositoryUrl: '', description: '', publicationDate: '' })
      showToast('Investigación agregada exitosamente')
      await loadInvestigations()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al agregar la investigación'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      if (deleteTarget.type === 'disease') {
        await studentDiseaseService.delete(deleteTarget.id)
        showToast('Enfermedad eliminada exitosamente')
        await loadDiseases()
      } else {
        await investigationService.delete(deleteTarget.id)
        showToast('Investigación eliminada exitosamente')
        await loadInvestigations()
      }
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setDeleting(false)
    }
  }

  const openDeleteConfirm = (item, type) => {
    setDeleteTarget({ ...item, type })
    setShowDeleteConfirm(true)
  }

  const tabs = [
    { id: 'diseases', label: 'Enfermedades', icon: Activity },
    { id: 'investigations', label: 'Investigaciones', icon: BookOpen }
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
          <h1 className="text-2xl font-bold text-slate-800">Salud Estudiantil</h1>
          <p className="text-slate-500 mt-1">Gestión de enfermedades e investigaciones</p>
        </div>
      </div>

      <div className="flex gap-1 bg-slate-100 p-1 rounded-xl w-fit">
        {tabs.map(tab => {
          const Icon = tab.icon
          return (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`flex items-center gap-2 px-5 py-2.5 rounded-lg text-sm font-semibold transition-all duration-200 ${
                activeTab === tab.id
                  ? 'bg-white text-clinical-600 shadow-sm'
                  : 'text-slate-500 hover:text-slate-700 hover:bg-slate-50'
              }`}
            >
              <Icon className="w-4 h-4" />
              {tab.label}
            </button>
          )
        })}
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
        <>
          {activeTab === 'diseases' && (
            <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
              <div className="flex items-center justify-between p-4 border-b border-slate-100">
                <h2 className="text-lg font-bold text-slate-800">Enfermedades Registradas</h2>
                <button
                  onClick={() => setShowDiseaseModal(true)}
                  className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/20"
                >
                  <Plus className="w-4 h-4" />
                  Agregar Enfermedad
                </button>
              </div>
              {diseases.length === 0 ? (
                <div className="text-center py-16">
                  <Activity className="w-12 h-12 text-slate-300 mx-auto mb-4" />
                  <p className="text-slate-500 font-medium">No hay enfermedades registradas</p>
                </div>
              ) : (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="bg-slate-50 border-b border-slate-100">
                        <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Enfermedad</th>
                        <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Código CIE</th>
                        <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Estado</th>
                        <th className="text-right p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Acciones</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-50">
                      {diseases.map(disease => (
                        <tr key={disease.id} className="hover:bg-slate-50/50 transition-colors">
                          <td className="p-4 font-medium text-slate-800">{disease.diseaseName || disease.diseaseCieDTO?.label || '-'}</td>
                          <td className="p-4 text-slate-500">{disease.diseaseCode || disease.diseaseCieDTO?.code || '-'}</td>
                          <td className="p-4">
                            <span className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold border ${
                              disease.isActive || disease.isActive === undefined || disease.isActive === null || disease.isActive === true
                                ? 'bg-emerald-100 text-emerald-700 border-emerald-200'
                                : 'bg-slate-100 text-slate-600 border-slate-200'
                            }`}>
                              {disease.isActive || disease.isActive === undefined || disease.isActive === null || disease.isActive === true ? (
                                <><CheckCircle className="w-3 h-3" /> Activo</>
                              ) : (
                                <><AlertTriangle className="w-3 h-3" /> Inactivo</>
                              )}
                            </span>
                          </td>
                          <td className="p-4 text-right">
                            <button
                              onClick={() => openDeleteConfirm(disease, 'disease')}
                              className="p-2 hover:bg-red-50 rounded-lg transition-colors"
                              title="Eliminar enfermedad"
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

          {activeTab === 'investigations' && (
            <div className="bg-white rounded-2xl border border-slate-100 shadow-sm overflow-hidden">
              <div className="flex items-center justify-between p-4 border-b border-slate-100">
                <h2 className="text-lg font-bold text-slate-800">Investigaciones Registradas</h2>
                <button
                  onClick={() => setShowInvestigationModal(true)}
                  className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/20"
                >
                  <Plus className="w-4 h-4" />
                  Agregar Investigación
                </button>
              </div>
              {investigations.length === 0 ? (
                <div className="text-center py-16">
                  <BookOpen className="w-12 h-12 text-slate-300 mx-auto mb-4" />
                  <p className="text-slate-500 font-medium">No hay investigaciones registradas</p>
                </div>
              ) : (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="bg-slate-50 border-b border-slate-100">
                        <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">URL del Repositorio</th>
                        <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Descripción</th>
                        <th className="text-left p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Fecha de Publicación</th>
                        <th className="text-right p-4 font-semibold text-slate-500 text-xs uppercase tracking-wider">Acciones</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-50">
                      {investigations.map(inv => (
                        <tr key={inv.id} className="hover:bg-slate-50/50 transition-colors">
                          <td className="p-4">
                            <a
                              href={inv.repositoryUrl}
                              target="_blank"
                              rel="noopener noreferrer"
                              className="flex items-center gap-1.5 text-clinical-600 hover:text-clinical-700 font-medium"
                            >
                              {inv.repositoryUrl}
                              <ExternalLink className="w-3.5 h-3.5 flex-shrink-0" />
                            </a>
                          </td>
                          <td className="p-4 text-slate-600 max-w-xs truncate">{inv.description}</td>
                          <td className="p-4 text-slate-500">{inv.publicationDate}</td>
                          <td className="p-4 text-right">
                            <button
                              onClick={() => openDeleteConfirm(inv, 'investigation')}
                              className="p-2 hover:bg-red-50 rounded-lg transition-colors"
                              title="Eliminar investigación"
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
        </>
      )}

      {showDiseaseModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50">
              <div className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Agregar Enfermedad</h2>
              </div>
              <button
                onClick={() => { setShowDiseaseModal(false); setSelectedCie(null); setSearchTerm(''); setCieResults([]) }}
                className="p-2 hover:bg-slate-100 rounded-xl transition-colors"
              >
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>

            <form onSubmit={handleCreateDisease}>
              <div className="p-6 space-y-4">
                <div ref={searchRef} className="relative">
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">
                    Buscar Enfermedad CIE *
                  </label>
                  <div className="relative">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                    <input
                      type="text"
                      placeholder="Escriba al menos 3 caracteres para buscar..."
                      value={searchTerm}
                      onChange={(e) => { setSearchTerm(e.target.value); setSelectedCie(null) }}
                      className="input-field pl-10"
                      required
                      autoComplete="off"
                    />
                    {searchingCie && (
                      <div className="absolute right-3 top-1/2 -translate-y-1/2">
                        <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-clinical-600"></div>
                      </div>
                    )}
                  </div>

                  {showCieDropdown && cieResults.length > 0 && (
                    <div className="absolute z-10 mt-1 w-full bg-white border border-slate-200 rounded-xl shadow-xl max-h-60 overflow-y-auto">
                      {cieResults.map((item, idx) => (
                        <button
                          key={idx}
                          type="button"
                          onClick={() => handleSelectCie(item)}
                          className="w-full text-left px-4 py-3 hover:bg-clinical-50 border-b border-slate-50 last:border-b-0 transition-colors"
                        >
                          <span className="font-mono text-xs font-bold text-clinical-600 bg-clinical-50 px-1.5 py-0.5 rounded">{item.code}</span>
                          <span className="text-sm text-slate-700 ml-2">{item.label}</span>
                        </button>
                      ))}
                    </div>
                  )}

                  {showCieDropdown && cieResults.length === 0 && !searchingCie && searchTerm.length >= 3 && (
                    <div className="absolute z-10 mt-1 w-full bg-white border border-slate-200 rounded-xl shadow-xl p-4 text-center text-sm text-slate-500">
                      No se encontraron resultados para "{searchTerm}"
                    </div>
                  )}
                </div>

                {selectedCie && (
                  <div className="p-4 bg-clinical-50 rounded-xl border border-clinical-100">
                    <p className="text-xs font-bold text-clinical-600 uppercase tracking-wider mb-1">Enfermedad seleccionada</p>
                    <p className="text-sm font-semibold text-slate-800">
                      <span className="font-mono text-clinical-600">{selectedCie.code}</span> - {selectedCie.label}
                    </p>
                  </div>
                )}
              </div>

              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50">
                <button
                  type="button"
                  onClick={() => { setShowDiseaseModal(false); setSelectedCie(null); setSearchTerm(''); setCieResults([]) }}
                  className="btn-secondary"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  disabled={!selectedCie}
                  className="btn-primary shadow-lg shadow-clinical-600/10 disabled:opacity-50"
                >
                  Agregar Enfermedad
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showInvestigationModal && (
        <div className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg overflow-hidden border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-slate-50/50">
              <div className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Nueva Investigación</h2>
              </div>
              <button
                onClick={() => setShowInvestigationModal(false)}
                className="p-2 hover:bg-slate-100 rounded-xl transition-colors"
              >
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>

            <form onSubmit={handleCreateInvestigation}>
              <div className="p-6 space-y-4">
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">
                    URL del Repositorio *
                  </label>
                  <input
                    type="url"
                    placeholder="https://github.com/usuario/proyecto"
                    value={newInvestigation.repositoryUrl}
                    onChange={(e) => setNewInvestigation({ ...newInvestigation, repositoryUrl: e.target.value })}
                    className="input-field py-3"
                    required
                  />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">
                    Descripción *
                  </label>
                  <textarea
                    placeholder="Describa brevemente la investigación..."
                    value={newInvestigation.description}
                    onChange={(e) => setNewInvestigation({ ...newInvestigation, description: e.target.value })}
                    className="input-field py-3 resize-none"
                    rows={3}
                    required
                  />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">
                    Fecha de Publicación *
                  </label>
                  <input
                    type="date"
                    value={newInvestigation.publicationDate}
                    onChange={(e) => setNewInvestigation({ ...newInvestigation, publicationDate: e.target.value })}
                    className="input-field py-3"
                    required
                  />
                </div>
              </div>

              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-slate-50/50">
                <button
                  type="button"
                  onClick={() => setShowInvestigationModal(false)}
                  className="btn-secondary"
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className="btn-primary shadow-lg shadow-clinical-600/10"
                >
                  Agregar Investigación
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
                ¿Estás seguro de eliminar{' '}
                {deleteTarget?.type === 'disease'
                  ? `la enfermedad "${deleteTarget?.diseaseName || deleteTarget?.diseaseCieDTO?.label || ''}"`
                  : `la investigación "${deleteTarget?.description || ''}"`
                }?
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
