import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import Layout from './components/layout/Layout'
import Login from './pages/Login'
import Dashboard from './pages/Dashboard'
import Students from './pages/Students'
import Doctors from './pages/Doctors'
import Tasks from './pages/Tasks'
import Admin from './pages/Admin'
import Universities from './pages/Universities'
import EntryPractices from './pages/EntryPractices'
import Attendance from './pages/Attendance'
import Files from './pages/Files'
import Health from './pages/Health'
import MedicalTreatment from './pages/MedicalTreatment'
import Groups from './pages/Groups'

function ProtectedRoute({ children, allowedRoles }) {
  const { user, loading } = useAuth()
  if (loading) return <div className="min-h-screen flex items-center justify-center">Cargando...</div>
  if (!user) return <Navigate to="/login" replace />
  if (allowedRoles && !allowedRoles.some(r => user.role?.includes(r))) {
    return <Navigate to="/" replace />
  }
  return children
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<ProtectedRoute><Layout /></ProtectedRoute>}>
            <Route index element={<Dashboard />} />
            <Route path="students" element={<ProtectedRoute allowedRoles={['ADMIN','DOCTOR']}><Students /></ProtectedRoute>} />
            <Route path="doctors" element={<ProtectedRoute allowedRoles={['ADMIN','DOCTOR']}><Doctors /></ProtectedRoute>} />
            <Route path="tasks" element={<ProtectedRoute allowedRoles={['ADMIN','DOCTOR']}><Tasks /></ProtectedRoute>} />
            <Route path="admin" element={<ProtectedRoute allowedRoles={['ADMIN']}><Admin /></ProtectedRoute>} />
            <Route path="universities" element={<ProtectedRoute allowedRoles={['ADMIN']}><Universities /></ProtectedRoute>} />
            <Route path="groups" element={<ProtectedRoute allowedRoles={['DOCTOR']}><Groups /></ProtectedRoute>} />
            <Route path="practices" element={<ProtectedRoute allowedRoles={['ADMIN','DOCTOR']}><EntryPractices /></ProtectedRoute>} />
            <Route path="attendance" element={<ProtectedRoute allowedRoles={['STUDENT']}><Attendance /></ProtectedRoute>} />
            <Route path="files" element={<ProtectedRoute allowedRoles={['STUDENT']}><Files /></ProtectedRoute>} />
            <Route path="health" element={<ProtectedRoute allowedRoles={['STUDENT']}><Health /></ProtectedRoute>} />
            <Route path="treatments" element={<ProtectedRoute allowedRoles={['DOCTOR','STUDENT']}><MedicalTreatment /></ProtectedRoute>} />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App
