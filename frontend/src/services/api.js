import api from '../utils/api'

export const studentService = {
  findAll: (pageable) => api.get('/student/find/all', { params: pageable }),
  findById: (id) => api.get(`/student/find/${id}`),
  findByDni: (dni) => api.get(`/student/find/dni/${dni}`),
  findSelf: () => api.get('/student/find/self'),
  create: (dto) => api.post('/student/create', dto),
  update: (id, dto) => api.put(`/student/update/${id}`, dto),
  updateSelf: (dto) => api.put('/student/update/self', dto),
  delete: (id) => api.delete(`/student/delete/${id}`)
}

export const doctorService = {
  findAll: (pageable) => api.get('/Doctor/find/all', { params: pageable }),
  findById: (id) => api.get(`/Doctor/find/${id}`),
  findSelf: () => api.get('/Doctor/find/self'),
  create: (dto) => api.post('/Doctor/create', dto),
  update: (id, dto) => api.put(`/Doctor/update/${id}`, dto),
  updateSelf: (dto) => api.put('/Doctor/update/self', dto),
  delete: (id) => api.delete(`/Doctor/delete/${id}`)
}

export const adminService = {
  findAll: (pageable) => api.get('/admin/find/all', { params: pageable }),
  findById: (id) => api.get(`/admin/find/${id}`),
  findSelf: () => api.get('/admin/find/self'),
  create: (dto) => api.post('/admin/create', dto),
  update: (id, dto) => api.put(`/admin/update/${id}`, dto),
  delete: (id) => api.delete(`/admin/delete/${id}`)
}

export const rotationService = {
  findAll: (pageable) => api.get('/rotation/find/all', { params: pageable }),
  findById: (id) => api.get(`/rotation/find/${id}`),
  findSelf: (pageable) => api.get('/rotation/find/self', { params: pageable }),
  create: (dto, doctorId) => api.post(`/rotation/create/${doctorId}`, dto),
  createSelf: (dto) => api.post('/rotation/create/self', dto),
  update: (id, dto) => api.put(`/rotation/update/${id}`, dto),
  delete: (id) => api.delete(`/rotation/delete/${id}`)
}

export const groupService = {
  findAll: (pageable) => api.get('/Group/find/all', { params: pageable }),
  findById: (id) => api.get(`/Group/find/${id}`),
  create: (dto) => api.post('/Group/create', dto),
  update: (id, dto) => api.put(`/Group/update/${id}`, dto),
  delete: (id) => api.delete(`/Group/delete/${id}`)
}

export const groupAssignmentService = {
  findAll: (pageable) => api.get('/group/assignment/find/all', { params: pageable }),
  findSelfDetailed: (pageable) => api.get('/group/assignment/find/self/detailed', { params: pageable }),
  findAllDetailed: (pageable) => api.get('/group/assignment/find/all/detailed', { params: pageable }),
  create: (dto) => api.post('/group/assignment/create', dto),
  delete: (id) => api.delete(`/group/assignment/delete/${id}`)
}

export const universityService = {
  findAll: (pageable = { page: 0, size: 100 }) => api.get('/university/find/all', { params: pageable }),
  findById: (id) => api.get(`/university/find/${id}`),
  create: (dto) => api.post('/university/create', dto),
  update: (id, dto) => api.put(`/university/update/${id}`, dto),
  delete: (id) => api.delete(`/university/delete/${id}`),
  softDelete: (id) => api.delete(`/university/soft/delete/${id}`),
  restore: (id) => api.put(`/university/restore/${id}`)
}

export const entryPracticeService = {
  findAll: (pageable) => api.get('/entry/practice/find/all', { params: pageable }),
  findById: (id) => api.get(`/entry/practice/find/${id}`),
  findSelf: (pageable) => api.get('/entry/practice/find/self', { params: pageable }),
  create: (dto) => api.post('/entry/practice/create', dto),
  update: (id, dto) => api.put(`/entry/practice/update/${id}`, dto),
  delete: (id) => api.delete(`/entry/practice/delete/${id}`)
}

export const entryService = {
  findAll: (pageable) => api.get('/entry/find/all', { params: pageable }),
  findById: (id) => api.get(`/entry/find/${id}`),
  create: (dto) => api.post('/entry/create', dto),
  saveByQr: (qrCode, dto) => api.post(`/entry/qr/${qrCode}`, dto),
  update: (id, dto) => api.put(`/entry/update/${id}`, dto),
  delete: (id) => api.delete(`/entry/delete/${id}`)
}

export const fileService = {
  findAll: (pageable) => api.get('/api/v1/files', { params: pageable }),
  findById: (id) => api.get(`/api/v1/files/${id}`),
  upload: (personId, formData) => api.post(`/api/v1/files/upload/${personId}`, formData, {
    headers: { 'Content-Type': undefined }
  }),
  updateMetadata: (id, dto) => api.patch(`/api/v1/files/${id}/metadata`, dto),
  replace: (id, formData) => api.put(`/api/v1/files/${id}/replace`, formData, {
    headers: { 'Content-Type': undefined }
  }),
  delete: (id) => api.delete(`/api/v1/files/${id}`)
}

export const attendantService = {
  findAll: (pageable) => api.get('/api/v1/attendants', { params: pageable }),
  findById: (id) => api.get(`/api/v1/attendants/${id}`),
  create: (dto) => api.post('/api/v1/attendants', dto),
  update: (id, dto) => api.put(`/api/v1/attendants/${id}`, dto),
  delete: (id) => api.delete(`/api/v1/attendants/${id}`)
}

export const diseaseService = {
  findAll: (pageable) => api.get('/api/v1/diseases', { params: pageable }),
  findById: (id) => api.get(`/api/v1/diseases/${id}`),
  create: (dto) => api.post('/api/v1/diseases', dto),
  update: (id, dto) => api.put(`/api/v1/diseases/${id}`, dto),
  delete: (id) => api.delete(`/api/v1/diseases/${id}`)
}

export const studentDiseaseService = {
  findAll: (pageable) => api.get('/api/v1/student-diseases', { params: pageable }),
  findById: (id) => api.get(`/api/v1/student-diseases/${id}`),
  create: (dto) => api.post('/api/v1/student-diseases', dto),
  update: (id, dto) => api.put(`/api/v1/student-diseases/${id}`, dto),
  delete: (id) => api.delete(`/api/v1/student-diseases/${id}`)
}

export const investigationService = {
  findAll: (pageable) => api.get('/api/v1/investigations', { params: pageable }),
  findById: (id) => api.get(`/api/v1/investigations/${id}`),
  create: (dto) => api.post('/api/v1/investigations', dto),
  update: (id, dto) => api.put(`/api/v1/investigations/${id}`, dto),
  delete: (id) => api.delete(`/api/v1/investigations/${id}`)
}

export const medicineService = {
  findAll: (pageable) => api.get('/api/v1/medicines', { params: pageable }),
  create: (dto) => api.post('/api/v1/medicines', dto),
  delete: (id) => api.delete(`/api/v1/medicines/${id}`)
}

export const medicalTreatmentService = {
  findAll: (pageable) => api.get('/api/v1/medical-treatments', { params: pageable }),
  create: (dto) => api.post('/api/v1/medical-treatments', dto),
  delete: (id) => api.delete(`/api/v1/medical-treatments/${id}`)
}

export const cieService = {
  search: (term) => api.get(`/api/test/search/${term}`),
  searchSpecific: (dto) => api.post('/api/test/search/especific', dto)
}
