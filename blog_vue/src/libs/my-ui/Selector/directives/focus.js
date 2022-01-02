export default {
    mounted(el, {value}) {
        const oSelectorInput = el.querySelector('.selector-input'),
            oSelectorMenu = el.querySelector('.selector-menu')

        const oInput = oSelectorInput.querySelector('input'),
            oPlaceholder = oSelectorInput.querySelector('label'),

            oIcon = oSelectorInput.querySelector('span')

        oInput.addEventListener('focus', function () {
            console.log(oInput.placeholder);

            oInput.placeholder = ''
            // oPlaceholder.style.display = 'none';
            oIcon.className = 'iconfont icon-icon_search'

            setTimeout(() => {
                oSelectorMenu.style.display = 'block'
            }, 200)
        }, false)

        oInput.addEventListener('blur', function () {

            oIcon.className = 'iconfont icon-icon_arrow_down'


            setTimeout(() => {
                oSelectorMenu.style.display = 'none'

                if (this.value.length === 0) {
                    // oPlaceholder.style.display = 'block'
                    oInput.placeholder = value
                }
            }, 200)
        }, false)
    }
}