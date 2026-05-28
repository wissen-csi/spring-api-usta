import { Menu, LogOut } from 'lucide-react'
import { useAuth } from '../../context/AuthContext'

const roleLabels = {
  ADMIN: 'Administrador',
  STUDENT: 'Estudiante',
  DOCTOR: 'Doctor',
  PORTER: 'Portero',
}

export default function Header({ onMenuClick }) {
  const { user, logout } = useAuth()

  return (
    <header className="h-16 bg-white border-b border-clinical-200 flex items-center justify-between px-6 sticky top-0 z-30">
      <div className="flex items-center gap-4">
        <button 
          onClick={onMenuClick}
          className="p-2 rounded-lg hover:bg-clinical-100 transition-colors lg:hidden"
        >
          <Menu className="w-5 h-5 text-clinical-600" />
        </button>
        
      </div>

      <div className="flex items-center gap-3">
        <div className="flex items-center gap-3 pl-3 border-l border-clinical-200">
          <div className="w-9 h-9 bg-clinical-600 rounded-full flex items-center justify-center">
            <span className="text-white font-medium text-sm">
              {user?.name ? user.name.split(' ').map(w => w[0]).join('').slice(0, 2).toUpperCase() : user?.dni?.slice(0, 2).toUpperCase()}
            </span>
          </div>
          <div className="hidden sm:block">
            <p className="text-sm font-medium text-slate-800">{user?.name}</p>
            <p className="text-xs text-slate-500">{roleLabels[user?.role] || user?.role}</p>
          </div>
          <button onClick={logout} className="p-2 rounded-lg hover:bg-red-50 transition-colors" title="Cerrar sesión">
            <LogOut className="w-4 h-4 text-slate-500 hover:text-red-600" />
          </button>
        </div>
      </div>
    </header>
  )
}
