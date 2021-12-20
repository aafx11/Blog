import service from "../http";

export const getTagList=(size) => {
    return service({
        url:"/tag/getTagList",
        method:'get',
        params:{size:size}
    })
}