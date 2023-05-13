import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '主页',
      component: () => import('@/views/dashboard/index'),
      meta: {title: '主页', icon: 'dashboard'}
    }],
    hidden: true
  },
  {
    path: '/sys',
    component: Layout,
    redirect: '/user',
    children: [{
      path: 'user',
      name: '用户信息',
      component: () => import('@/views/sys/UserInfo'),
      meta: {title: '用户信息'}
    }],
    hidden: true
  },
  {
    path: '/smsServer',
    component: Layout,
    redirect: '/smsServer/channels',
    name: '短信平台服务',
    meta: {title: '短信平台服务', icon: 'example'},
    children: [
      {
        path: 'appList',
        name: '应用管理',
        component: () => import('@/views/smsServer/AppList'),
        meta: {title: '应用管理', icon: 'tree'}
      },
      {
        path: 'channels',
        name: '渠道管理',
        component: () => import('@/views/smsServer/SmsChannels'),
        meta: {title: '渠道管理', icon: 'link'}
      },
      {
        path: 'templateList',
        name: '模版管理',
        component: () => import('@/views/smsServer/SmsTemplates'),
        meta: {title: '模版管理', icon: 'nested'}
      },
      {
        path: '/smsServer/templateBinding',
        name: '绑定渠道',
        component: () => import('@/views/smsServer/SmsTemplatesBinding'),
        meta: {title: '绑定渠道',hideInMenu: true },
        hidden : true
      },
      // {
      //   path: 'smsList',
      //   name: '短信管理',
      //   component: () => import('@/views/smsServer/SmsList'),
      //   meta: { title: '短信管理', icon: 'form' }
      // }
      //   ,
      //   {
      //   path: 'ruleList',
      //     name: '规则管理',
      //   component: () => import('@/views/smsServer/RuleList'),
      //   meta: { title: '规则管理', icon: 'password' }
      // }
    ]
  },


  // 404 page must be placed at the end !!!
  {path: '*', redirect: '/404', hidden: true}
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({y: 0}),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
