import { useState, useEffect } from 'react'
import { Search, Plus, Trash2, Users as UsersIcon, AlertTriangle } from 'lucide-react'
import { groupService, groupAssignmentService, studentService } from '../services/api'

export default function Groups() {
  const [groups, setGroups] = useState([])
  const [assignments, setAssignments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [searchDni, setSearchDni] = useState('')
  const [selectedGroup, setSelectedGroup] = useState('')
  const [foundStudent, setFoundStudent] = useState(null)
  const [studentSearching, setStudentSearching] = useState(false)
  const [adding, setAdding] = useState(false)
  const [toast, setToast] = useState({ show: false, message: '', type: 'success' })
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [deleting, setDeleting] = useState(false)

  useEffect(() => {
    loadData()
  }, [])

  const loadData = async () => {
    setLoading(true)
    setError('')
    try {
      const [groupRes, assignRes] = await Promise.all([
        groupService.findAll({ page: 0, size: 100 }),
        groupAssignmentService.findAllDetailed({ page: 0, size: 1000 })
      ])
      setGroups(groupRes.data.content || [])
      setAssignments(assignRes.data.content || [])
      if (!selectedGroup && groupRes.data.content.length > 0) {
        setSelectedGroup(groupRes.data.content[0].id)
      }
    } catch (err) {
      setError('Error al cargar datos')
    } finally {
      setLoading(false)
    }
  }

  const searchStudent = async () => {
    if (!searchDni.trim()) return
    setStudentSearching(true)
    setFoundStudent(null)
    try {
      const res = await studentService.findByDni(searchDni.trim())
      const alreadyAssigned = assignments.some(
        a => a.studentDni === searchDni.trim() && a.groupId === selectedGroup
      )
      if (alreadyAssigned) {
        setToast({ show: true, message: 'El estudiante ya está en este grupo', type: 'error' })
        setTimeout(() => setToast({ show: false, message: '' }), 3000)
        setFoundStudent(null)
      } else {
        setFoundStudent(res.data)
      }
    } catch {
      setToast({ show: true, message: 'Estudiante no encontrado', type: 'error' })
      setTimeout(() => setToast({ show: false, message: '' }), 3000)
    } finally {
      setStudentSearching(false)
    }
  }

  const addStudent = async () => {
    if (!foundStudent || !selectedGroup) return
    setAdding(true)
    try {
      await groupAssignmentService.create({
        idStudent: foundStudent.id,
        idGroup: selectedGroup
      })
      setToast({ show: true, message: 'Estudiante agregado al grupo', type: 'success' })
      setSearchDni('')
      setFoundStudent(null)
      await loadData()
    } catch {
      setToast({ show: true, message: 'Error al agregar estudiante', type: 'error' })
    } finally {
      setAdding(false)
      setTimeout(() => setToast({ show: false, message: '' }), 3000)
    }
  }

  const confirmDelete = (assignment) => {
    setDeleteTarget(assignment)
    setShowDeleteConfirm(true)
  }

  const deleteAssignment = async () => {
    if (!deleteTarget) return
    setDeleting(true)
    try {
      await groupAssignmentService.delete(deleteTarget.assignmentId)
      setToast({ show: true, message: 'Estudiante eliminado del grupo', type: 'success' })
      await loadData()
    } catch {
      setToast({ show: true, message: 'Error al eliminar estudiante', type: 'error' })
    } finally {
      setDeleting(false)
      setShowDeleteConfirm(false)
      setDeleteTarget(null)
      setTimeout(() => setToast({ show: false, message: '' }), 3000)
    }
  }

  const getGroupAssignments = (groupId) =>
    assignments.filter(a => a.groupId === groupId)

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-clinical-600" />
      </div>
    )
  }

  if (error) {
    return (
      <div className="p-6">
        <div className="bg-red-50 text-red-600 p-4 rounded-xl">{error}</div>
      </div>
    )
  }

  return (
    <div className="p-6 max-w-6xl mx-auto space-y-6">
      <div className="flex items-center gap-3">
        <div className="w-10 h-10 bg-clinical-100 rounded-xl flex items-center justify-center">
          <UsersIcon className="w-5 h-5 text-clinical-600" />
        </div>
        <div>
          <h1 className="text-2xl font-bold text-slate-800">Grupos</h1>
          <p className="text-sm text-slate-500">Gestiona los estudiantes en cada grupo</p>
        </div>
      </div>

      {toast.show && (
        <div className={`fixed top-4 right-4 z-50 px-6 py-3 rounded-xl shadow-lg text-white text-sm font-medium ${
          toast.type === 'success' ? 'bg-green-500' : 'bg-red-500'
        }`}>
          {toast.message}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        <div className="lg:col-span-1">
          <div className="bg-white rounded-xl shadow-sm border border-slate-200 p-4">
            <h2 className="text-sm font-semibold text-slate-700 mb-3">Grupos</h2>
            {groups.length === 0 ? (
              <p className="text-sm text-slate-400">No hay grupos</p>
            ) : (
              <div className="space-y-2">
                {groups.map(group => {
                  const count = getGroupAssignments(group.id).length
                  return (
                    <button
                      key={group.id}
                      onClick={() => setSelectedGroup(group.id)}
                      className={`w-full text-left px-3 py-2 rounded-lg text-sm transition-colors ${
                        selectedGroup === group.id
                          ? 'bg-clinical-50 text-clinical-700 font-medium'
                          : 'text-slate-600 hover:bg-slate-50'
                      }`}
                    >
                      <span>{group.name}</span>
                      <span className="text-xs text-slate-400 ml-2">({count}/{group.capacity})</span>
                    </button>
                  )
                })}
              </div>
            )}
          </div>
        </div>

        <div className="lg:col-span-3">
          {selectedGroup ? (
            <div className="space-y-4">
              <div className="bg-white rounded-xl shadow-sm border border-slate-200 p-4">
                <h2 className="text-sm font-semibold text-slate-700 mb-3">Agregar Estudiante</h2>
                <div className="flex gap-2">
                  <div className="flex-1 relative">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
                    <input
                      type="text"
                      value={searchDni}
                      onChange={(e) => { setSearchDni(e.target.value); setFoundStudent(null) }}
                      onKeyDown={(e) => e.key === 'Enter' && searchStudent()}
                      placeholder="Buscar por DNI..."
                      className="w-full pl-9 pr-3 py-2 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-clinical-500"
                    />
                  </div>
                  <button
                    onClick={searchStudent}
                    disabled={!searchDni.trim() || studentSearching}
                    className="px-4 py-2 bg-clinical-600 text-white rounded-lg text-sm font-medium hover:bg-clinical-700 disabled:opacity-50"
                  >
                    {studentSearching ? '...' : 'Buscar'}
                  </button>
                </div>
                {foundStudent && (
                  <div className="mt-3 flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                    <div>
                      <p className="text-sm font-medium text-slate-700">{foundStudent.fullName}</p>
                      <p className="text-xs text-slate-500">DNI: {foundStudent.dni}</p>
                    </div>
                    <button
                      onClick={addStudent}
                      disabled={adding}
                      className="flex items-center gap-1 px-3 py-1.5 bg-green-600 text-white rounded-lg text-sm font-medium hover:bg-green-700 disabled:opacity-50"
                    >
                      <Plus className="w-4 h-4" />
                      {adding ? '...' : 'Agregar'}
                    </button>
                  </div>
                )}
              </div>

              <div className="bg-white rounded-xl shadow-sm border border-slate-200 overflow-hidden">
                <div className="p-4 border-b border-slate-100">
                  <h2 className="text-sm font-semibold text-slate-700">
                    {groups.find(g => g.id === selectedGroup)?.name} - Estudiantes
                  </h2>
                </div>
                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead>
                      <tr className="bg-slate-50">
                        <th className="text-left px-4 py-3 text-xs font-medium text-slate-500 uppercase">Estudiante</th>
                        <th className="text-left px-4 py-3 text-xs font-medium text-slate-500 uppercase">DNI</th>
                        <th className="text-left px-4 py-3 text-xs font-medium text-slate-500 uppercase">Rotación</th>
                        <th className="text-right px-4 py-3 text-xs font-medium text-slate-500 uppercase">Acción</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100">
                      {getGroupAssignments(selectedGroup).length === 0 ? (
                        <tr>
                          <td colSpan={4} className="px-4 py-8 text-center text-sm text-slate-400">
                            No hay estudiantes en este grupo
                          </td>
                        </tr>
                      ) : (
                        getGroupAssignments(selectedGroup).map(a => (
                          <tr key={a.assignmentId} className="hover:bg-slate-50">
                            <td className="px-4 py-3 text-sm text-slate-700">{a.studentName}</td>
                            <td className="px-4 py-3 text-sm text-slate-500">{a.studentDni}</td>
                            <td className="px-4 py-3 text-sm text-slate-500">{a.rotationType}</td>
                            <td className="px-4 py-3 text-right">
                              <button
                                onClick={() => confirmDelete(a)}
                                className="p-1.5 text-red-500 hover:bg-red-50 rounded-lg transition-colors"
                              >
                                <Trash2 className="w-4 h-4" />
                              </button>
                            </td>
                          </tr>
                        ))
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          ) : (
            <div className="bg-white rounded-xl shadow-sm border border-slate-200 p-8 text-center">
              <UsersIcon className="w-12 h-12 text-slate-300 mx-auto mb-3" />
              <p className="text-slate-500">Selecciona un grupo para gestionar sus estudiantes</p>
            </div>
          )}
        </div>
      </div>

      {showDeleteConfirm && (
        <div className="fixed inset-0 bg-slate-900/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl max-w-md w-full p-6">
            <div className="flex items-center gap-3 mb-4">
              <div className="w-10 h-10 bg-red-100 rounded-full flex items-center justify-center">
                <AlertTriangle className="w-5 h-5 text-red-600" />
              </div>
              <div>
                <h3 className="text-lg font-semibold text-slate-800">Eliminar Estudiante</h3>
                <p className="text-sm text-slate-500">
                  ¿Eliminar a {deleteTarget?.studentName} del grupo?
                </p>
              </div>
            </div>
            <div className="flex justify-end gap-3">
              <button
                onClick={() => { setShowDeleteConfirm(false); setDeleteTarget(null) }}
                className="px-4 py-2 text-sm font-medium text-slate-600 hover:bg-slate-100 rounded-lg"
              >
                Cancelar
              </button>
              <button
                onClick={deleteAssignment}
                disabled={deleting}
                className="px-4 py-2 text-sm font-medium text-white bg-red-600 hover:bg-red-700 rounded-lg disabled:opacity-50"
              >
                {deleting ? '...' : 'Eliminar'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}