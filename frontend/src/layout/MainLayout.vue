<template>
  <el-container class="app-shell">
    <el-aside width="232px" class="sidebar">
      <div class="brand">高校成绩管理系统</div>
      <el-menu router :default-active="$route.path" background-color="#243247" text-color="#dbe5f3" active-text-color="#fff">
        <el-menu-item v-for="item in menus" :key="item.path" :index="item.path">
          <component :is="item.icon" class="menu-icon" />
          <span>{{ item.meta.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="topbar">
        <div>
          <strong>{{ $route.meta.title || '控制台' }}</strong>
          <span class="role-tag">{{ roleName }}</span>
        </div>
        <div class="user-area">
          <span>{{ auth.user?.realName || auth.user?.username }}</span>
          <el-button size="small" @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { BookOpen, ChartColumn, GraduationCap, LayoutDashboard, ListChecks, School, ScrollText, UserCog, Users } from 'lucide-vue-next'
import { menuRoutes } from '../router'
import { useAuthStore } from '../stores/auth'

const auth = useAuthStore()
const router = useRouter()

const icons = [LayoutDashboard, UserCog, GraduationCap, Users, School, BookOpen, ListChecks, ScrollText, ChartColumn]
const menus = computed(() => menuRoutes
  .filter((route) => route.meta.roles.includes(auth.role))
  .map((route, index) => ({ ...route, icon: icons[index % icons.length] })))

const roleName = computed(() => ({ ADMIN: '管理员', TEACHER: '教师', STUDENT: '学生' }[auth.role] || ''))

function logout() {
  auth.logout()
  router.push('/login')
}
</script>

<style scoped>
.app-shell {
  min-height: 100vh;
}

.sidebar {
  background: var(--sidebar);
  color: #fff;
}

.brand {
  height: 60px;
  display: flex;
  align-items: center;
  padding: 0 18px;
  font-weight: 800;
  letter-spacing: 0;
}

.menu-icon {
  width: 18px;
  height: 18px;
  margin-right: 10px;
}

.topbar {
  height: 60px;
  background: #fff;
  border-bottom: 1px solid var(--border);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.role-tag {
  margin-left: 12px;
  color: var(--muted);
  font-size: 13px;
}

.user-area {
  display: flex;
  align-items: center;
  gap: 12px;
}

.main-content {
  padding: 20px;
}
</style>
