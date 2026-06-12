import request from '../utils/request'

export const authApi = {
  login: (data) => request.post('/auth/login', data),
  me: () => request.get('/auth/me')
}

export const crudApi = (resource) => ({
  list: () => request.get(`/${resource}`),
  create: (data) => request.post(`/${resource}`, data),
  update: (id, data) => request.put(`/${resource}/${id}`, data),
  remove: (id) => request.delete(`/${resource}/${id}`)
})

export const ruleApi = {
  list: (courseId) => request.get(`/courses/${courseId}/grade-rules`),
  save: (courseId, data) => request.post(`/courses/${courseId}/grade-rules`, data)
}

export const gradeApi = crudApi('grades')

export const statisticsApi = {
  overview: () => request.get('/statistics/overview'),
  course: (courseId) => request.get(`/statistics/courses/${courseId}`),
  studentSummary: () => request.get('/statistics/student-summary')
}
