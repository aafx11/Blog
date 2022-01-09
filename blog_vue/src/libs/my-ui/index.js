import mySelector from './Selector/index.vue'
import myModal from './Modal/modal.vue'
import treeMenu from './Nav/treeMenu.vue'
import menuItem from './Nav/menuItem.vue'
import subMenu from './Nav/subMenu.vue'
import reSubMenu from './Nav/reSubMenu.vue'
import magnifier from './magnifier/magnifier.vue'
let myUI = {}

myUI.install = function (Vue) {
    Vue.component(mySelector.name, mySelector);
    Vue.component(myModal.name, myModal);
    Vue.component(treeMenu.name, treeMenu);
    Vue.component(menuItem.name, menuItem);
    Vue.component(subMenu.name, subMenu);
    Vue.component(reSubMenu.name, reSubMenu);
    Vue.component(magnifier.name, magnifier);

}

export default myUI;