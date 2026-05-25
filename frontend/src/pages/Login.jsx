import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { Lock, UserCircle } from 'lucide-react'

export default function Login() {
  const [dni, setDni] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await login(dni, password)
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.message || 'Credenciales inválidas')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-tr from-slate-50 via-clinical-50/30 to-indigo-50/40 flex items-center justify-center p-4 relative overflow-hidden">
      {/* Decorative Blur Spheres */}
      <div className="absolute top-1/4 left-1/4 w-72 h-72 bg-clinical-200/20 rounded-full blur-3xl -z-10" />
      <div className="absolute bottom-1/4 right-1/4 w-80 h-80 bg-indigo-200/20 rounded-full blur-3xl -z-10" />

      <div className="bg-white/90 backdrop-blur-md rounded-3xl border border-slate-100 shadow-2xl w-full max-w-md p-8 transform hover:scale-[1.01] transition-all duration-300 relative overflow-hidden">
        <div className="absolute top-0 left-0 w-full h-2 bg-gradient-to-r from-clinical-500 to-indigo-600" />
        
        <div className="text-center mb-8">
          <img src="/api/logo" alt="Logo" className="h-20 mx-auto mb-4" />
          <h1 className="text-lg font-bold text-slate-800 tracking-tight leading-tight">Hospital Universitario<br />San Rafael de Tunja</h1>
          <p className="text-xs font-bold text-clinical-600 uppercase tracking-widest mt-1">Rotación</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5">
          {error && (
            <div className="bg-red-50/80 backdrop-blur-sm border border-red-200/50 text-red-700 px-4 py-3 rounded-2xl text-xs font-semibold">
              {error}
            </div>
          )}

          <div>
            <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-2">DNI de Acceso</label>
            <div className="relative">
              <UserCircle className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
              <input
                type="text"
                value={dni}
                onChange={(e) => setDni(e.target.value)}
                className="w-full pl-11 pr-4 py-3 bg-slate-50/50 border border-slate-200 rounded-2xl focus:bg-white focus:ring-2 focus:ring-clinical-500 focus:border-transparent outline-none transition-all duration-200 text-sm placeholder:text-slate-400 font-medium text-slate-700"
                placeholder="Ingrese su número de DNI"
                required
              />
            </div>
          </div>

          <div>
            <label className="block text-[10px] font-bold text-slate-500 uppercase tracking-wider mb-2">Contraseña</label>
            <div className="relative">
              <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-slate-400" />
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full pl-11 pr-4 py-3 bg-slate-50/50 border border-slate-200 rounded-2xl focus:bg-white focus:ring-2 focus:ring-clinical-500 focus:border-transparent outline-none transition-all duration-200 text-sm placeholder:text-slate-400 font-medium text-slate-700"
                placeholder="Ingrese su contraseña segura"
                required
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-gradient-to-r from-clinical-600 to-clinical-700 hover:from-clinical-700 hover:to-clinical-800 disabled:from-slate-400 disabled:to-slate-500 text-white font-semibold py-3.5 rounded-2xl shadow-xl shadow-clinical-600/10 hover:shadow-clinical-600/25 transition-all duration-300 text-sm mt-2"
          >
            {loading ? 'Validando Credenciales...' : 'Iniciar Sesión'}
          </button>
        </form>
      </div>
    </div>
  )
}
