import service from "../http";

export const MenuList=() => {
    return service({
        url:"/menu/MenuList",
        method:'get',
    })
}

export const getMenuById=(id) => {
    return service({
        url:"/menu/MenuInfo/" + id,
        method:'get',
    })
}

export const deleteById=(id) => {
    return service({
        url:'/menu/delete/'+id,
        method:'post',
    })
}

export const updateOrSave=(data) => {
    return service({
        url:'/menu/' + (data.id?'update' : 'save'),
        method:'post',
        data
    })
}