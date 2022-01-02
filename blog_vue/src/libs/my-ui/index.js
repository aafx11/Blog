
import mySelector from './Selector/index.vue'

let myUI = {}

myUI.install = function (Vue) {
    Vue.component(mySelector.name, mySelector)
}

export default myUI;