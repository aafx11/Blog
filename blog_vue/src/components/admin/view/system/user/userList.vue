<template>
  <!--搜索框-->
  <el-form :inline="true" :model="searchForm" @submit.native.prevent class="top-container">
    <el-form-item>
      <el-input v-model="searchForm.username" placeholder="名称" clearable></el-input>
    </el-form-item>
    <el-form-item>
      <!--      <el-button @click="getUserList">搜索</el-button>-->
      <my-button @click="getUserList">搜索</my-button>
    </el-form-item>
    <!--      <el-button type="primary" @click="dialogFormVisible = true" >添加</el-button>-->
    <el-form-item>
      <my-button type="primary" @click="dialogFormVisible = true" v-if="isAuth('user:save')">添加</my-button>
    </el-form-item>
    <el-form-item>
      <el-popconfirm
          title="确定删除吗？"
          @confirm="deleteHandle(null)"
          v-if="isAuth('user:delete')"
      >
        <template #reference>
          <my-button type="danger" :disabled="delBtnState">批量删除</my-button>
        </template>
      </el-popconfirm>
    </el-form-item>
  </el-form>

  <!--  <el-button type="danger">批量删除</el-button>-->

  <!--用户信息列表-->
  <el-table ref="multipleTable" border stripe :data="tableData" tooltip-effect="dark" style="width: 100%"
            @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column label="头像" width="50">
      <template #default="scope">
        <el-avatar size="small" :src="'http://localhost:8081/static/avatar/'+scope.row.avatar"></el-avatar>
      </template>
    </el-table-column>
    <el-table-column prop="username" label="用户名" width="120"></el-table-column>
    <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>

    <el-table-column label="角色名称" width="180">
      <template #default="scope">
        <el-tag style="margin-right: 5px;" size="small" type="info" v-for="item in scope.row.roles">
          {{ item.roleName }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="email" label="邮箱" width="200"></el-table-column>
    <el-table-column prop="phone" label="手机号" width="200"></el-table-column>
    <el-table-column label="状态" width="200">
      <template #default="scope">
        <el-tag v-if="scope.row.state === 0" size="small" type="danger">禁用</el-tag>
        <el-tag v-else-if="scope.row.state === 1" size="small" type="success">正常</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="created" label="创建时间" width="200"></el-table-column>
    <el-table-column width="320px" label="操作" fixed="right">
      <template #default="scope">
        <!--        <el-button type="text" @click="roleAssignment(scope.row.id)">分配角色</el-button>-->
        <my-button type="primary" size="mini" @click="roleAssignment(scope.row.id)">分配角色</my-button>
        <el-divider direction="vertical"></el-divider>
        <!--        <el-button type="text" @click="edit(scope.row.id)">编辑</el-button>-->
        <my-button type="primary" size="mini" letter-spacing @click="edit(scope.row.id)">编辑</my-button>
        <el-divider direction="vertical"></el-divider>
        <!--        <el-button type="text" @click="resetPasswd(scope.row.id, scope.row.username)">-->
        <!--          重置密码-->
        <!--        </el-button>-->
        <my-button type="success" size="mini" @click="resetPasswd(scope.row.id, scope.row.username)"    v-if="isAuth('user:respass')">重置密码</my-button>
        <el-divider direction="vertical"></el-divider>
        <el-popconfirm
            title="确定删除吗？"
            @confirm="deleteHandle(scope.row.id)"
            v-if="isAuth('user:delete')"
        >
          <template #reference>
            <my-button type="danger" size="mini" letter-spacing >删除</my-button>
          </template>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>
  <!--  <el-button type="danger">删除</el-button>-->


  <!--分页-->
  <div class="bottom-container">
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="current"
                   :page-sizes="[10, 20, 50, 100]" :page-size="size" layout="total, sizes, prev, pager, next, jumper"
                   :total="total"></el-pagination>
  </div>
  <!--新增用户对话框-->
  <el-dialog
      title="用户信息"
      v-model="dialogFormVisible"
      width="600px"
      @closed="resetedForm"
  >
    <el-form :model="editForm" :rules="editFormRules" ref="edForm">
      <el-form-item label="用户名" prop="username" label-width="100px">
        <el-input v-model="editForm.username" autocomplete="off"></el-input>
        <el-alert title="初始密码为456789" :closable="false" type="info" style="line-height: 12px;"></el-alert>
      </el-form-item>
      <el-form-item label="昵称" prop="nickname" label-width="100px">
        <el-input v-model="editForm.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email" label-width="100px">
        <el-input v-model="editForm.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="phone" label-width="100px">
        <el-input v-model="editForm.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="状态" prop="state" label-width="100px">
        <el-radio-group v-model="editForm.state">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">正常</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="resetedForm('edForm')">取 消</el-button>
      <el-button type="primary" @click="submitForm('edForm')">确 定</el-button>
    </div>
  </el-dialog>


  <!-- 分配权限对话框 -->
  <el-dialog title="分配角色" v-model="roleDialogFormVisible" width="600px" @closed="resetreForm('reForm')">
    <el-form :model="roleForm" ref="reForm">
      <el-tree
          :data="roleTreeData"
          :props="defaultProps"
          ref="roleTree"
          node-key="id"
          :default-expand-all=true
          show-checkbox
      >

      </el-tree>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="resetreForm('reForm')">取 消</el-button>
      <el-button type="primary" @click="submitRoleForm('reForm')">确 定</el-button>
    </div>
  </el-dialog>


</template>

<script>
import {ref, reactive, getCurrentInstance, onMounted} from 'vue'
import {ElMessageBox, ElMessage} from 'element-plus';
import myButton from '../../../../../view/myButton.vue'
import {
  getRoleById,
  updateOrSave,
  deleteById,
  UserList,
  rePassWord,
  getUserInfoById
} from '../../../../../request/api/userList.js'
import {roleList} from '../../../../../request/api/role.js'
import {useStore} from "vuex";
import service from "../../../../../request/http";


export default {
  name: "userList",
  components: {
    myButton: myButton
  },
  data() {
    return {
      editForm: {},
      defaultProps: {
        children: 'children', label: 'roleName'
      }
    }
  },
  methods: {
    /*获取该用户的角色分配表单*/
    roleAssignment(id) {
      this.roleDialogFormVisible = true
      getRoleById(id).then(res => {
        this.roleForm = res.data.data
        const roleIds = []
        res.data.data.roles.forEach(row => {
          roleIds.push(row.id)
        })
        this.$refs.roleTree.setCheckedKeys(roleIds);
      })
    },

    /*提交编辑或新增表单*/
    submitForm(formName) {
      this.$refs.edForm.validate((valid) => {
        if (valid) {
          updateOrSave(this.editForm).then(res => {
            ElMessage({
              showClose: true,
              message: '恭喜你，操作成功',
              type: 'success',
              onClose: () => {
                this.getUserList()
              }
            });
            this.dialogFormVisible = false
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    /*重置表单*/
    resetedForm(formName) {
      console.log("重置表单");
      this.$refs.edForm.resetFields();
      this.editForm = {}
      this.dialogFormVisible = false
    },
    resetreForm(formName) {
      console.log("重置表单");
      this.$refs.reForm.resetFields();
      this.editForm = {}
      this.roleDialogFormVisible = false
    },
  },
  setup() {
    const {proxy} = getCurrentInstance()
    const dialogFormVisible = ref(false)
    const tableData = reactive([])
    const delBtnState = ref(true) /*默认禁用按钮*/
    const roleDialogFormVisible = ref(false)
    const treeCheckedKeys = reactive([])
    const size = ref(10);/*每页多少行信息*/
    const current = ref(1);/*当前所在页数*/
    const total = ref(100);/*表单总数*/
    const roleForm = reactive([])
    const roleTreeData = reactive([])


    /*搜索*/
    const searchForm = reactive({
      username: ''
    });

    const editFormRules = reactive({
      username: [{required: true, message: '请输入名称', trigger: 'blur'}],
      emil: [{required: true, message: '请输入唯一编码', trigger: 'blur'}],
      state: [{required: true, message: '请选择状态', trigger: 'blur'}]
    })
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
          /*操作成功后的回调函数,更新数据*/
          onClose: () => {
            proxy.getUserList();
          }
        })
      })
    }

    const handleSelectionChange = (rows) => {
      proxy.multipleSelection = rows;
      delBtnState.value = rows.length == 0
    }

    const getUserList = () => {
      UserList({
        username: proxy.searchForm.username,
        current: current.value,
        size: size.value
      }).then(res => {
        proxy.tableData = res.data.data.records
        size.value = res.data.data.size
        current.value = res.data.data.current
        total.value = res.data.data.total
        proxy.$forceUpdate();
      })
    }


    /*重置密码*/
    const resetPasswd = (id, username) => {
      ElMessageBox.confirm('将重置用户【' + username + '】的密码, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        rePassWord(id).then(res => {
          ElMessage({
            showClose: true, message: '恭喜你，操作成功', type: 'success', onClose: () => {
            }
          });
        })
      })
    }

    /*打开编辑表单*/
    const edit = (id) => {
      getUserInfoById(id).then(res => {
        proxy.editForm = res.data.data
        proxy.dialogFormVisible = true
      })
    }

    const handleSizeChange = (val) => {
      console.log(`每页 ${val} 条`);
      size.value = val
      proxy.getUserList()
    }

    const handleCurrentChange = (val) => {
      console.log(`当前页: ${val}`);
      current.value = val
      proxy.getUserList()
    }


    /*提交角色表单*/
    const submitRoleForm = (formName) => {
      let roleIds = []
      roleIds = proxy.$refs.roleTree.getCheckedKeys()
      service.post("/user/perm/" + proxy.roleForm.id, roleIds).then(res => {
        ElMessage({
          showClose: true, message: '恭喜你，操作成功', type: 'success', onClose: () => {
            proxy.resetreForm(formName)
            proxy.getUserList()
          }
        });
        roleDialogFormVisible.value = false
      })
    }

    // const hasAuth = () => {
    //   return proxy.$globalFun.hasAuth();
    // }

    const store = useStore()
    const isAuth = (perm) => {
      // return hasAuth(data);
      const authority = store.state.menus.permList
      if (authority.indexOf(perm) > -1) {
        return true
      } else {
        return false
      }
      }

      onMounted(() => {
        getUserList()
        roleList().then(res => {
          proxy.roleTreeData = res.data.data.records;
        })
      })

      return {
        searchForm,
        dialogFormVisible,
        tableData,
        delBtnState,
        roleDialogFormVisible,
        treeCheckedKeys,
        size,
        current,
        total,
        editFormRules,
        roleForm,
        roleTreeData,
        deleteHandle,
        handleSelectionChange,
        // hasAuth,
        getUserList,
        resetPasswd,
        edit,
        handleCurrentChange,
        handleSizeChange,
        submitRoleForm,
        isAuth,
      }
    }

}

</script>

<style scoped>
.tableHeader {
  padding: 0px;
  line-height: 15px;
  height: 100px !important;
}

/deep/ .el-main {
  background-color: #e9eef3 !important;
  padding: 0 !important;
  color: #333;
  /* text-align: center; */
  line-height: 20px !important;
}

.top-container {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 15px 10px 0 10px;
}

.control-container {
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.bottom-container{
  display: flex;
  justify-content: flex-end;
  padding: 10px 0;
}
</style>