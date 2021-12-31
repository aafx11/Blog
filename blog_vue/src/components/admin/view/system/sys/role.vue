<template>
  <!--搜索，添加，删除-->
  <el-form :inline="true" @submit.native.prevent class="top-container">
    <el-form-item>
      <el-input
          v-model="searchForm.roleName"
          placeholder="请输入搜索的名称"
          clearable
      >
      </el-input>
    </el-form-item>
    <el-form-item>
      <my-button @click="getRoleList">搜索</my-button>
    </el-form-item>
    <el-form-item>
      <!--      <el-button type="primary" @click="dialogVisible = true">添加角色</el-button>-->
      <my-button type="primary" @click="dialogVisible = true">添加角色</my-button>
    </el-form-item>
    <el-form-item>
      <el-popconfirm
          title="确定要批量删除吗？"
          @confirm="deleteHandle(null)"
      >
        <template #reference>
          <my-button type="danger" :disabled="btnState">批量删除</my-button>
        </template>
      </el-popconfirm>
    </el-form-item>
  </el-form>

  <!--  <el-button type="danger" :disabled="btnState">批量删除</el-button>-->

  <!--角色管理表单信息-->
  <el-table
      ref="multipleTable"
      :data="tableData"
      border stripe
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="roleName" label="名称" width="120"></el-table-column>
    <el-table-column prop="codes" label="唯一编码" width="120"></el-table-column>
    <el-table-column prop="describes" label="描述"></el-table-column>
    <el-table-column prop="state" label="状态" width="120">
      <template #default="scope">
        <el-tag v-if="scope.row.state === 0" size="small" type="danger">禁用</el-tag>
        <el-tag v-else-if="scope.row.state === 1" size="small" type="success">正常</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="250" fixed="right">
      <template #default="scope">
        <div class="control-container">
        <!--        <el-button type="text" @click="permHandle(scope.row.id)">分配权限</el-button>-->
        <my-button type="primary" size="mini" @click="permHandle(scope.row.id)">分配权限</my-button>

<!--        <el-divider direction="vertical"></el-divider>-->
        <!--        <el-button type="text" @click="editHandle(scope.row.id)">编辑</el-button>-->
        <my-button type="primary" size="mini" @click="editHandle(scope.row.id)" letter-spacing>编辑</my-button>
<!--        <el-divider direction="vertical"></el-divider>-->
        <el-popconfirm
            title="确定删除吗？"
            @confirm="deleteHandle(scope.row.id)"

        >
          <template #reference>
            <my-button type="danger" size="mini" letter-spacing>删除</my-button>
          </template>
        </el-popconfirm>
        </div>
      </template>
    </el-table-column>
  </el-table>
  <!--  <el-button type="danger">删除</el-button>-->
  <!--分页-->
  <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="size"
      :current-page="current"
      :total="total">
  </el-pagination>

  <!--添加,添加角色弹窗-->
  <el-dialog
      title="角色信息"
      v-model="dialogVisible"
      width="40%"
      @closed="resetedForm('edForm')"
  >
    <el-form :model="editForm" :rules="editFormRules" ref="edForm">
      <el-form-item label="角色名称" prop="roleName" label-width="100px">
        <el-input v-model="editForm.roleName" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="角色编码" prop="codes" label-width="100px">
        <el-input v-model="editForm.codes" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="角色描述" prop="describes" label-width="100px">
        <el-input v-model="editForm.describes" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item label="角色状态" prop="state" label-width="100px">
        <el-radio-group v-model="editForm.state">
          <!--选择禁用，相当于选择一个0-->
          <el-radio :label=0>禁用</el-radio>
          <el-radio :label=1>正常</el-radio>
        </el-radio-group>
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="resetedForm('edForm')">取 消</el-button>
      <el-button type="primary" @click="submitEditForm('edForm')">确 定</el-button>
    </div>
  </el-dialog>


  <!-- 分配权限弹窗 -->
  <el-dialog
      title="分配权限"
      v-model="permDialogVisible"
      width="40%"
  >
    <el-form :model="permForm" ref="peForm">
      <el-tree
          :data="permTreeData"
          :props="defaultProps"
          ref="permTree"
          :check-strictly=true
          node-key="id"
          :default-expand-all=true
          show-checkbox
      ></el-tree>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="resetpeForm('peForm')">取 消</el-button>
      <el-button type="primary" @click="submitPermForm('peForm')">确 定</el-button>
    </div>
  </el-dialog>

</template>

<script>
import {ref, reactive, getCurrentInstance, onMounted} from 'vue'
import {useStore} from "vuex";
import myButton from '../../../../../view/myButton.vue'
import {ElMessageBox, ElMessage} from 'element-plus';
import {updateOrSave, getRoleInfoById, roleList, deleteById} from '../../../../../request/api/role.js'
import {MenuList} from '../../../../../request/api/menu.js'
import service from "../../../../../request/http.js";

export default {
  name: "role",
  components: {
    myButton: myButton
  },
  data() {
    return {
      editForm: {},
      searchForm: {
        roleName: ''
      } /*搜索表单*/,
      defaultProps: {
        children: 'children',
        label: 'menuName'
      },
    }
  },
  methods: {
    /*提交添加菜单表格
  * 添加方法和编辑方法共用，通过判断表单是否带有id，判断是save还是update方法
  * */
    submitEditForm(formName) {
      this.$refs.edForm.validate((valid) => {
        if (valid) {
          updateOrSave(this.editForm).then(() => {
            ElMessage({
              showClose: true,
              message: '操作成功',
              type: 'success',
              /*操作成功后的回调函数,更新数据*/
              onClose: () => {
                this.getRoleList();
              }
            })
            this.dialogVisible = false;
          })
        } else {
          ElMessage({
            showClose: true,
            message: '操作错误',
            type: 'info',
          })
          return false;
        }
      });
    },

    /*获取权限分配信息*/
    permHandle(id) {
      this.permDialogVisible = true;
      getRoleInfoById(id).then((res) => {
        this.$refs.permTree.setCheckedKeys(res.data.data.menuIds)  /*勾选已有的权限*/
        this.permForm = res.data.data
      })

    },
    /*取消（重置）菜单表格*/
    resetedForm(formName) {
      this.$refs.edForm.resetFields();
      this.editForm = {};
      this.dialogVisible = false;
    },
    /*取消（重置）菜单表格*/
    resetpeForm(formName) {
      this.$refs.peForm.resetFields();
      this.editForm = {};
      this.permDialogVisible = false;
    }
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const btnState = ref(true); /*删除按钮的状态*/
    let size = ref(10);/*每页多少行信息*/
    let current = ref(1);/*当前所在页数*/
    let total = ref();/*表单总数*/
    const permDialogVisible = ref(false);
    const dialogVisible = ref(false);
    const multipleSelection = reactive([]);/*多选时，勾选的数据*/
    const permTreeData = reactive([])/*权限树*/
    const checkStrictly = ref(true)/*是否开启权限树的强关联*/


    /*添加，编辑表单验证规则*/
    const editFormRules = reactive({
      roleName: [{required: true, message: '请输入名称', trigger: 'blur'}],
      codes: [{required: true, message: '请输入唯一编码', trigger: 'blur'}],
      describes: [{required: false, message: '请输入角色描述', trigger: 'blur'}],
      state: [{required: true, message: '请选择状态', trigger: 'blur'}]
    })

    /*角色管理表单*/
    const tableData = reactive([]);

    /*根据权限是否显示按钮，未完成*/
    // const hasAuth = () => {
    //   return true;
    // }
    // const hasAuth = () => {
    //   return proxy.$globalFun.hasAuth();
    // }


    /*编辑按钮函数*/
    const editHandle = (id) => {
      getRoleInfoById(id).then((res) => {
        proxy.editForm = res.data.data;

        dialogVisible.value = true
      })
    }


    /*批量删除角色和单独删除*/
    const deleteHandle = (id) => {
      const ids = []
      if (id) {
        ids.push(id) /*当id不为空，即单独删除某个id时*/
      } else {
        /*当id为空，即多选时，将多选的id每一个都放进ids*/
        proxy.multipleSelection.forEach(row => {
          ids.push(row.id)
        })
      }
      deleteById(ids).then((res) => {
        ElMessage({
          showClose: true,
          message: '删除成功',
          type: 'success',
          onClose: () => {
            proxy.getRoleList();
          }
        })
      })
    }

    const handleSelectionChange = (val) => {
      proxy.multipleSelection = val;
      btnState.value = val.length == 0;
    }

    const handleSizeChange = (val) => {
      console.log(`每页 ${val} 条`);
      size.value = val
      proxy.getRoleList()
    }

    const handleCurrentChange = (val) => {
      console.log(`当前页: ${val}`);
      current.value = val
      proxy.getRoleList()
    }

    const permForm = reactive({})/*权限表单*/
    /*提交权限编辑表单*/
    const submitPermForm = (formName) => {
      let menuIds = []
      menuIds = proxy.$refs.permTree.getCheckedKeys()/*获取所有勾选的主键（id）*/
      service.post("/role/perm/" + proxy.permForm.id, menuIds).then(res => {
        ElMessage({
          showClose: true, message: '恭喜你，操作成功', type: 'success', onClose: () => {
            proxy.resetpeForm()
          }
        });
      })


      permDialogVisible.value = false
      proxy.$forceUpdate();

    }


    /*获取角色列表*/
    const getRoleList = () => {
      roleList({
        roleName: proxy.searchForm.roleName,
        current: current.value,
        size: size.value
      }).then((res) => {
        proxy.tableData = res.data.data.records
        size.value = res.data.data.size
        current.value = res.data.data.current
        total.value = res.data.data.total
        proxy.$forceUpdate();
      })
    };

    onMounted(() => {
      getRoleList();
      MenuList().then(res => {
        proxy.permTreeData = res.data.data
      })
    })
    return {
      btnState,
      tableData,
      size,
      current,
      total,
      editFormRules,
      dialogVisible,
      multipleSelection,
      permDialogVisible,
      permForm,
      permTreeData,
      checkStrictly,
      // hasAuth,
      deleteHandle,
      editHandle,
      getRoleList,
      handleSelectionChange,
      handleSizeChange,
      handleCurrentChange,
      submitPermForm,
    };
  }

}

</script>

<style scoped>
.el-pagination {
  float: right;
}

/*顶部操作栏*/
.top-container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 15px 10px 0 10px;
}
.control-container{
  display: flex;
  justify-content: space-around;
  align-items: center;
}
</style>