import { useState, useEffect } from 'react'
import { Users, Stethoscope, UserCog, ClipboardList, BarChart3 } from 'lucide-react'
import { studentService, doctorService, adminService, rotationService } from '../services/api'
import { useAuth } from '../context/AuthContext'

export default function Admin() {
  const { user } = useAuth()
  const [stats, setStats] = useState({
    students: 0,
    doctors: 0,
    admins: 0,
    rotations: 0
  })
  const [loading, setLoading] = useState(true)

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

  const total = stats.students + stats.doctors + stats.admins

  return (
    <div className="space-y-6">
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

      <div className="bg-gradient-to-r from-slate-800 to-slate-900 rounded-3xl p-6 text-white shadow-xl">
        <div className="flex items-center gap-3 mb-4">
          <BarChart3 className="w-6 h-6" />
          <h2 className="text-lg font-semibold">Resumen Total de Personas</h2>
        </div>
        <p className="text-5xl font-bold tracking-tight">
          {loading ? '...' : total}
        </p>
        <p className="text-slate-300 mt-2">personas registradas en el sistema</p>
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

      {loading && (
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-clinical-600"></div>
        </div>
      )}
    </div>
  )
}
