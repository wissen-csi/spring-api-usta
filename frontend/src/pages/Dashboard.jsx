import { useState, useEffect } from 'react'
import { Users, Stethoscope, ClipboardList, TrendingUp, Calendar, Clock, BookOpen, MapPin, User } from 'lucide-react'
import { studentService, doctorService, rotationService, groupAssignmentService } from '../services/api'
import { useAuth } from '../context/AuthContext'

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

  return isStudent ? <StudentDashboard /> : <AdminDashboard />
}

function StudentDashboard() {
  const [profile, setProfile] = useState(null)
  const [groupAssignments, setGroupAssignments] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true)
      try {
        const [profileRes, groupRes] = await Promise.all([
          studentService.findSelf(),
          groupAssignmentService.findSelfDetailed({ page: 0, size: 10 })
        ])
        setProfile(profileRes.data)
        setGroupAssignments(groupRes.data?.content || [])
      } catch (err) {
        console.error('Error loading student dashboard:', err)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  if (loading) {
    return (
      <div className="flex justify-center py-24">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-clinical-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-800">Mi Perfil</h1>
        <p className="text-slate-500 mt-1">Información personal y grupo de rotación</p>
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
