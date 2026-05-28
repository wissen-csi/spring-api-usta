import { NavLink } from 'react-router-dom'
import { 
  LayoutDashboard, 
  Users, 
  Stethoscope, 
  ClipboardList,
  UserCog,
  Building2,
  QrCode,
  Camera,
  FileText,
  Activity,
  Pill,
  X
} from 'lucide-react'
import { useAuth } from '../../context/AuthContext'

export default function Sidebar({ isOpen, setIsOpen }) {
  const { user } = useAuth()
  const isAdmin = user?.role?.includes('ADMIN')
  const isStudent = user?.role?.includes('STUDENT')
  const isDoctor = user?.role?.includes('DOCTOR')

  const navItems = [
    { path: '/', icon: LayoutDashboard, label: isStudent ? 'Mi Perfil' : 'Dashboard' },
    ...(isAdmin ? [
      { path: '/students', icon: Users, label: 'Estudiantes' },
      { path: '/doctors', icon: Stethoscope, label: 'Médicos' },
    ] : []),
    ...(isAdmin || isDoctor ? [
      { path: '/tasks', icon: ClipboardList, label: 'Rotaciones' },
    ] : []),
    ...(isDoctor ? [
      { path: '/groups', icon: Users, label: 'Grupos' },
    ] : []),
    ...(isAdmin ? [
      { path: '/universities', icon: Building2, label: 'Universidades' },
    ] : []),
    ...(!isStudent ? [
      { path: '/practices', icon: QrCode, label: 'Ingreso' },
    ] : []),
    ...(isStudent ? [
      { path: '/attendance', icon: Camera, label: 'Asistencia' },
    ] : []),
    ...(isStudent ? [
      { path: '/files', icon: FileText, label: 'Archivos' },
    ] : []),
    ...(isStudent ? [
      { path: '/health', icon: Activity, label: 'Investigaciones' },
    ] : []),
    ...(isStudent ? [
      { path: '/treatments', icon: Pill, label: 'Tratamientos' },
    ] : []),
    ...(isAdmin ? [{ path: '/admin', icon: UserCog, label: 'Administración' }] : []),
  ]
  return (
    <>
      {isOpen && (
        <div 
          className="fixed inset-0 bg-clinical-900/50 z-40 lg:hidden"
          onClick={() => setIsOpen(false)}
        />
      )}
      
      <aside className={`fixed top-0 left-0 h-full bg-clinical-800 border-r border-clinical-700 z-50 
                        transition-all duration-300 flex flex-col
                        ${isOpen ? 'w-64' : 'w-20'}`}>
        <div className="h-16 flex items-center justify-between px-4 border-b border-clinical-600">
          {isOpen ? (
            <img src="/api/logo" alt="Logo" className="h-10" />
          ) : (
            <img src="/api/logo" alt="Logo" className="h-8 mx-auto" />
          )}
          <button 
            onClick={() => setIsOpen(false)}
            className="p-2 rounded-lg hover:bg-clinical-700 lg:hidden"
          >
            <X className="w-5 h-5 text-clinical-200" />
          </button>
        </div>

        <nav className="flex-1 p-4 space-y-2">
          {navItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              className={({ isActive }) =>
                `flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200
                ${isActive 
                  ? 'bg-white text-clinical-800 font-medium' 
                  : 'text-clinical-200 hover:bg-clinical-700 hover:text-white'
                }`
              }
            >
              <item.icon className={`w-5 h-5 flex-shrink-0 ${!isOpen && 'mx-auto'}`} />
              {isOpen && <span>{item.label}</span>}
            </NavLink>
          ))}
        </nav>


      </aside>
    </>
  )
}