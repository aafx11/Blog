export const test=() => {
    const aa = document.querySelector('.contact-icon')
    // console.log("123",aa.childNodes[1].children[0]);
    // aa.children[0].firstChild.style.color='blue'

    for (let i=0; i <=aa.children.length; i++){

        aa.children[i].firstChild.addEventListener('mouseenter',()=>{

            aa.children[i].firstChild.style.color='#3eaf7c'
        })

        aa.children[i].firstChild.addEventListener('mouseleave',()=>{
            aa.children[i].firstChild.style.color='#0c0c0c'
        })
    }
    // const icon = document.querySelector('.icon-style')
    // console.log(icon);
    // icon.firstChild.style.color='blue'
}