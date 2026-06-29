import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import MainLayout from '../layout/MainLayout.vue'
import LoginView from '../views/LoginView.vue'
import DashboardView from '../views/DashboardView.vue'
import UsersView from '../views/admin/UsersView.vue'
import StudentsView from '../views/admin/StudentsView.vue'
import TeachersView from '../views/admin/TeachersView.vue'
import ClassesView from '../views/admin/ClassesView.vue'
import CoursesView from '../views/admin/CoursesView.vue'
import RulesView from '../views/admin/RulesView.vue'
import GradesView from '../views/admin/GradesView.vue'
import StatisticsView from '../views/admin/StatisticsView.vue'
import TeacherCoursesView from '../views/teacher/TeacherCoursesView.vue'
import TeacherGradesView from '../views/teacher/TeacherGradesView.vue'
import TeacherStatisticsView from '../views/teacher/TeacherStatisticsView.vue'
import StudentProfileView from '../views/student/StudentProfileView.vue'
import StudentGradesView from '../views/student/StudentGradesView.vue'
import StudentSummaryView from '../views/student/StudentSummaryView.vue'

export const menuRoutes = [
  { path: '/dashboard', name: 'Dashboard', component: DashboardView, meta: { title: '控制台', roles: ['ADMIN', 'TEACHER', 'STUDENT'] } },
  { path: '/admin/users', component: UsersView, meta: { title: '用户管理', roles: ['ADMIN'] } },
  { path: '/admin/students', component: StudentsView, meta: { title: '学生管理', roles: ['ADMIN'] } },
  { path: '/admin/teachers', component: TeachersView, meta: { title: '教师管理', roles: ['ADMIN'] } },
  { path: '/admin/classes', component: ClassesView, meta: { title: '班级管理', roles: ['ADMIN'] } },
  { path: '/admin/courses', component: CoursesView, meta: { title: '课程管理', roles: ['ADMIN'] } },
  { path: '/admin/rules', component: RulesView, meta: { title: '成绩规则', roles: ['ADMIN'] } },
  { path: '/admin/grades', component: GradesView, meta: { title: '成绩管理', roles: ['ADMIN'] } },
  { path: '/admin/statistics', component: StatisticsView, meta: { title: '统计分析', roles: ['ADMIN'] } },
  { path: '/teacher/courses', component: TeacherCoursesView, meta: { title: '我的课程', roles: ['TEACHER'] } },
  { path: '/teacher/rules', component: RulesView, meta: { title: '成绩规则', roles: ['TEACHER'] } },
  { path: '/teacher/grades', component: TeacherGradesView, meta: { title: '成绩录入', roles: ['TEACHER'] } },
  { path: '/teacher/statistics', component: TeacherStatisticsView, meta: { title: '课程统计', roles: ['TEACHER'] } },
  { path: '/student/profile', component: StudentProfileView, meta: { title: '个人信息', roles: ['STUDENT'] } },
  { path: '/student/grades', component: StudentGradesView, meta: { title: '我的成绩', roles: ['STUDENT'] } },
  { path: '/student/summary', component: StudentSummaryView, meta: { title: '成绩汇总', roles: ['STUDENT'] } }
]

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', component: LoginView },
    { path: '/', redirect: '/dashboard' },
    { path: '/', component: MainLayout, children: menuRoutes }
  ]
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.path !== '/login' && !auth.isLoggedIn) return '/login'
  const roles = to.meta?.roles
  if (roles && !roles.includes(auth.role)) return '/dashboard'
  if (to.path === '/login' && auth.isLoggedIn) return '/dashboard'
  return true
})

export default router
