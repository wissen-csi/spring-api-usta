import { useState, useEffect } from 'react'
import { Search, Filter, ChevronLeft, ChevronRight, Plus, X, Sparkles, Trash2, AlertTriangle, Pencil, ClipboardList, Upload } from 'lucide-react'
import { studentService, universityService, groupService, groupAssignmentService, rotationService } from '../services/api'
import Toast from '../components/Toast'

const getStatusColor = (status) => {
  const colors = {
    ACTIVE: 'bg-emerald-100 text-emerald-700',
    INACTIVE: 'bg-slate-100 text-slate-600',
    SUSPENDED: 'bg-red-100 text-red-700',
    PENDING_DOCUMENTS: 'bg-amber-100 text-amber-700',
    FINISHED: 'bg-clinical-100 text-clinical-700'
  }
  return colors[status] || colors.PENDING_DOCUMENTS
}

const getStatusLabel = (status) => {
  const labels = {
    ACTIVE: 'Activo',
    INACTIVE: 'Inactivo',
    SUSPENDED: 'Suspendido',
    PENDING_DOCUMENTS: 'Pendiente Documentos',
    FINISHED: 'Finalizado'
  }
  return labels[status] || status
}

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

const languageOptions = [
  { value: 'ENGLISH', label: 'Inglés' },
  { value: 'PORTUGUESE', label: 'Portugués' },
  { value: 'OTHER', label: 'Otro' }
]

const academicProgramOptions = [
  { value: 'MEDICINE', label: 'Medicina' },
  { value: 'BACTERIOLOGY', label: 'Bacteriología' },
  { value: 'NURSING', label: 'Enfermería' },
  { value: 'PHYSICAL_THERAPY', label: 'Fisioterapia' },
  { value: 'RESPIRATORY_THERAPY', label: 'Terapia Respiratoria' },
  { value: 'MEDICAL_INTERNSHIP', label: 'Internado Médico' }
]

const studentStatusOptions = [
  { value: 'ACTIVE', label: 'Activo' },
  { value: 'INACTIVE', label: 'Inactivo' },
  { value: 'SUSPENDED', label: 'Suspendido' },
  { value: 'PENDING_DOCUMENTS', label: 'Pendiente Documentos' },
  { value: 'FINISHED', label: 'Finalizado' }
]

const emptyForm = {
  name: '',
  lastName: '',
  dni: '',
  email: '',
  phoneNumber: '',
  password: '',
  placeBirth: { address: '', city: '', department: '' },
  residenceAddress: { address: '', city: '', department: '' },
  academicPrograms: 'MEDICINE',
  universityId: ''
}

export default function Students() {
  const [searchTerm, setSearchTerm] = useState('')
  const [currentPage, setCurrentPage] = useState(0)
  const [students, setStudents] = useState([])
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [toast, setToast] = useState({ show: false, message: '' })
  const [creating, setCreating] = useState(false)
  const [universities, setUniversities] = useState([])
  const [newStudent, setNewStudent] = useState(emptyForm)
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)
  const [showEditModal, setShowEditModal] = useState(false)
  const [editTarget, setEditTarget] = useState(null)
  const [editing, setEditing] = useState(false)
  const [showAssignModal, setShowAssignModal] = useState(false)
  const [assignTarget, setAssignTarget] = useState(null)
  const [availableGroups, setAvailableGroups] = useState([])
  const [availableRotations, setAvailableRotations] = useState([])
  const [selectedRotationId, setSelectedRotationId] = useState('')
  const [selectedGroupId, setSelectedGroupId] = useState('')
  const [assigning, setAssigning] = useState(false)
  const [assignments, setAssignments] = useState([])
  const [showExcelModal, setShowExcelModal] = useState(false)
  const [excelFile, setExcelFile] = useState(null)
  const [uploading, setUploading] = useState(false)
  const itemsPerPage = 5

  useEffect(() => {
    const fetchStudents = async () => {
      setLoading(true)
      setError('')
      try {
        const [{ data: studentsData }, { data: assignmentsData }] = await Promise.all([
          studentService.findAll({ page: currentPage, size: itemsPerPage }),
          groupAssignmentService.findAllDetailed({ page: 0, size: 200 })
        ])
        setStudents(studentsData.content)
        setTotalPages(studentsData.totalPages)
        setTotalElements(studentsData.totalElements)
        setAssignments(assignmentsData.content || [])
      } catch (err) {
        setError('Error al cargar los estudiantes')
      } finally {
        setLoading(false)
      }
    }
    fetchStudents()
  }, [currentPage])

  const openCreateModal = async () => {
    setShowModal(true)
    setNewStudent(emptyForm)
    try {
      const uniRes = await universityService.findAll()
      setUniversities(uniRes.data.content || uniRes.data)
    } catch (err) {
      console.error('Error loading data:', err)
    }
  }

  const filteredStudents = students.filter(student =>
    student.fullName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    student.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
    student.universityName?.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const getAssignments = (studentId) => assignments.filter(a => a.studentId === studentId)

  const handleRemoveAssignment = async (assignmentId) => {
    try {
      await groupAssignmentService.delete(assignmentId)
      setToast({ show: true, message: 'Rotación removida exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 3000)
      const { data: assignmentsData } = await groupAssignmentService.findAllDetailed({ page: 0, size: 200 })
      setAssignments(assignmentsData.content || [])
    } catch (err) {
      alert('Error al remover la rotación')
    }
  }

  const startIndex = currentPage * itemsPerPage

  const handleCreateStudent = async (e) => {
    e.preventDefault()
    if (!newStudent.name || !newStudent.lastName || !newStudent.dni || !newStudent.email || !newStudent.password) {
      alert('Por complete todos los campos obligatorios.')
      return
    }

    setCreating(true)
    try {
      await studentService.create(newStudent)

      setShowModal(false)
      setToast({
        show: true,
        message: 'Estudiante creado exitosamente'
      })
      setTimeout(() => setToast({ show: false, message: '' }), 3000)

      const { data } = await studentService.findAll({ page: currentPage, size: itemsPerPage })
      setStudents(data.content)
      setTotalPages(data.totalPages)
      setTotalElements(data.totalElements)
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al crear el estudiante'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setCreating(false)
    }
  }

  const handleExcelUpload = async () => {
    if (!excelFile) return
    setUploading(true)
    try {
      const formData = new FormData()
      formData.append('file', excelFile)
      await studentService.createExcel(formData)
      setShowExcelModal(false)
      setExcelFile(null)
      setToast({ show: true, message: 'Estudiantes importados exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 3000)
      const { data } = await studentService.findAll({ page: currentPage, size: itemsPerPage })
      setStudents(data.content)
      setTotalPages(data.totalPages)
      setTotalElements(data.totalElements)
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al importar Excel'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setUploading(false)
    }
  }

  const openAssignModal = async (student) => {
    setAssignTarget(student)
    setSelectedRotationId('')
    setSelectedGroupId('')
    try {
      const [groupsRes, rotationsRes] = await Promise.all([
        groupService.findAll({ page: 0, size: 100 }),
        rotationService.findAll({ page: 0, size: 50 })
      ])
      setAvailableGroups(groupsRes.data.content || [])
      setAvailableRotations(rotationsRes.data.content || [])
      setShowAssignModal(true)
    } catch (err) {
      alert('Error al cargar datos')
    }
  }

  const handleAssign = async (e) => {
    e.preventDefault()
    if (!selectedGroupId) return
    setAssigning(true)
    try {
      await groupAssignmentService.create({
        idStudent: assignTarget.id,
        idGroup: selectedGroupId
      })
      setShowAssignModal(false)
      setAssignTarget(null)
      setToast({ show: true, message: 'Rotación asignada exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 3000)
      const { data: assignmentsData } = await groupAssignmentService.findAllDetailed({ page: 0, size: 200 })
      setAssignments(assignmentsData.content || [])
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al asignar rotación'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setAssigning(false)
    }
  }

  const handleDelete = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      await studentService.delete(deleteTarget.id)
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
      setToast({ show: true, message: 'Estudiante eliminado exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 3000)
      const { data } = await studentService.findAll({ page: currentPage, size: itemsPerPage })
      setStudents(data.content)
      setTotalPages(data.totalPages)
      setTotalElements(data.totalElements)
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar el estudiante'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setDeleting(false)
    }
  }

  return (
    <div className="space-y-6 relative">
      <Toast show={toast.show} message={toast.message} />

      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Estudiantes</h1>
          <p className="text-slate-500 mt-1">Gestión de estudiantes en rotación</p>
        </div>
        <div className="flex gap-2">
          <button
            onClick={() => setShowExcelModal(true)}
            className="btn-secondary flex items-center gap-2 hover:border-clinical-500 hover:text-clinical-700 transition-all duration-300"
          >
            <Upload className="w-4 h-4" />
            Importar Excel
          </button>
          <button
            onClick={openCreateModal}
            className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/15 hover:shadow-clinical-600/25 transition-all duration-300"
          >
            <Plus className="w-4 h-4" />
            Crear Estudiante
          </button>
        </div>
      </div>

      <div className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm hover:shadow-md transition-shadow duration-300">
        <div className="flex flex-col sm:flex-row gap-4 mb-6">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
            <input
              type="text"
              placeholder="Buscar estudiantes por nombre, correo o universidad..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="input-field pl-10 focus:ring-clinical-500 focus:border-clinical-500"
            />
          </div>
          <button className="btn-secondary flex items-center gap-2 hover:border-slate-400 hover:text-slate-800 transition-colors">
            <Filter className="w-4 h-4" />
            Filtrar
          </button>
        </div>

        {loading ? (
          <div className="flex justify-center py-16">
            <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-clinical-600"></div>
          </div>
        ) : error ? (
          <div className="text-center py-12 text-red-600 font-medium">{error}</div>
        ) : (
          <>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-slate-200">
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Estudiante</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Universidad</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Programa</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Estado</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">ARL Fin</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Rotación</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Grupo</th>
                    <th className="text-left py-3.5 px-4 text-xs font-bold uppercase tracking-wider text-slate-400">Acciones</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100/80">
                  {filteredStudents.map((student) => {
                    const studentAssignments = getAssignments(student.id)
                    return (
                    <tr key={student.id} className="hover:bg-slate-50/40 transition-colors group">
                      <td className="py-4 px-4">
                        <div className="flex items-center gap-3">
                          <div className="w-9 h-9 bg-clinical-50 text-clinical-600 rounded-xl flex items-center justify-center font-bold text-sm group-hover:bg-clinical-600 group-hover:text-white transition-all duration-300">
                            {student.fullName?.split(' ').map(n => n[0]).join('').slice(0, 2).toUpperCase() || 'ES'}
                          </div>
                          <div>
                            <p className="text-sm font-semibold text-slate-800 group-hover:text-clinical-700 transition-colors">{student.fullName}</p>
                            <p className="text-xs text-slate-400 font-medium">{student.email}</p>
                          </div>
                        </div>
                      </td>
                      <td className="py-4 px-4 text-sm text-slate-600 font-medium">{student.universityName || 'N/A'}</td>
                      <td className="py-4 px-4 text-sm text-slate-600">{student.academicProgram || 'N/A'}</td>
                      <td className="py-4 px-4">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider ${getStatusColor(student.studentStatus)}`}>
                          {getStatusLabel(student.studentStatus)}
                        </span>
                      </td>
                      <td className="py-4 px-4 text-sm text-slate-500 font-medium">
                        {student.arlEndDate ? new Date(student.arlEndDate).toLocaleDateString('es-CO') : 'N/A'}
                      </td>
                      <td className="py-4 px-4">
                        <div className="flex flex-wrap gap-1.5">
                          {studentAssignments.length > 0 ? studentAssignments.map(a => (
                            <span key={a.assignmentId} className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold bg-clinical-50 text-clinical-700 border border-clinical-200">
                              {a.rotationType}
                            </span>
                          )) : <span className="text-xs text-slate-400">—</span>}
                        </div>
                      </td>
                      <td className="py-4 px-4">
                        <div className="flex flex-wrap gap-1.5">
                          {studentAssignments.length > 0 ? studentAssignments.map(a => (
                            <span key={a.assignmentId} className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-bold bg-amber-50 text-amber-700 border border-amber-200">
                              {a.groupName}
                              <button
                                onClick={() => handleRemoveAssignment(a.assignmentId)}
                                className="ml-0.5 hover:text-red-500 transition-colors" title="Remover de esta rotación"
                              >
                                <X className="w-3 h-3" />
                              </button>
                            </span>
                          )) : <span className="text-xs text-slate-400">—</span>}
                        </div>
                      </td>
                      <td className="py-4 px-4">
                        <div className="flex items-center gap-2">
                          <button
                            onClick={() => openAssignModal(student)}
                            className="p-2 hover:bg-amber-50 rounded-lg transition-colors group" title="Asignar rotación"
                          >
                            <ClipboardList className="w-4 h-4 text-slate-400 group-hover:text-amber-600" />
                          </button>
                          <button
                            onClick={async () => {
                              setEditTarget(student);
                              setShowEditModal(true);
                              try {
                                const uniRes = await universityService.findAll();
                                setUniversities(uniRes.data.content || uniRes.data);
                              } catch (err) {
                                console.error('Error loading universities:', err);
                              }
                            }}
                            className="p-2 hover:bg-clinical-50 rounded-lg transition-colors group" title="Editar estudiante"
                          >
                            <Pencil className="w-4 h-4 text-slate-400 group-hover:text-clinical-600" />
                          </button>
                          <button
                            onClick={() => { setDeleteTarget(student); setShowDeleteConfirm(true) }}
                            className="p-2 hover:bg-red-50 rounded-lg transition-colors group" title="Eliminar estudiante"
                          >
                            <Trash2 className="w-4 h-4 text-slate-400 group-hover:text-red-500" />
                          </button>
                        </div>
                      </td>
                    </tr>
                    )
                  })}
                  {filteredStudents.length === 0 && (
                    <tr>
                      <td colSpan={8} className="py-12 text-center text-slate-400 font-medium">
                        No se encontraron estudiantes para la búsqueda actual.
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
                  <ChevronLeft className="w-4 h-4 text-slate-600" />
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
                  <ChevronRight className="w-4 h-4 text-slate-600" />
                </button>
              </div>
            </div>
          </>
        )}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-clinical-900/40 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl overflow-hidden border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-white sticky top-0 z-10">
              <div className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Crear Estudiante</h2>
              </div>
              <button
                onClick={() => setShowModal(false)}
                className="p-2 hover:bg-slate-100 rounded-xl transition-colors"
              >
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>

            <form onSubmit={handleCreateStudent}>
              <div className="p-6 space-y-6">
                {/* Personal Information */}
                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Personal</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Nombre *</label>
                      <input required type="text" placeholder="Juan" className="input-field py-3"
                        value={newStudent.name}
                        onChange={(e) => setNewStudent({ ...newStudent, name: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Apellido *</label>
                      <input required type="text" placeholder="Pérez" className="input-field py-3"
                        value={newStudent.lastName}
                        onChange={(e) => setNewStudent({ ...newStudent, lastName: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">DNI *</label>
                      <input required type="text" placeholder="1234567890" className="input-field py-3"
                        value={newStudent.dni}
                        onChange={(e) => setNewStudent({ ...newStudent, dni: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Teléfono *</label>
                      <input required type="text" placeholder="3001234567" maxLength={11} className="input-field py-3"
                        value={newStudent.phoneNumber}
                        onChange={(e) => setNewStudent({ ...newStudent, phoneNumber: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Correo Electrónico *</label>
                      <input required type="email" placeholder="juan@correo.com" className="input-field py-3"
                        value={newStudent.email}
                        onChange={(e) => setNewStudent({ ...newStudent, email: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Contraseña *</label>
                      <input required type="password" placeholder="Contraseña" className="input-field py-3"
                        value={newStudent.password}
                        onChange={(e) => setNewStudent({ ...newStudent, password: e.target.value })} />
                    </div>
                  </div>
                </div>

                {/* Location Information */}
                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información de Ubicación</h3>
                  <div className="mb-4">
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Lugar de Nacimiento *</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input required type="text" placeholder="Dirección" className="input-field py-3"
                        value={newStudent.placeBirth.address}
                        onChange={(e) => setNewStudent({ ...newStudent, placeBirth: { ...newStudent.placeBirth, address: e.target.value } })} />
                      <input required type="text" placeholder="Ciudad" className="input-field py-3"
                        value={newStudent.placeBirth.city}
                        onChange={(e) => setNewStudent({ ...newStudent, placeBirth: { ...newStudent.placeBirth, city: e.target.value } })} />
                      <input required type="text" placeholder="Departamento" className="input-field py-3"
                        value={newStudent.placeBirth.department}
                        onChange={(e) => setNewStudent({ ...newStudent, placeBirth: { ...newStudent.placeBirth, department: e.target.value } })} />
                    </div>
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Dirección de Residencia *</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input required type="text" placeholder="Dirección" className="input-field py-3"
                        value={newStudent.residenceAddress.address}
                        onChange={(e) => setNewStudent({ ...newStudent, residenceAddress: { ...newStudent.residenceAddress, address: e.target.value } })} />
                      <input required type="text" placeholder="Ciudad" className="input-field py-3"
                        value={newStudent.residenceAddress.city}
                        onChange={(e) => setNewStudent({ ...newStudent, residenceAddress: { ...newStudent.residenceAddress, city: e.target.value } })} />
                      <input required type="text" placeholder="Departamento" className="input-field py-3"
                        value={newStudent.residenceAddress.department}
                        onChange={(e) => setNewStudent({ ...newStudent, residenceAddress: { ...newStudent.residenceAddress, department: e.target.value } })} />
                    </div>
                  </div>
                </div>

                {/* Academic Information */}
                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Académica</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Universidad *</label>
                      <select required className="input-field py-3"
                        value={newStudent.universityId}
                        onChange={(e) => setNewStudent({ ...newStudent, universityId: e.target.value })}>
                        <option value="">Seleccionar universidad...</option>
                        {universities.map(u => <option key={u.id} value={u.id}>{u.name}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Programa Académico *</label>
                      <select required className="input-field py-3"
                        value={newStudent.academicPrograms}
                        onChange={(e) => setNewStudent({ ...newStudent, academicPrograms: e.target.value })}>
                        {academicProgramOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>

              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-white sticky bottom-0">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary">
                  Cancelar
                </button>
                <button type="submit" disabled={creating} className="btn-primary shadow-lg shadow-clinical-600/10 disabled:opacity-50">
                  {creating ? 'Creando...' : 'Crear Estudiante'}
                </button>
              </div>
            </form>
            </div>
          </div>
        </div>
      )}

      {showEditModal && editTarget && (
        <div className="fixed inset-0 bg-clinical-900/40 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl overflow-hidden border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-white sticky top-0 z-10">
              <div className="flex items-center gap-2">
                <Pencil className="w-5 h-5 text-clinical-600" />
                <h2 className="text-xl font-bold text-slate-800">Editar Estudiante</h2>
              </div>
              <button onClick={() => { setShowEditModal(false); setEditTarget(null) }} className="p-2 hover:bg-slate-100 rounded-xl transition-colors">
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>
            <form onSubmit={async (e) => {
              e.preventDefault()
              setEditing(true)
              try {
                await studentService.update(editTarget.id, {
                  name: editTarget.name,
                  lastName: editTarget.lastName,
                  dni: editTarget.dni,
                  email: editTarget.email,
                  phoneNumber: editTarget.phoneNumber,
                  academicPrograms: editTarget.academicPrograms,
                  universityId: editTarget.universityId,
                  placeBirth: editTarget.placeBirth,
                  residenceAddress: editTarget.residenceAddress
                })
                setShowEditModal(false)
                setEditTarget(null)
                setToast({ show: true, message: 'Estudiante actualizado exitosamente' })
                setTimeout(() => setToast({ show: false, message: '' }), 3000)
                const { data } = await studentService.findAll({ page: currentPage, size: itemsPerPage })
                setStudents(data.content)
                setTotalPages(data.totalPages)
                setTotalElements(data.totalElements)
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
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Ubicación</h3>
                  <div className="mb-4">
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Lugar de Nacimiento</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input type="text" placeholder="Dirección" className="input-field py-3"
                        value={editTarget.placeBirth?.address || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, placeBirth: { ...editTarget.placeBirth, address: e.target.value } })} />
                      <input type="text" placeholder="Ciudad" className="input-field py-3"
                        value={editTarget.placeBirth?.city || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, placeBirth: { ...editTarget.placeBirth, city: e.target.value } })} />
                      <input type="text" placeholder="Departamento" className="input-field py-3"
                        value={editTarget.placeBirth?.department || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, placeBirth: { ...editTarget.placeBirth, department: e.target.value } })} />
                    </div>
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Dirección de Residencia</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input type="text" placeholder="Dirección" className="input-field py-3"
                        value={editTarget.residenceAddress?.address || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, residenceAddress: { ...editTarget.residenceAddress, address: e.target.value } })} />
                      <input type="text" placeholder="Ciudad" className="input-field py-3"
                        value={editTarget.residenceAddress?.city || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, residenceAddress: { ...editTarget.residenceAddress, city: e.target.value } })} />
                      <input type="text" placeholder="Departamento" className="input-field py-3"
                        value={editTarget.residenceAddress?.department || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, residenceAddress: { ...editTarget.residenceAddress, department: e.target.value } })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Académica</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Universidad</label>
                      <select className="input-field py-3"
                        value={editTarget.universityId || ''}
                        onChange={(e) => setEditTarget({ ...editTarget, universityId: e.target.value })}>
                        <option value="">Seleccionar universidad...</option>
                        {universities.map(u => <option key={u.id} value={u.id}>{u.name}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Programa Académico *</label>
                      <select required className="input-field py-3"
                        value={editTarget.academicPrograms || 'MEDICINE'}
                        onChange={(e) => setEditTarget({ ...editTarget, academicPrograms: e.target.value })}>
                        {academicProgramOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-white">
                <button type="button" onClick={() => { setShowEditModal(false); setEditTarget(null) }} className="btn-secondary">Cancelar</button>
                <button type="submit" disabled={editing} className="btn-primary disabled:opacity-50">
                  {editing ? 'Guardando...' : 'Guardar Cambios'}
                </button>
              </div>
            </form>
            </div>
          </div>
        </div>
      )}

      {showAssignModal && assignTarget && (
        <div className="fixed inset-0 bg-clinical-900/40 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-lg border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-white">
              <div className="flex items-center gap-2">
                <ClipboardList className="w-5 h-5 text-amber-600" />
                <h2 className="text-xl font-bold text-slate-800">Asignar Rotación</h2>
              </div>
              <button onClick={() => { setShowAssignModal(false); setAssignTarget(null) }} className="p-2 hover:bg-slate-100 rounded-xl transition-colors">
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>
            <form onSubmit={handleAssign}>
              <div className="p-6 space-y-4">
                <div className="p-4 bg-slate-50 rounded-2xl">
                  <p className="text-xs font-bold text-slate-500 uppercase tracking-wider mb-1">Estudiante</p>
                  <p className="text-sm font-semibold text-slate-800">{assignTarget.fullName}</p>
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Rotación</label>
                  <select
                    required
                    className="input-field py-3"
                    value={selectedRotationId}
                    onChange={(e) => { setSelectedRotationId(e.target.value); setSelectedGroupId('') }}
                  >
                    <option value="">Seleccionar rotación...</option>
                    {availableRotations.map(r => (
                      <option key={r.id} value={r.id}>
                        {r.typeRotation} — {r.hospitalLocation} ({r.doctorName})
                      </option>
                    ))}
                  </select>
                </div>
                {selectedRotationId && (
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Grupo</label>
                    <select
                      required
                      className="input-field py-3"
                      value={selectedGroupId}
                      onChange={(e) => setSelectedGroupId(e.target.value)}
                    >
                      <option value="">Seleccionar grupo...</option>
                      {availableGroups.filter(g => g.rotationId === selectedRotationId).map(g => (
                        <option key={g.id} value={g.id}>
                          {g.name} (Cap. {g.capacity})
                        </option>
                      ))}
                    </select>
                    {availableGroups.filter(g => g.rotationId === selectedRotationId).length === 0 && (
                      <p className="text-xs text-amber-600 mt-2">Esta rotación no tiene grupos. Créalos en la página de Rotaciones.</p>
                    )}
                  </div>
                )}
                {availableRotations.length === 0 && (
                  <p className="text-sm text-amber-600 bg-amber-50 p-3 rounded-xl">
                    No hay rotaciones disponibles. Crea una rotación primero.
                  </p>
                )}
              </div>
              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-white">
                <button type="button" onClick={() => { setShowAssignModal(false); setAssignTarget(null) }} className="btn-secondary">
                  Cancelar
                </button>
                <button type="submit" disabled={assigning || !selectedGroupId} className="btn-primary disabled:opacity-50">
                  {assigning ? 'Asignando...' : 'Asignar Rotación'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {showExcelModal && (
        <div className="fixed inset-0 bg-clinical-900/40 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-md border border-slate-100 transform transition-all animate-in scale-in duration-200">
            <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-white">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-clinical-50 text-clinical-600 rounded-xl flex items-center justify-center">
                  <Upload className="w-5 h-5" />
                </div>
                <div>
                  <h2 className="text-lg font-bold text-slate-800">Importar Estudiantes</h2>
                  <p className="text-sm text-slate-500">Sube un archivo Excel con los datos</p>
                </div>
              </div>
              <button onClick={() => { setShowExcelModal(false); setExcelFile(null) }} className="p-2 hover:bg-slate-100 rounded-xl transition-colors">
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>
            <div className="p-6">
              <label className="flex flex-col items-center justify-center w-full h-40 border-2 border-dashed border-slate-300 rounded-2xl cursor-pointer hover:border-clinical-400 hover:bg-clinical-50/30 transition-all duration-200">
                <Upload className="w-8 h-8 text-slate-400 mb-2" />
                <span className="text-sm font-medium text-slate-500">
                  {excelFile ? excelFile.name : 'Seleccionar archivo Excel'}
                </span>
                <input
                  type="file"
                  accept=".xlsx,.xls"
                  className="hidden"
                  onChange={(e) => setExcelFile(e.target.files[0])}
                />
              </label>
              <p className="text-xs text-slate-400 mt-3">Formatos aceptados: .xlsx, .xls</p>
            </div>
            <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-white">
              <button onClick={() => { setShowExcelModal(false); setExcelFile(null) }} className="btn-secondary">Cancelar</button>
              <button
                onClick={handleExcelUpload}
                disabled={!excelFile || uploading}
                className="btn-primary disabled:opacity-50"
              >
                {uploading ? 'Subiendo...' : 'Importar'}
              </button>
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
                ¿Estás seguro de eliminar al estudiante <strong>{deleteTarget?.fullName}</strong>?
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
