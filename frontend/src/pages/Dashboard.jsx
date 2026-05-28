import { useState, useEffect } from 'react'
import { Users, Stethoscope, ClipboardList, TrendingUp, Calendar, BookOpen, MapPin, User, X, Pencil, CheckCircle, Search, Activity, Plus, Trash2 } from 'lucide-react'
import { studentService, doctorService, rotationService, groupAssignmentService, studentDiseaseService, cieService, universityService, academicPeriodService, studentAcademicPeriodService } from '../services/api'
import { useAuth } from '../context/AuthContext'

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

const specialtyOptions = [
  { value: 'OTHER', label: 'Otra' },
  { value: 'MEDICINA_INTERNA', label: 'Medicina Interna' },
  { value: 'CIRUGIA_GENERAL', label: 'Cirugía General' },
  { value: 'PEDIATRIA', label: 'Pediatría' },
  { value: 'GINECOLOGIA_OBSTETRICIA', label: 'Ginecología y Obstetricia' },
  { value: 'URGENCIAS', label: 'Urgencias' },
  { value: 'CARDIOLOGIA', label: 'Cardiología' },
  { value: 'DERMATOLOGIA', label: 'Dermatología' },
  { value: 'NEUROLOGIA', label: 'Neurología' },
  { value: 'PSIQUIATRIA', label: 'Psiquiatría' },
  { value: 'ORTOPEDIA', label: 'Ortopedia' },
  { value: 'OFTALMOLOGIA', label: 'Oftalmología' },
  { value: 'OTORRINOLARINGOLOGIA', label: 'Otorrinolaringología' },
  { value: 'ANESTESIOLOGIA', label: 'Anestesiología' },
  { value: 'RADIOLOGIA', label: 'Radiología' },
  { value: 'MEDICINA_FAMILIAR', label: 'Medicina Familiar' },
  { value: 'CUIDADOS_INTENSIVOS', label: 'Cuidados Intensivos' },
  { value: 'ONCOLOGIA', label: 'Oncología' },
  { value: 'NEFROLOGIA', label: 'Nefrología' },
  { value: 'ENDOCRINOLOGIA', label: 'Endocrinología' },
  { value: 'GASTROENTEROLOGIA', label: 'Gastroenterología' },
  { value: 'REUMATOLOGIA', label: 'Reumatología' },
  { value: 'NEUMOLOGIA', label: 'Neumología' },
  { value: 'HEMATOLOGIA', label: 'Hematología' },
  { value: 'INFECTOLOGIA', label: 'Infectología' },
  { value: 'MEDICINA_DEPORTIVA', label: 'Medicina Deportiva' },
  { value: 'GENETICA', label: 'Genética' },
  { value: 'PATOLOGIA', label: 'Patología' },
  { value: 'MEDICINA_LEGAL', label: 'Medicina Legal' },
  { value: 'FARMACOLOGIA', label: 'Farmacología' }
]

const getStatusColor = (status) => {
  const colors = {
    ACTIVE: 'bg-emerald-100 text-emerald-700',
    INACTIVE: 'bg-slate-100 text-slate-600',
    SUSPENDED: 'bg-red-100 text-red-700',
    PENDING_DOCUMENTS: 'bg-amber-100 text-amber-700',
    FINISHED: 'bg-clinical-100 text-clinical-700'
  }
  return colors[status] || 'bg-amber-100 text-amber-700'
}

const getStatusLabel = (status) => {
  const labels = {
    ACTIVE: 'Activo',
    INACTIVE: 'Inactivo',
    SUSPENDED: 'Suspendido',
    PENDING_DOCUMENTS: 'Pendiente',
    FINISHED: 'Finalizado'
  }
  return labels[status] || status
}

export default function Dashboard() {
  const { user } = useAuth()
  const isStudent = user?.role?.includes('STUDENT')
  const isDoctor = user?.role?.includes('DOCTOR')

  if (isStudent) return <StudentDashboard />
  if (isDoctor) return <DoctorDashboard />
  return <AdminDashboard />
}

function StudentDashboard() {
  const { user } = useAuth()
  const [profile, setProfile] = useState(null)
  const [groupAssignments, setGroupAssignments] = useState([])
  const [loading, setLoading] = useState(true)
  const [showEditModal, setShowEditModal] = useState(false)
  const [editForm, setEditForm] = useState(null)
  const [saving, setSaving] = useState(false)
  const [toast, setToast] = useState({ show: false, message: '' })
  const [diseases, setDiseases] = useState([])
  const [diseaseSearchTerm, setDiseaseSearchTerm] = useState('')
  const [cieResults, setCieResults] = useState([])
  const [searchingCie, setSearchingCie] = useState(false)
  const [selectedCie, setSelectedCie] = useState(null)
  const [showCieDropdown, setShowCieDropdown] = useState(false)
  const [addingDisease, setAddingDisease] = useState(false)
  const [deletingDisease, setDeletingDisease] = useState(null)
  const [academicPeriods, setAcademicPeriods] = useState([])

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true)
      try {
        const [profileRes, groupRes, acadRes] = await Promise.all([
          studentService.findSelf(),
          groupAssignmentService.findSelfDetailed({ page: 0, size: 10 }),
          academicPeriodService.findAll()
        ])
        setProfile(profileRes.data)
        setGroupAssignments(groupRes.data?.content || [])
        setAcademicPeriods(acadRes.data?.content || [])
      } catch (err) {
        console.error('Error loading student dashboard:', err)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  const openEditModal = () => {
    loadDiseases()
    setSelectedCie(null)
    setDiseaseSearchTerm('')
    setCieResults([])
    const studentAcad = Array.isArray(profile.studentAcademicPeriods) ? profile.studentAcademicPeriods : []
    const latestPeriod = studentAcad.length > 0 ? studentAcad[studentAcad.length - 1] : null
    setEditForm({
      name: profile.name || '',
      lastName: profile.lastName || '',
      dni: profile.dni || '',
      phoneNumber: profile.phoneNumber || '',
      email: profile.email || '',
      maritalStatus: profile.maritalStatus || 'OTHER',
      typeBlood: profile.typeBlood || 'O_POSITIVE',
      weight: profile.weight ?? '',
      imc: profile.imc ?? '',
      secondLanguage: profile.secondLanguage || 'OTHER',
      academicPrograms: profile.academicProgram || 'MEDICINE',
      studentStatus: profile.studentStatus || 'ACTIVE',
      courseApproved: profile.courseApproved || false,
      entryDateAcademicProgram: profile.entryDateAcademicProgram || '',
      startInductionDate: profile.startInductionDate || '',
      endInductionDate: profile.endInductionDate || '',
      arlStartDate: profile.arlStartDate || '',
      arlEndDate: profile.arlEndDate || '',
      hobbies: profile.hobbies || '',
      universityId: profile.universityId || '',
      placeBirth: profile.placeBirth || { address: '', city: '', department: '' },
      residenceAddress: profile.residenceAddress || { address: '', city: '', department: '' },
      academicPeriodId: latestPeriod?.academicPeriodId || '',
      semester: latestPeriod?.semester || '',
      periodYear: latestPeriod?.periodYear || ''
    })
    setShowEditModal(true)
  }

  const handleSave = async (e) => {
    e.preventDefault()
    setSaving(true)
    try {
      await studentService.updateSelf({
        name: editForm.name || null,
        lastName: editForm.lastName || null,
        dni: editForm.dni || null,
        phoneNumber: editForm.phoneNumber || null,
        email: editForm.email || null,
        maritalStatus: editForm.maritalStatus,
        typeBlood: editForm.typeBlood,
        weight: editForm.weight ? parseFloat(editForm.weight) : null,
        imc: editForm.imc ? parseFloat(editForm.imc) : null,
        secondLanguage: editForm.secondLanguage,
        academicPrograms: editForm.academicPrograms,
        studentStatus: editForm.studentStatus,
        courseApproved: editForm.courseApproved,
        entryDateAcademicProgram: editForm.entryDateAcademicProgram || null,
        startInductionDate: editForm.startInductionDate || null,
        endInductionDate: editForm.endInductionDate || null,
        arlStartDate: editForm.arlStartDate || null,
        arlEndDate: editForm.arlEndDate || null,
        hobbies: editForm.hobbies || null,
        universityId: editForm.universityId || null,
        placeBirth: editForm.placeBirth,
        residenceAddress: editForm.residenceAddress
      })
      if (editForm.academicPeriodId && editForm.semester) {
        try {
          await studentAcademicPeriodService.create({
            academicPeriodId: editForm.academicPeriodId,
            semester: editForm.semester,
            periodYear: editForm.periodYear || null
          })
        } catch (e) {
          console.error('Error saving semester:', e)
        }
      }
      setShowEditModal(false)
      setToast({ show: true, message: 'Perfil actualizado exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 4000)
      const profileRes = await studentService.findSelf()
      setProfile(profileRes.data)
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al actualizar perfil'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setSaving(false)
    }
  }

  const loadDiseases = async () => {
    try {
      const res = await studentDiseaseService.findAll({ page: 0, size: 50 })
      let data = res.data.content || []
      if (profile?.id) {
        data = data.filter(d => String(d.studentId) === String(profile.id))
      }
      setDiseases(data)
    } catch (err) {
      console.error('Error loading diseases:', err)
    }
  }

  useEffect(() => {
    if (diseaseSearchTerm.length < 3) {
      setCieResults([])
      setShowCieDropdown(false)
      return
    }
    const timer = setTimeout(async () => {
      setSearchingCie(true)
      try {
        const res = await cieService.search(diseaseSearchTerm)
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
  }, [diseaseSearchTerm])

  const handleSelectCie = (item) => {
    setSelectedCie(item)
    setDiseaseSearchTerm(`${item.code} - ${item.label}`)
    setShowCieDropdown(false)
  }

  const handleAddDisease = async () => {
    if (!selectedCie) return
    setAddingDisease(true)
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
      setSelectedCie(null)
      setDiseaseSearchTerm('')
      setCieResults([])
      await loadDiseases()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al agregar la enfermedad'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setAddingDisease(false)
    }
  }

  const handleRemoveDisease = async (id) => {
    setDeletingDisease(id)
    try {
      await studentDiseaseService.delete(id)
      await loadDiseases()
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al eliminar la enfermedad'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setDeletingDisease(null)
    }
  }

  if (loading) {
    return (
      <div className="flex justify-center py-24">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-clinical-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
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
          <h1 className="text-2xl font-bold text-slate-800">Mi Perfil</h1>
          <p className="text-slate-500 mt-1">Información personal y grupo de rotación</p>
        </div>
        <button
          onClick={openEditModal}
          className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/15"
        >
          <Pencil className="w-4 h-4" />
          Editar Perfil
        </button>
      </div>

      {profile && (
        <div className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm">
          <div className="flex items-center gap-4 mb-6">
            <div className="w-14 h-14 bg-clinical-100 rounded-full flex items-center justify-center">
              <User className="w-7 h-7 text-clinical-600" />
            </div>
            <div>
              <h2 className="text-xl font-semibold text-slate-800">{profile.fullName}</h2>
              <p className="text-sm text-slate-500">{profile.email}</p>
            </div>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            <div className="p-4 bg-slate-50 rounded-2xl">
              <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">Documento</p>
              <p className="text-sm font-semibold text-slate-800 mt-1">{profile.dni}</p>
            </div>
            <div className="p-4 bg-slate-50 rounded-2xl">
              <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">Teléfono</p>
              <p className="text-sm font-semibold text-slate-800 mt-1">{profile.phoneNumber || 'N/A'}</p>
            </div>
            <div className="p-4 bg-slate-50 rounded-2xl">
              <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">Universidad</p>
              <p className="text-sm font-semibold text-slate-800 mt-1">{profile.universityName || 'N/A'}</p>
            </div>
            <div className="p-4 bg-slate-50 rounded-2xl">
              <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">Programa</p>
              <p className="text-sm font-semibold text-slate-800 mt-1">{profile.academicProgram || 'N/A'}</p>
            </div>
            {(() => {
              const per = Array.isArray(profile.studentAcademicPeriods) && profile.studentAcademicPeriods.length > 0
                ? profile.studentAcademicPeriods[profile.studentAcademicPeriods.length - 1] : null
              return per ? (
                <div className="p-4 bg-slate-50 rounded-2xl">
                  <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">Semestre</p>
                  <p className="text-sm font-semibold text-slate-800 mt-1">{per.semester}°</p>
                </div>
              ) : null
            })()}
            <div className="p-4 bg-slate-50 rounded-2xl">
              <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">Estado</p>
              <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium mt-1 ${getStatusColor(profile.studentStatus)}`}>
                {getStatusLabel(profile.studentStatus)}
              </span>
            </div>
            <div className="p-4 bg-slate-50 rounded-2xl">
              <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">Vencimiento ARL</p>
              <p className="text-sm font-semibold text-slate-800 mt-1">
                {profile.arlEndDate ? new Date(profile.arlEndDate).toLocaleDateString('es-CO') : 'N/A'}
              </p>
            </div>
          </div>
        </div>
      )}

      {groupAssignments.length > 0 && (
        <div className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm">
          <h2 className="text-lg font-semibold text-slate-800 mb-4">Mi Grupo de Rotación</h2>
          <div className="space-y-4">
            {groupAssignments.map((ga) => (
              <div key={ga.assignmentId} className="p-5 bg-gradient-to-br from-clinical-50 to-blue-50 rounded-2xl border border-clinical-100">
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center gap-2">
                    <BookOpen className="w-5 h-5 text-clinical-600" />
                    <span className="font-semibold text-slate-800">{ga.groupName}</span>
                  </div>
                  <span className="text-xs text-slate-500 bg-white px-2 py-1 rounded-lg">Capacidad: {ga.capacity}</span>
                </div>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 text-sm">
                  <div className="flex items-center gap-2 text-slate-600">
                    <MapPin className="w-4 h-4 text-slate-400" />
                    <span>{ga.hospitalLocation} — {ga.rotationType}</span>
                  </div>
                  <div className="flex items-center gap-2 text-slate-600">
                    <Calendar className="w-4 h-4 text-slate-400" />
                    <span>{ga.startDate ? new Date(ga.startDate).toLocaleDateString('es-CO') : '—'} al {ga.completionDate ? new Date(ga.completionDate).toLocaleDateString('es-CO') : '—'}</span>
                  </div>
                  <div className="flex items-center gap-2 text-slate-600 sm:col-span-2">
                    <Stethoscope className="w-4 h-4 text-slate-400" />
                    <span>Médico supervisor: {ga.doctorName}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {groupAssignments.length === 0 && !loading && (
        <div className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm">
          <div className="text-center py-8 text-slate-500">
            <BookOpen className="w-12 h-12 mx-auto mb-3 text-slate-300" />
            <p className="font-medium">No tienes grupo de rotación asignado</p>
            <p className="text-sm mt-1">Contacta con tu administrador para ser asignado a un grupo.</p>
          </div>
        </div>
      )}

      {showEditModal && editForm && (
        <div className="fixed inset-0 bg-clinical-900/40 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl overflow-hidden border border-slate-100">
            <div className="max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-white sticky top-0 z-10">
              <h2 className="text-xl font-bold text-slate-800">Editar Mi Perfil</h2>
              <button onClick={() => setShowEditModal(false)} className="p-2 hover:bg-slate-100 rounded-xl transition-colors">
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>
            <form onSubmit={handleSave}>
              <div className="p-6 space-y-6">
                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Personal</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Nombre</label>
                      <input type="text" className="input-field py-3" value={editForm.name}
                        onChange={(e) => setEditForm({ ...editForm, name: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Apellido</label>
                      <input type="text" className="input-field py-3" value={editForm.lastName}
                        onChange={(e) => setEditForm({ ...editForm, lastName: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">DNI</label>
                      <input type="text" className="input-field py-3" value={editForm.dni}
                        onChange={(e) => setEditForm({ ...editForm, dni: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Teléfono</label>
                      <input type="text" className="input-field py-3" value={editForm.phoneNumber}
                        onChange={(e) => setEditForm({ ...editForm, phoneNumber: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Correo Electrónico</label>
                      <input type="email" className="input-field py-3" value={editForm.email}
                        onChange={(e) => setEditForm({ ...editForm, email: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Estado Civil</label>
                      <select className="input-field py-3" value={editForm.maritalStatus}
                        onChange={(e) => setEditForm({ ...editForm, maritalStatus: e.target.value })}>
                        {maritalStatusOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Tipo de Sangre</label>
                      <select className="input-field py-3" value={editForm.typeBlood}
                        onChange={(e) => setEditForm({ ...editForm, typeBlood: e.target.value })}>
                        {typeBloodOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Peso (kg)</label>
                      <input type="number" step="0.1" className="input-field py-3" value={editForm.weight}
                        onChange={(e) => setEditForm({ ...editForm, weight: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">IMC</label>
                      <input type="number" step="0.1" className="input-field py-3" value={editForm.imc}
                        onChange={(e) => setEditForm({ ...editForm, imc: e.target.value })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Ubicación</h3>
                  <div className="mb-4">
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Lugar de Nacimiento</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input type="text" placeholder="Dirección" className="input-field py-3"
                        value={editForm.placeBirth.address}
                        onChange={(e) => setEditForm({ ...editForm, placeBirth: { ...editForm.placeBirth, address: e.target.value } })} />
                      <input type="text" placeholder="Ciudad" className="input-field py-3"
                        value={editForm.placeBirth.city}
                        onChange={(e) => setEditForm({ ...editForm, placeBirth: { ...editForm.placeBirth, city: e.target.value } })} />
                      <input type="text" placeholder="Departamento" className="input-field py-3"
                        value={editForm.placeBirth.department}
                        onChange={(e) => setEditForm({ ...editForm, placeBirth: { ...editForm.placeBirth, department: e.target.value } })} />
                    </div>
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Dirección de Residencia</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input type="text" placeholder="Dirección" className="input-field py-3"
                        value={editForm.residenceAddress.address}
                        onChange={(e) => setEditForm({ ...editForm, residenceAddress: { ...editForm.residenceAddress, address: e.target.value } })} />
                      <input type="text" placeholder="Ciudad" className="input-field py-3"
                        value={editForm.residenceAddress.city}
                        onChange={(e) => setEditForm({ ...editForm, residenceAddress: { ...editForm.residenceAddress, city: e.target.value } })} />
                      <input type="text" placeholder="Departamento" className="input-field py-3"
                        value={editForm.residenceAddress.department}
                        onChange={(e) => setEditForm({ ...editForm, residenceAddress: { ...editForm.residenceAddress, department: e.target.value } })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Académica</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Segundo Idioma</label>
                      <select className="input-field py-3" value={editForm.secondLanguage}
                        onChange={(e) => setEditForm({ ...editForm, secondLanguage: e.target.value })}>
                        {languageOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fecha Ingreso Programa</label>
                      <input type="date" className="input-field py-3" value={editForm.entryDateAcademicProgram}
                        onChange={(e) => setEditForm({ ...editForm, entryDateAcademicProgram: e.target.value })} />
                    </div>
                    <div className="flex items-end pb-3">
                      <label className="flex items-center gap-3 cursor-pointer">
                        <input type="checkbox" checked={editForm.courseApproved}
                          onChange={(e) => setEditForm({ ...editForm, courseApproved: e.target.checked })}
                          className="w-4 h-4 rounded border-slate-300 text-clinical-600 focus:ring-clinical-500" />
                        <span className="text-xs font-bold text-slate-500 uppercase tracking-wider">Curso Aprobado</span>
                      </label>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Semestre</label>
                      <select className="input-field py-3" value={editForm.semester}
                        onChange={(e) => setEditForm({ ...editForm, semester: e.target.value })}>
                        <option value="">Seleccionar</option>
                        {[1,2,3,4,5,6,7,8,9,10,11,12].map(n => <option key={n} value={n}>{n}° Semestre</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Periodo Académico</label>
                      <select className="input-field py-3" value={editForm.academicPeriodId}
                        onChange={(e) => setEditForm({ ...editForm, academicPeriodId: e.target.value })}>
                        <option value="">Seleccionar</option>
                        {academicPeriods.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Año</label>
                      <input type="number" className="input-field py-3" value={editForm.periodYear}
                        onChange={(e) => setEditForm({ ...editForm, periodYear: e.target.value })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Inducción y ARL</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Inicio Inducción</label>
                      <input type="date" className="input-field py-3" value={editForm.startInductionDate}
                        onChange={(e) => setEditForm({ ...editForm, startInductionDate: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fin Inducción</label>
                      <input type="date" className="input-field py-3" value={editForm.endInductionDate}
                        min={editForm.startInductionDate || ''}
                        onChange={(e) => setEditForm({ ...editForm, endInductionDate: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Inicio ARL</label>
                      <input type="date" className="input-field py-3" value={editForm.arlStartDate}
                        onChange={(e) => setEditForm({ ...editForm, arlStartDate: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Fin ARL</label>
                      <input type="date" className="input-field py-3" value={editForm.arlEndDate}
                        min={editForm.arlStartDate || ''}
                        onChange={(e) => setEditForm({ ...editForm, arlEndDate: e.target.value })} />
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Adicional</h3>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Hobbies</label>
                    <textarea rows={3} className="input-field py-3" value={editForm.hobbies}
                      onChange={(e) => setEditForm({ ...editForm, hobbies: e.target.value })} />
                  </div>
                </div>

                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Enfermedades</h3>
                  <div className="space-y-3">
                    <div className="flex gap-2 items-start">
                      <div className="relative flex-1">
                        <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                        <input
                          type="text"
                          placeholder="Buscar enfermedad CIE (mín. 3 caracteres)..."
                          value={diseaseSearchTerm}
                          onChange={(e) => { setDiseaseSearchTerm(e.target.value); setSelectedCie(null) }}
                          className="input-field pl-10 py-3"
                          autoComplete="off"
                        />
                        {searchingCie && (
                          <div className="absolute right-3 top-1/2 -translate-y-1/2">
                            <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-clinical-600"></div>
                          </div>
                        )}
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
                      </div>
                      <button
                        type="button"
                        onClick={handleAddDisease}
                        disabled={!selectedCie || addingDisease}
                        className="btn-primary px-4 py-3 disabled:opacity-50 whitespace-nowrap"
                      >
                        {addingDisease ? 'Agregando...' : 'Agregar'}
                      </button>
                    </div>
                    {selectedCie && (
                      <div className="p-3 bg-clinical-50 rounded-xl border border-clinical-100">
                        <p className="text-xs font-bold text-clinical-600 uppercase tracking-wider mb-1">Enfermedad seleccionada</p>
                        <p className="text-sm font-semibold text-slate-800">
                          <span className="font-mono text-clinical-600">{selectedCie.code}</span> - {selectedCie.label}
                        </p>
                      </div>
                    )}
                    {diseases.length > 0 ? (
                      <div className="space-y-2">
                        {diseases.map(d => (
                          <div key={d.id} className="flex items-center justify-between p-3 bg-slate-50 rounded-xl">
                            <div className="flex items-center gap-2">
                              <Activity className="w-4 h-4 text-slate-400" />
                              <div>
                                <p className="text-sm font-medium text-slate-700">{d.diseaseName || d.diseaseCieDTO?.label || '-'}</p>
                                <p className="text-xs text-slate-400">{d.diseaseCode || d.diseaseCieDTO?.code || '-'}</p>
                              </div>
                            </div>
                            <button
                              type="button"
                              onClick={() => handleRemoveDisease(d.id)}
                              disabled={deletingDisease === d.id}
                              className="p-2 hover:bg-red-50 rounded-lg transition-colors"
                            >
                              <Trash2 className="w-4 h-4 text-slate-400 hover:text-red-500" />
                            </button>
                          </div>
                        ))}
                      </div>
                    ) : (
                      <p className="text-sm text-slate-400 text-center py-4">No tienes enfermedades registradas</p>
                    )}
                  </div>
                </div>
              </div>
              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-white">
                <button type="button" onClick={() => setShowEditModal(false)} className="btn-secondary">Cancelar</button>
                <button type="submit" disabled={saving} className="btn-primary disabled:opacity-50">
                  {saving ? 'Guardando...' : 'Guardar Cambios'}
                </button>
              </div>
            </form>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

function DoctorDashboard() {
  const { user } = useAuth()
  const [profile, setProfile] = useState(null)
  const [students, setStudents] = useState([])
  const [rotations, setRotations] = useState([])
  const [assignments, setAssignments] = useState([])
  const [universities, setUniversities] = useState([])
  const [loading, setLoading] = useState(true)
  const [showEditModal, setShowEditModal] = useState(false)
  const [editForm, setEditForm] = useState(null)
  const [saving, setSaving] = useState(false)
  const [toast, setToast] = useState({ show: false, message: '' })

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true)
      try {
        const [profileRes, studentsRes, rotationsRes, assignRes, unisRes] = await Promise.all([
          doctorService.findSelf(),
          studentService.findAll({ page: 0, size: 100 }),
          rotationService.findSelf({ page: 0, size: 100 }),
          groupAssignmentService.findAllDetailed({ page: 0, size: 1000 }),
          universityService.findAll()
        ])
        setProfile(profileRes.data)
        setStudents(studentsRes.data.content || [])
        setRotations(rotationsRes.data.content || [])
        setAssignments(assignRes.data.content || [])
        setUniversities(unisRes.data.content || [])
      } catch (err) {
        console.error('Error loading doctor dashboard:', err)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  const openEditModal = () => {
    setEditForm({
      name: profile.name || '',
      lastName: profile.lastName || '',
      dni: profile.dni || '',
      phoneNumber: profile.phoneNumber || '',
      email: profile.email || '',
      maritalStatus: profile.maritalStatus || 'OTHER',
      typeBlood: profile.typeBlood || 'O_POSITIVE',
      weight: profile.weight ?? '',
      imc: profile.imc ?? '',
      specialty: profile.specialty || 'OTHER',
      universityId: profile.universityId || '',
      placeBirth: profile.placeBirth || { address: '', city: '', department: '' },
      residenceAddress: profile.residenceAddress || { address: '', city: '', department: '' }
    })
    setShowEditModal(true)
  }

  const handleSave = async (e) => {
    e.preventDefault()
    setSaving(true)
    try {
      await doctorService.updateSelf({
        name: editForm.name || null,
        lastName: editForm.lastName || null,
        dni: editForm.dni || null,
        phoneNumber: editForm.phoneNumber || null,
        email: editForm.email || null,
        maritalStatus: editForm.maritalStatus,
        typeBlood: editForm.typeBlood,
        weight: editForm.weight ? parseFloat(editForm.weight) : null,
        imc: editForm.imc ? parseFloat(editForm.imc) : null,
        specialty: editForm.specialty,
        universityId: editForm.universityId || null,
        placeBirth: editForm.placeBirth,
        residenceAddress: editForm.residenceAddress
      })
      setShowEditModal(false)
      setToast({ show: true, message: 'Perfil actualizado exitosamente' })
      setTimeout(() => setToast({ show: false, message: '' }), 4000)
      const profileRes = await doctorService.findSelf()
      setProfile(profileRes.data)
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al actualizar perfil'
      alert(typeof msg === 'string' ? msg : JSON.stringify(msg))
    } finally {
      setSaving(false)
    }
  }

  const now = new Date()
  const activeRotations = rotations.filter(r => !r.completionDate || new Date(r.completionDate + 'T23:59:59') >= now)
  const completedRotations = rotations.filter(r => r.completionDate && new Date(r.completionDate + 'T23:59:59') < now)
  const rotationIds = rotations.map(r => r.id)
  const myAssignments = assignments.filter(a => rotationIds.includes(a.rotationId))
  const assignedStudentIds = new Set(myAssignments.map(a => a.studentId))
  const activeStudentsInRotations = assignedStudentIds.size
  const assignedStudents = students.filter(s => assignedStudentIds.has(s.id))

  if (loading) {
    return (
      <div className="flex justify-center py-24">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-clinical-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
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
          <h1 className="text-2xl font-bold text-slate-800">Dashboard</h1>
          <p className="text-slate-500 mt-1">Panel del médico supervisor</p>
        </div>
        <button onClick={openEditModal} className="btn-primary flex items-center gap-2 shadow-lg shadow-clinical-600/15">
          <Pencil className="w-4 h-4" />
          Editar Perfil
        </button>
      </div>

      {profile && (
        <div className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm">
          <div className="flex items-center gap-4 mb-6">
            <div className="w-14 h-14 bg-clinical-100 rounded-full flex items-center justify-center">
              <User className="w-7 h-7 text-clinical-600" />
            </div>
            <div>
              <h2 className="text-xl font-semibold text-slate-800">{profile.name} {profile.lastName}</h2>
              <p className="text-sm text-slate-500">{profile.email}</p>
              <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-clinical-100 text-clinical-700 mt-1">
                {profile.specialty || 'Sin especialidad'}
              </span>
            </div>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            <div className="p-4 bg-slate-50 rounded-2xl">
              <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">Documento</p>
              <p className="text-sm font-semibold text-slate-800 mt-1">{profile.dni}</p>
            </div>
            <div className="p-4 bg-slate-50 rounded-2xl">
              <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">Teléfono</p>
              <p className="text-sm font-semibold text-slate-800 mt-1">{profile.phoneNumber || 'N/A'}</p>
            </div>
            <div className="p-4 bg-slate-50 rounded-2xl">
              <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">Universidad</p>
              <p className="text-sm font-semibold text-slate-800 mt-1">{profile.universityName || 'N/A'}</p>
            </div>
            <div className="p-4 bg-slate-50 rounded-2xl">
              <p className="text-xs font-medium text-slate-500 uppercase tracking-wider">Tipo de Sangre</p>
              <p className="text-sm font-semibold text-slate-800 mt-1">{profile.typeBlood || 'N/A'}</p>
            </div>
          </div>
        </div>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {[
          { title: 'Estudiantes Activos', value: activeStudentsInRotations, icon: Users, color: 'text-blue-600', hoverBorder: 'hover:border-blue-200', bgGrad: 'from-blue-500 to-indigo-600', bgColor: 'bg-blue-50' },
          { title: 'Rotaciones Activas', value: activeRotations.length, icon: ClipboardList, color: 'text-amber-600', hoverBorder: 'hover:border-amber-200', bgGrad: 'from-amber-500 to-orange-600', bgColor: 'bg-amber-50' },
          { title: 'Completadas', value: completedRotations.length, icon: TrendingUp, color: 'text-purple-600', hoverBorder: 'hover:border-purple-200', bgGrad: 'from-purple-500 to-pink-600', bgColor: 'bg-purple-50' },
        ].map((card, index) => (
          <div key={index} className={`bg-white rounded-3xl p-6 border border-slate-100 shadow-sm hover:shadow-xl hover:-translate-y-1 transform transition-all duration-300 group overflow-hidden relative ${card.hoverBorder}`}>
            <div className="flex items-start justify-between">
              <div className={`p-3 rounded-2xl ${card.bgColor} group-hover:scale-110 transition-transform duration-300`}>
                <card.icon className={`w-6 h-6 ${card.color}`} />
              </div>
            </div>
            <div className="mt-4">
              <p className="text-3xl font-bold text-slate-800 tracking-tight group-hover:text-clinical-700 transition-colors">{card.value}</p>
              <p className="text-sm font-medium text-slate-500 mt-1">{card.title}</p>
            </div>
            <div className={`absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r ${card.bgGrad} opacity-0 group-hover:opacity-100 transition-opacity duration-300`} />
          </div>
        ))}
      </div>

      {activeRotations.length > 0 && (
        <div className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm">
          <h2 className="text-lg font-semibold text-slate-800 mb-4 flex items-center gap-2">
            <ClipboardList className="w-5 h-5 text-amber-600" />
            Rotaciones Activas
          </h2>
          <div className="space-y-3">
            {activeRotations.map(r => (
              <div key={r.id} className="p-4 bg-amber-50 rounded-2xl border border-amber-100">
                <div className="flex items-center justify-between mb-2">
                  <span className="font-semibold text-slate-800">{r.typeRotation || 'OTHER'}</span>
                  <span className="text-xs bg-white px-2 py-1 rounded-lg text-amber-700 font-medium">{r.hospitalLocation}</span>
                </div>
                <div className="flex items-center gap-4 text-sm text-slate-600">
                  <span><Calendar className="w-3.5 h-3.5 inline mr-1" />{new Date(r.startDate).toLocaleDateString('es-CO')}</span>
                  <span>→</span>
                  <span>{r.completionDate ? new Date(r.completionDate).toLocaleDateString('es-CO') : 'Sin fecha fin'}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {completedRotations.length > 0 && (
        <div className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm">
          <h2 className="text-lg font-semibold text-slate-800 mb-4 flex items-center gap-2">
            <TrendingUp className="w-5 h-5 text-purple-600" />
            Rotaciones Completadas
          </h2>
          <div className="space-y-3">
            {completedRotations.map(r => (
              <div key={r.id} className="p-4 bg-purple-50 rounded-2xl border border-purple-100">
                <div className="flex items-center justify-between mb-2">
                  <span className="font-semibold text-slate-800">{r.typeRotation || 'OTHER'}</span>
                  <span className="text-xs bg-white px-2 py-1 rounded-lg text-purple-700 font-medium">{r.hospitalLocation}</span>
                </div>
                <div className="flex items-center gap-4 text-sm text-slate-600">
                  <span><Calendar className="w-3.5 h-3.5 inline mr-1" />{new Date(r.startDate).toLocaleDateString('es-CO')}</span>
                  <span>→</span>
                  <span>{new Date(r.completionDate).toLocaleDateString('es-CO')}</span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {assignedStudents.length > 0 && (
        <div className="bg-white rounded-3xl p-6 border border-slate-100 shadow-sm">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-slate-800">Estudiantes Asignados</h2>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-slate-200">
                  <th className="text-left py-3 px-4 text-sm font-medium text-slate-600">Estudiante</th>
                  <th className="text-left py-3 px-4 text-sm font-medium text-slate-600">DNI</th>
                  <th className="text-left py-3 px-4 text-sm font-medium text-slate-600">Universidad</th>
                  <th className="text-left py-3 px-4 text-sm font-medium text-slate-600">Estado</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {assignedStudents.map(s => (
                  <tr key={s.id} className="hover:bg-slate-50 transition-colors">
                    <td className="py-3 px-4">
                      <span className="text-slate-700 font-medium">{s.fullName}</span>
                      <p className="text-xs text-slate-500">{s.email}</p>
                    </td>
                    <td className="py-3 px-4 text-slate-600">{s.dni}</td>
                    <td className="py-3 px-4 text-slate-600">{s.universityName}</td>
                    <td className="py-3 px-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(s.studentStatus)}`}>
                        {getStatusLabel(s.studentStatus)}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {showEditModal && editForm && (
        <div className="fixed inset-0 bg-clinical-900/40 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl shadow-2xl w-full max-w-2xl overflow-hidden border border-slate-100">
            <div className="max-h-[90vh] overflow-y-auto">
              <div className="flex items-center justify-between p-6 border-b border-slate-100 bg-white sticky top-0 z-10">
              <h2 className="text-xl font-bold text-slate-800">Editar Mi Perfil</h2>
              <button onClick={() => setShowEditModal(false)} className="p-2 hover:bg-slate-100 rounded-xl transition-colors">
                <X className="w-5 h-5 text-slate-500" />
              </button>
            </div>
            <form onSubmit={handleSave}>
              <div className="p-6 space-y-6">
                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Información Personal</h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Nombre</label>
                      <input type="text" className="input-field py-3" value={editForm.name}
                        onChange={(e) => setEditForm({ ...editForm, name: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Apellido</label>
                      <input type="text" className="input-field py-3" value={editForm.lastName}
                        onChange={(e) => setEditForm({ ...editForm, lastName: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">DNI</label>
                      <input type="text" className="input-field py-3" value={editForm.dni}
                        onChange={(e) => setEditForm({ ...editForm, dni: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Teléfono</label>
                      <input type="text" className="input-field py-3" value={editForm.phoneNumber}
                        onChange={(e) => setEditForm({ ...editForm, phoneNumber: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Correo Electrónico</label>
                      <input type="email" className="input-field py-3" value={editForm.email}
                        onChange={(e) => setEditForm({ ...editForm, email: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Estado Civil</label>
                      <select className="input-field py-3" value={editForm.maritalStatus}
                        onChange={(e) => setEditForm({ ...editForm, maritalStatus: e.target.value })}>
                        {maritalStatusOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Tipo de Sangre</label>
                      <select className="input-field py-3" value={editForm.typeBlood}
                        onChange={(e) => setEditForm({ ...editForm, typeBlood: e.target.value })}>
                        {typeBloodOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Peso (kg)</label>
                      <input type="number" step="0.1" className="input-field py-3" value={editForm.weight}
                        onChange={(e) => setEditForm({ ...editForm, weight: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">IMC</label>
                      <input type="number" step="0.1" className="input-field py-3" value={editForm.imc}
                        onChange={(e) => setEditForm({ ...editForm, imc: e.target.value })} />
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Especialidad</label>
                      <select className="input-field py-3" value={editForm.specialty}
                        onChange={(e) => setEditForm({ ...editForm, specialty: e.target.value })}>
                        {specialtyOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                      </select>
                    </div>
                    <div>
                      <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Universidad</label>
                      <select className="input-field py-3" value={editForm.universityId}
                        onChange={(e) => setEditForm({ ...editForm, universityId: e.target.value })}>
                        <option value="">Seleccione una universidad</option>
                        {universities.map(u => <option key={u.id} value={u.id}>{u.name}</option>)}
                      </select>
                    </div>
                  </div>
                </div>
                <div>
                  <h3 className="text-sm font-bold text-slate-700 uppercase tracking-wider mb-4 pb-2 border-b border-slate-100">Ubicación</h3>
                  <div className="mb-4">
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Lugar de Nacimiento</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input type="text" placeholder="Dirección" className="input-field py-3"
                        value={editForm.placeBirth.address}
                        onChange={(e) => setEditForm({ ...editForm, placeBirth: { ...editForm.placeBirth, address: e.target.value } })} />
                      <input type="text" placeholder="Ciudad" className="input-field py-3"
                        value={editForm.placeBirth.city}
                        onChange={(e) => setEditForm({ ...editForm, placeBirth: { ...editForm.placeBirth, city: e.target.value } })} />
                      <input type="text" placeholder="Departamento" className="input-field py-3"
                        value={editForm.placeBirth.department}
                        onChange={(e) => setEditForm({ ...editForm, placeBirth: { ...editForm.placeBirth, department: e.target.value } })} />
                    </div>
                  </div>
                  <div>
                    <label className="block text-xs font-bold text-slate-500 uppercase tracking-wider mb-2">Dirección de Residencia</label>
                    <div className="grid grid-cols-3 gap-3">
                      <input type="text" placeholder="Dirección" className="input-field py-3"
                        value={editForm.residenceAddress.address}
                        onChange={(e) => setEditForm({ ...editForm, residenceAddress: { ...editForm.residenceAddress, address: e.target.value } })} />
                      <input type="text" placeholder="Ciudad" className="input-field py-3"
                        value={editForm.residenceAddress.city}
                        onChange={(e) => setEditForm({ ...editForm, residenceAddress: { ...editForm.residenceAddress, city: e.target.value } })} />
                      <input type="text" placeholder="Departamento" className="input-field py-3"
                        value={editForm.residenceAddress.department}
                        onChange={(e) => setEditForm({ ...editForm, residenceAddress: { ...editForm.residenceAddress, department: e.target.value } })} />
                    </div>
                  </div>
                </div>
              </div>
              <div className="flex items-center justify-end gap-3 p-6 border-t border-slate-100 bg-white">
                <button type="button" onClick={() => setShowEditModal(false)} className="btn-secondary">Cancelar</button>
                <button type="submit" disabled={saving} className="btn-primary disabled:opacity-50">
                  {saving ? 'Guardando...' : 'Guardar Cambios'}
                </button>
              </div>
            </form>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

function AdminDashboard() {
  const [studentCount, setStudentCount] = useState(0)
  const [doctorCount, setDoctorCount] = useState(0)
  const [activeRotationCount, setActiveRotationCount] = useState(0)
  const [completedRotationCount, setCompletedRotationCount] = useState(0)
  const [students, setStudents] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true)
      try {
        const [studentsRes, doctorsRes, rotationsRes] = await Promise.all([
          studentService.findAll({ page: 0, size: 10 }),
          doctorService.findAll({ page: 0, size: 10 }),
          rotationService.findAll({ page: 0, size: 100 })
        ])
        setStudentCount(studentsRes.data.totalElements)
        setDoctorCount(doctorsRes.data.totalElements)
        setStudents(studentsRes.data.content)

        const allRotations = rotationsRes.data.content || []
        const active = allRotations.filter(r => !r.completionDate || new Date(r.completionDate) >= new Date()).length
        const completed = allRotations.filter(r => r.completionDate && new Date(r.completionDate) < new Date()).length
        setActiveRotationCount(active)
        setCompletedRotationCount(completed)
      } catch (err) {
        console.error('Error loading dashboard:', err)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Dashboard</h1>
          <p className="text-slate-500 mt-1">Resumen de rotaciones médicas</p>
        </div>
        <div className="flex items-center gap-2 text-sm text-slate-500 bg-white px-4 py-2 rounded-lg border border-slate-200">
          <Calendar className="w-4 h-4" />
          <span>{new Date().toLocaleDateString('es-CO', { month: 'long', year: 'numeric' })}</span>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        {[
          { title: 'Estudiantes Activos', value: loading ? '...' : studentCount, icon: Users, color: 'text-blue-600', hoverBorder: 'hover:border-blue-200', bgGrad: 'from-blue-500 to-indigo-600', bgColor: 'bg-blue-50' },
          { title: 'Médicos Supervisores', value: loading ? '...' : doctorCount, icon: Stethoscope, color: 'text-emerald-600', hoverBorder: 'hover:border-emerald-200', bgGrad: 'from-emerald-500 to-teal-600', bgColor: 'bg-emerald-50' },
          { title: 'Rotaciones Activas', value: loading ? '...' : activeRotationCount, icon: ClipboardList, color: 'text-amber-600', hoverBorder: 'hover:border-amber-200', bgGrad: 'from-amber-500 to-orange-600', bgColor: 'bg-amber-50' },
          { title: 'Completadas', value: loading ? '...' : completedRotationCount, icon: TrendingUp, color: 'text-purple-600', hoverBorder: 'hover:border-purple-200', bgGrad: 'from-purple-500 to-pink-600', bgColor: 'bg-purple-50' },
        ].map((card, index) => (
          <div key={index} className={`bg-white rounded-3xl p-6 border border-slate-100 shadow-sm hover:shadow-xl hover:-translate-y-1 transform transition-all duration-300 group overflow-hidden relative ${card.hoverBorder}`}>
            <div className="flex items-start justify-between">
              <div className={`p-3 rounded-2xl ${card.bgColor} group-hover:scale-110 transition-transform duration-300`}>
                <card.icon className={`w-6 h-6 ${card.color}`} />
              </div>
            </div>
            <div className="mt-4">
              <p className="text-3xl font-bold text-slate-800 tracking-tight group-hover:text-clinical-700 transition-colors">{card.value}</p>
              <p className="text-sm font-medium text-slate-500 mt-1">{card.title}</p>
            </div>
            <div className={`absolute bottom-0 left-0 w-full h-1 bg-gradient-to-r ${card.bgGrad} opacity-0 group-hover:opacity-100 transition-opacity duration-300`} />
          </div>
        ))}
      </div>

      <div className="card">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-lg font-semibold text-slate-800">Estudiantes Recientes</h2>
        </div>

        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-clinical-600"></div>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-slate-200">
                  <th className="text-left py-3 px-4 text-sm font-medium text-slate-600">Estudiante</th>
                  <th className="text-left py-3 px-4 text-sm font-medium text-slate-600">Universidad</th>
                  <th className="text-left py-3 px-4 text-sm font-medium text-slate-600">Programa</th>
                  <th className="text-left py-3 px-4 text-sm font-medium text-slate-600">Estado</th>
                  <th className="text-left py-3 px-4 text-sm font-medium text-slate-600">ARL Fin</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {students.map((student) => (
                  <tr key={student.id} className="hover:bg-slate-50 transition-colors">
                    <td className="py-4 px-4">
                      <div className="flex items-center gap-3">
                        <div className="w-8 h-8 bg-clinical-100 rounded-full flex items-center justify-center">
                          <span className="text-sm font-medium text-clinical-700">
                            {student.fullName?.split(' ').map(n => n[0]).join('').slice(0, 2)}
                          </span>
                        </div>
                        <div>
                          <span className="text-sm text-slate-700 font-medium">{student.fullName}</span>
                          <p className="text-xs text-slate-500">{student.email}</p>
                        </div>
                      </div>
                    </td>
                    <td className="py-4 px-4 text-sm text-slate-600">{student.universityName}</td>
                    <td className="py-4 px-4 text-sm text-slate-600">{student.academicProgram}</td>
                    <td className="py-4 px-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(student.studentStatus)}`}>
                        {getStatusLabel(student.studentStatus)}
                      </span>
                    </td>
                    <td className="py-4 px-4 text-sm text-slate-600">
                      {student.arlEndDate ? new Date(student.arlEndDate).toLocaleDateString('es-CO') : 'N/A'}
                    </td>
                  </tr>
                ))}
                {students.length === 0 && (
                  <tr>
                    <td colSpan={5} className="py-8 text-center text-slate-500">No hay estudiantes registrados</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  )
}
