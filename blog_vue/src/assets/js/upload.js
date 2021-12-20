import {ElMessage} from 'element-plus';
/* --------------------------
 1.判断图片格式和大小
 2.判断视频格式和大小
 -------------------------- */




/* --------------------------
 1.判断图片格式和大小  Js Code Starts Here
 -------------------------- */
export const isImg = (img) => {
    let types = ['image/jpeg', 'image/jpg', 'image/gif', 'image/bmp', 'image/png'];
    const isImage = types.includes(img.type);
    const isLtSize = img.size / 1024 / 1024 < 5;
    if (!isImage) {
        ElMessage.error('上传图片只能是 JPG、JPEG、gif、bmp、PNG 格式!');
        return false;
    }
    if (!isLtSize) {
        ElMessage.error('上传图片大小不能超过 5MB!');
        return false;
    }
    return true;
}
 /* --------------------------
   判断图片格式和大小 Js Code End Here
 -------------------------- */


/* --------------------------
 2.判断视频格式和大小  Js Code Starts Here
 -------------------------- */
export const isVideo = (video) => {
    const isLt500M = video.size / 1024 / 1024 < 500;
    if (['video/mp4', 'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb'].indexOf(video.type) == -1) {
        ElMessage.error('上传视频只能是 mp4、ogg、flv、avi、wmv、rmvb 格式!');
        return false;
    }
    if (!isLt500M) {
        ElMessage.error('上传视频大小不能超过 500MB!');
        return false;
    }
    return true;
}
 /* --------------------------
   判断视频格式和大小 Js Code End Here
 -------------------------- */
