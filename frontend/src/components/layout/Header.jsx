import { Menu, Bell, Search, LogOut } from 'lucide-react'
import { useAuth } from '../../context/AuthContext'

export default function Header({ onMenuClick }) {
  const { user, logout } = useAuth()

  return (
    <header className="h-16 bg-white border-b border-slate-200 flex items-center justify-between px-6 sticky top-0 z-30">
      <div className="flex items-center gap-4">
        <button 
          onClick={onMenuClick}
          className="p-2 rounded-lg hover:bg-slate-100 transition-colors lg:hidden"
        >
          <Menu className="w-5 h-5 text-slate-600" />
        </button>
        
        <div className="relative hidden md:block">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
          <input 
            type="text" 
            placeholder="Buscar..."
            className="pl-10 pr-4 py-2 bg-slate-100 border-0 rounded-lg text-sm
                       focus:outline-none focus:ring-2 focus:ring-clinical-500 w-64"
          />
        </div>
      </div>

      <div className="flex items-center gap-3">
        <button className="relative p-2 rounded-lg hover:bg-slate-100 transition-colors">
          <Bell className="w-5 h-5 text-slate-600" />
          <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full"></span>
        </button>
        
        <div className="flex items-center gap-3 pl-3 border-l border-slate-200">
          <div className="w-9 h-9 bg-clinical-600 rounded-full flex items-center justify-center">
            <span className="text-white font-medium text-sm">{user?.dni?.slice(0, 2).toUpperCase()}</span>
          </div>
          <div className="hidden sm:block">
            <p className="text-sm font-medium text-slate-800">{user?.dni}</p>
            <p className="text-xs text-slate-500">{user?.role}</p>
          </div>
          <button onClick={logout} className="p-2 rounded-lg hover:bg-red-50 transition-colors" title="Cerrar sesión">
            <LogOut className="w-4 h-4 text-slate-500 hover:text-red-600" />
          </button>
        </div>
      </div>
    </header>
  )
}
