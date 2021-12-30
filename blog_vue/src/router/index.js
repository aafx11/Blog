import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import test from "../components/test/test.vue";
import axios from "axios";
import store from '../store'
import service from "../request/http";
// import role from '../components/admin/view/system/sys/role.vue'

const role = () => import('../components/admin/view/system/sys/role.vue');
const menu = () => import('../components/admin/view/system/sys/menu.vue');
const userList = () => import('../components/admin/view/system/user/userList.vue');
const comment = () => import('../components/admin/view/system/information/comment.vue');
const resetpassword = () => import('../components/admin/view/system/personalcenter/resetpassword.vue');
const privateletter = () => import('../components/admin/view/system/information/privateletter.vue');
const articleList = () => import('../components/admin/view/system/article/articleList.vue');
const VideoList = () => import('../components/admin/view/system/video/VideoList.vue');


let map = {}
map['role'] = role
map['menu'] = menu
map['userList'] = userList
map['comment'] = comment
map['resetpassword'] = resetpassword
map['privateletter'] = privateletter
map['articleList'] = articleList
map['VideoList'] = VideoList

//requireAuth:游客也能获取,但是游客和登录用户所显示页面不同
//mustLogin：比如登录后才可浏览
const router = createRouter({
    history: createWebHistory(), // hash模式：createWebHashHistory，history模式：createWebHistory
    routes: [
        {
            path: '/',
            redirect: '/index'
        },
        {
            path: '/login',
            name: 'login',
            // component: () => import('../components/admin/view/login/login.vue'),
            component: () => import('../components/community/common/loginPage.vue'),
            meta: {requireAuth: true}
        },
        {
            path: '/register',
            name: 'register',
            component: () => import('../components/community/common/register.vue'),
            meta: {requireAuth: true}
        },
        {
            path: '/home',
            name: 'home',
            component: () => import('../components/community/home/home.vue'),
            meta: {requireAuth: true},
            children: [
                {
                    path: '/index', name: 'index', component: () => import('../components/community/index/index.vue'),
                    meta: {requireAuth: true}, redirect: '/latestArticles',
                    children: [
                        {
                            path: '/popularArticles',
                            name: 'hotArticle',
                            component: () => import('../components/community/index/popularArticles.vue'),
                            meta: {requireAuth: true}
                        },
                        {
                            path: '/latestArticles',
                            name: 'latestArticle',
                            component: () => import('../components/community/index/latestArticle.vue'),
                            meta: {requireAuth: true}
                        },
                        {
                            path: '/followArticles',
                            name: 'followArticle',
                            component: () => import('../components/community/index/followArticle.vue'),
                            meta: {requireAuth: true}
                        },
                    ]
                },
                {
                    path: '/videoPage',
                    name: 'videoPage',
                    component: () => import('../components/community/video/videoPage.vue'),
                    redirect:'/recommendedVideo',
                    meta: {requireAuth: true},
                    children:[
                        {
                            path: '/videoUpload',
                            name: 'videoUpload',
                            component:() => import('../components/community/video/videoUpload.vue')
                        },
                        {
                            path: '/manuscript',
                            name: 'manuscript',
                            component:() => import('../components/community/video/Manuscript.vue')
                        },
                        {
                            path: '/editVideo/:id',
                            name: 'editVideo',
                            component:() => import('../components/community/video/EdieVideo.vue')
                        },
                        {
                            path: '/recommendedVideo',
                            name: 'recommendedVideo',
                            component:() => import('../components/community/video/RecommendedVideo.vue')
                        },
                        {
                            path: '/videoFollow',
                            name: 'videoFollow',
                            component:() => import('../components/community/video/VideoFollow.vue')
                        },
                        {
                            path: '/hotVideoPage',
                            name: 'hotVideoPage',
                            component:() =>import('../components/community/video/HotVideoPage.vue')
                        },
                        {
                            path: '/videoTagsPage/:id/:name',
                            name: 'videoTagsPage',
                            component:() =>import('../components/community/video/VideoTagsPage.vue')
                        }

                    ]
                },
                {
                    path: '/videoDetail/:id',
                    name: 'videoDetail',
                    component:() => import('../components/community/video/VideoDetail.vue')
                },
                {
                    path: '/searchPage/:content',
                    name: 'searchPage',
                    component: () => import('../components/community/common/searchPage.vue')
                },
                {
                    path: '/editArticle',
                    name: 'editArticle',
                    component: () => import('../components/community/index/editArticle.vue'),
                    meta: {mustLogin: true}
                },
                {
                    path: '/personal',
                    name: 'personal',
                    component: () => import('../components/community/personalCenter/personalCenter.vue'),
                    meta: {mustLogin: true},
                    children: [
                        {
                            path: '/myHomePage',
                            name: 'myHomePage',
                            component: () => import('../components/community/personalCenter/myHomePage.vue'),
                            meta: {mustLogin: true},
                        },
                        {
                            path: '/editProfile',
                            name: 'editProfile',
                            component: () => import('../components/community/personalCenter/editProfile.vue'),
                            meta: {mustLogin: true},
                        },
                        {
                            path: '/myAttention',
                            name: 'myAttention',
                            component: () => import('../components/community/personalCenter/MyAttention.vue'),
                            meta: {mustLogin: true},
                        },
                        {
                            path: '/myFanList',
                            name: 'myFanList',
                            component: () => import('../components/community/personalCenter/MyFanList.vue'),
                            meta: {mustLogin: true},
                        },
                    ]
                },
                {
                    path: '/tagFile',
                    name: 'tagFile',
                    component: () => import('../components/community/tagFile/tag.vue'),
                    meta: {requireAuth: true},
                    children: [
                        {
                            path: '/tagFile/:id',
                            name: 'tagDetail',//params必须用name来识别路径
                            component: () => import('../components/community/tagFile/tagDetail.vue'),
                            meta: {requireAuth: true}
                        },
                    ]
                },
                {
                    path: '/hotArticles',
                    name: 'hotArticles',
                    component: () => import('../components/community/index/hotArticles.vue'),
                    meta: {requireAuth: true},
                },
                {
                    path: '/userInfo/u/:id',
                    name: 'userInfo',
                    component: () => import('../components/community/personalCenter/UserInfo.vue'),
                    meta: {requireAuth: true},
                    children:[
                        {
                            path: '/userInfo/article',
                            name: 'articleList',
                            component: () => import('../components/community/personalCenter/ArticleList.vue'),
                            meta: {requireAuth: true},
                        }
                    ]
                }

            ]
        },
        {
            path: '/article/:id',
            name: 'articleDetail',
            component: () => import('../components/community/index/articleDetail.vue'),
            meta: {requireAuth: true}
        },

        {
            path: '/admin',
            name: 'admin',
            component: () => import('../components/admin/view/system/manage.vue'),
            redirect: '/welcome',
            children: [
                {
                    path: '/welcome',
                    name: 'welcome',
                    component: () => import('../components/admin/view/system/welcome.vue')
                },
            ]
        },
        {
            path: '/privateLetter',
            name: 'privateLetter',
            component: () => import('../components/community/common/WebSocket.vue')
        },
        {
            path: "/test",
            name: "test",
            component: () => import('../components/test/test.vue'),


        },
    ],
    scrollBehavior(to, from, savedPosition) {
        if (to.name == 'articleDetail') {
            return {top: 0}
        }
    }


})


router.beforeEach((to, from, next) => {

    // const formData = new FormData();
    // formData.append('role',role);

    console.log("去的路径", to.path);
    let hasRoute = store.getters.hasRoutes
    let token = localStorage.getItem("token")
    store.commit('setToken', localStorage.getItem("token"))

    function getNavRouteUser() {
        service.get('/menu/getNav', {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }).then(res => {
            store.commit("setMenuList", res.data.data.nav) //保存菜单列表
            store.commit("setPermList", res.data.data.authoritys)//保存用户权限

            //动态绑定路由
            let newRoutes = router.options.routes; //获取当前的路由，和动态路由，组成新的路由
            res.data.data.nav.forEach(
                menu => {
                    if (menu.children) {
                        menu.children.forEach(e => {
                            // console.log("e是什么",e);
                            // component: formData.get('\''+e.component+'\'')
                            // console.log(formData.get('role'));

                            router.addRoute('admin', {
                                path: e.path,
                                name: e.name,
                                // component: () => import('../components/admin/view/system/' + e.component + '.vue')
                                // component: role
                                component: map[e.component]
                                // component: formData.get('role')
                            })
                        })
                        next({...to, replace: true})//确保动态路由生成完成
                    }
                }
            )
            hasRoute = true
            store.commit("changeRoutesStatus", hasRoute)
            next();
        });
    }

    if (to.path == '/login') {
        if (!token || token == "" || token == null || token == "null") {
            next();
        } else if (token) {
            next({path: '/popularArticles'})
        }
    }

    if (to.meta.mustLogin) {
        if (!token) {
            next({path: '/login'})
        } else if (!hasRoute && token) {
            console.log("1");
            getNavRouteUser();
            next()
        } else if (hasRoute && token) {
            next();
        }
    }

    if (to.meta.requireAuth) {
        if (!token || token == "" || token == null || token == "null") {
            next()
        } else if (!hasRoute && token) {
            console.log("此时的token", token);
            console.log("2");
            getNavRouteUser();
        } else if (hasRoute && token) {
            next();
        }
    } else if (!token) {
        next({path: '/login'})
    } else if (!hasRoute && token) {
        console.log("3");
        getNavRouteUser();
    } else if (hasRoute && token) {
        next();
    }
})


/*将导航的参数转化成路由*/
const menuToRoute = (menu) => {
    if (!menu.component) {
        return null
    }
    let route = {
        name: menu.name,
        path: menu.path,
        meta: {
            icon: menu.icon,
            title: menu.title
        },
        component: menu.component
    }

    return route;
}


export default router










