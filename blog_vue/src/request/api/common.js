import service from "../http";
import * as qs from "qs";

export const VerificationCode = () => {
    return service({
        url: "/getCodeImg",
        method: 'get',
    })
}

export const login= (loginForm) => {
    return service({
        url:'/login?' + qs.stringify(loginForm),
        method:'post'
    })
}

export const register=(data) => {
    return service({
        url:"/register",
        method:'post',
        data
    })
}

export const UserLogout=() => {
    return service({
        url:"/logout",
        method:'get',
    })
}

export const updatePasswd=(data) => {
    return service({
        url:'/user/updatePassWord',
        method:'post',
        data
    })
}

export const upload=(formData) => {
    return service({
        headers: {'content-Type': 'multipart/form-data'},
        url:"/upload/ImgUpload",
        method:'post',
        data:formData
    })
}

//上传文章图片到本地
export const uploadArticleImgToLocal=(formData) => {
    return service({
        headers: {'content-Type': 'multipart/form-data'},
        url:"/upload/uploadArticleImgToLocal",
        method:'post',
        data:formData
    })
}

export const AvatarUpload=(formData) => {
    return service({
        headers: {'content-Type': 'multipart/form-data'},
        url:"/upload/AvatarUpload",
        method:'post',
        data:formData
    })
}

export const videoUpload=(formData,percent) => {
    return service({
        headers: {'content-Type': 'multipart/form-data'},
        url:"/upload/videoUpload",
        method:'post',
        data:formData,
        onUploadProgress:progressEvent =>{
             percent=(progressEvent.loaded / progressEvent.total * 100) | 0;
        }
    })
}

//上传头像base64
export const AvatarUploadBase64=(formData) => {
    return service({
        headers: {'content-Type': 'multipart/form-data'},
        url:"/upload/AvatarUploadBase64",
        method:'post',
        data:formData
    })
}
//上传头像到本地
export const saveAvatarToLocal=(formData) => {
    return service({
        headers: {'content-Type': 'multipart/form-data'},
        url:"/upload/saveAvatarToLocal",
        method:'post',
        data:formData

    })
}

//上传文章封面到本地
export const uploadArticleCoverToLocal=(formData) => {
    return service({
        headers: {'content-Type': 'multipart/form-data'},
        url:"/upload/uploadArticleCoverToLocal",
        method:'post',
        data:formData

    })
}