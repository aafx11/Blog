<template>
  <div>
    <!--添加菜单按钮-->
    <!--    <el-form :inline="true">-->
    <!--      <el-form-item>-->
    <!--        <el-button color="#626aef" @click="dialogVisible = true">添加菜单</el-button>-->
    <!--      </el-form-item>-->
    <!--    </el-form>-->
    <div class="top-container">
      <my-button type="primary" @click="dialogVisible = true">添加菜单</my-button>
    </div>

    <!--菜单信息表格-->
    <el-table
        :data="tableData"
        style="width: 100%;margin-bottom: 20px;"
        row-key="id"
        stripe
        border
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column
          prop="menuName"
          label="菜单名称"
          sortable
          width="190">
      </el-table-column>
      <el-table-column
          prop="perms"
          label="权限编码"
          sortable
          width="180">
      </el-table-column>
      <el-table-column
          prop="icon"
          label="图标">
      </el-table-column>
      <el-table-column
          prop="type"
          label="类型">
        <template #default="scope">
          <el-tag size="small" v-if="scope.row.type === 0" type="success">目录</el-tag>
          <el-tag size="small" v-if="scope.row.type === 1" type="info">菜单</el-tag>
          <el-tag size="small" v-if="scope.row.type === 2">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="path"
          label="菜单路径">
      </el-table-column>
      <el-table-column
          prop="component"
          label="菜单组件">
      </el-table-column>
      <el-table-column
          prop="orderNum"
          label="排序号">
      </el-table-column>
      <el-table-column
          prop="state"
          label="状态">
        <template #default="scope">
          <el-tag size="small" v-if="scope.row.state === 1">正常</el-tag>
          <el-tag size="small" v-if="scope.row.state === 0">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="150">
        <template #default="scope">
          <!--          <el-tooltip class="item" effect="dark" content="编辑" placement="top" enterable>-->
          <!--            <el-button type="warning" icon="el-icon-edit" @click="editHandle(scope.row.id)"></el-button>-->
          <!--          </el-tooltip>-->
          <div class="button-container">
            <my-button type="primary" size="mini" @click="editHandle(scope.row.id)">编辑</my-button>
            <!--删除按钮-->
            <el-popconfirm
                title="确定删除吗？"
                @confirm="deleteHandle(scope.row.id)"
                v-if="isAuth('menu:delete')"
            >
              <template #reference>
                <my-button type="danger" size="mini">删除</my-button>
              </template>
            </el-popconfirm>

          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <!--  <el-button size="mini" class="delete-button" auto-insert-space="true">删除</el-button>-->


  <!--添加和编辑菜单表格弹窗-->
  <el-dialog
      title="菜单"
      v-model="dialogVisible"
      width="40%"
      :before-close="initForm"
  >
    <el-form :model="editForm" ref="edit" :rules="editFormRules" :key="editForm.parentId">
      <el-form-item label="上级菜单" label-width="100px" prop="parentId">
        <el-select v-model="editForm.parentId" placeholder="请选择上级菜单">
          <template v-for="item in tableData">

            <el-option :label="item.menuName" :value="item.id"></el-option>

            <template v-for="child in item.children">
              <el-option :label="child.menuName" :value="child.id"><span>{{ '- ' + child.menuName }}</span>
              </el-option>
            </template>
          </template>
        </el-select>
      </el-form-item>
      <el-form-item label="菜单名称" label-width="100px" prop="menuName">
        <el-input v-model="editForm.menuName" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="权限编码" label-width="100px" prop="perms">
        <el-input v-model="editForm.perms" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="图标" label-width="100px" prop="icon">
        <el-input v-model="editForm.icon" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="菜单URL" label-width="100px" prop="path">
        <el-input v-model="editForm.path" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="菜单组件" label-width="100px" prop="component">
        <el-input v-model="editForm.component" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="类型" label-width="100px" prop="type">
        <el-radio-group v-model="editForm.type">
          <el-radio :label=0>目录</el-radio>
          <el-radio :label=1>菜单</el-radio>
          <el-radio :label=2>按钮</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="状态" label-width="100px" prop="state">
        <el-radio-group v-model="editForm.state">
          <!--选择禁用，相当于选择一个0-->
          <el-radio :label=0>禁用</el-radio>
          <el-radio :label=1>正常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序号" label-width="100px" prop="orderNum">
        <el-input-number v-model="editForm.orderNum" :min="1" label="排序号">1</el-input-number>
      </el-form-item>
    </el-form>


    <template #footer>
      <div slot="footer" class="dialog-footer">
        <el-button @click="initForm">取 消</el-button>
        <el-button type="primary" @click="submitEditForm('edit')">确 定</el-button>
      </div>
    </template>

  </el-dialog>


</template>

<script>
import {ref, reactive, getCurrentInstance, onMounted, onBeforeMount, computed, isReactive} from 'vue'
import {ElMessageBox, ElMessage} from 'element-plus';
import {MenuList, getMenuById, deleteById, updateOrSave} from '../../../../../request/api/menu.js'
import {hasAuth} from '../../../../../assets/js/global.js'
import myButton from '../../../../../view/myButton.vue'

export default {
  name: "menuManagement",
  components: {
    myButton: myButton
  },
  data() {
    return {
      editFormRules: {
        parentId: [{required: true, message: '请选择上级菜单', trigger: 'blur'}],
        menuName: [{required: true, message: '请输入菜单名称', trigger: 'blur'}],
        perms: [{required: true, message: '请输入权限编码', trigger: 'blur'}],
        // path:  [{required: true, message: '请输入路径', trigger: 'blur'}],
        // icon:[{required: true, message: '请输入图标', trigger: 'blur'}]
        // orderNum: [{required: true, message: '请填入排序号', trigger: 'blur'}],
        type: [{required: true, message: '请选择菜单类型', trigger: 'blur'}],
        state: [{required: true, message: '请选择状态', trigger: 'blur'}],
      },
      editForm: {}
    }
  },
  methods: {

    /*提交添加菜单表格
    * 添加方法和编辑方法共用，通过判断表单是否带有id，判断是save还是update方法*/
    submitEditForm() {
      this.$refs.edit.validate((valid) => {
        if (valid) {
          updateOrSave(this.editForm).then(res => {
            ElMessage({
              showClose: true,
              message: '恭喜你，操作成功',
              type: 'success',
              onClose: () => {
                this.getMenuList();
              }
            });
            this.initForm();
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    }

  },

  setup() {
    const {proxy} = getCurrentInstance();


    /*菜单信息*/
    let tableData = reactive([]);
    const tableDisable = ref(false)
    /*获取菜单信息*/
    const getMenuList = () => {
      MenuList().then(res => {
        proxy.tableData = res.data.data
        proxy.tableData.push({id: 0});

        console.log("菜单信息", proxy.tableData);
        proxy.$forceUpdate();
        tableDisable.value = true
      })
    }


    /*删除按钮是否显示，待完成*/
    const isAuth = (data) => {
      return hasAuth(data);
    }

    const dialogVisible = ref(false);
    /*编辑按钮函数*/
    let editHandle = (id) => {
      getMenuById(id).then(res => {
        proxy.editForm = res.data.data;
        dialogVisible.value = true
      })
    }


    /*删除菜单*/
    const deleteHandle = (id) => {
      deleteById(id).then(res => {
        ElMessage({
          showClose: true,
          message: '删除成功',
          type: 'success',
          onClose: () => {
            proxy.getMenuList();
          }
        })
      })
    }

    /*取消（重置）菜单表格*/
    const initForm = () => {
      proxy.$refs.edit.resetFields();
      proxy.editForm = {}
      proxy.dialogVisible = false;
    }

    onMounted(
        () => {
          getMenuList();
        }
    )
    return {
      tableData,
      dialogVisible,
      tableDisable,
      initForm,
      getMenuList,
      isAuth,
      deleteHandle,
      editHandle,
    }
  },

}
</script>

<style scoped>
/*添加菜单按钮*/
.top-container {
  height: 64px;
  padding: 16px 16px;
}
.button-container{
  display: flex;
  justify-content: space-around;
}

</style>