import mySelector from './Selector/index.vue'
import myModal from './Modal/modal.vue'

let myUI = {}

myUI.install = function (Vue) {
    Vue.component(mySelector.name, mySelector);
    Vue.component(myModal.name, myModal);
}

export default myUI;