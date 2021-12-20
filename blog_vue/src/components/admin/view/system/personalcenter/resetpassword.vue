<template>
  <el-form :model="editForm" ref="edForm" :rules="editFormRules">
    <el-form-item label="旧密码" prop="oldPassWord">
      <el-input v-model="editForm.oldPassWord"></el-input>
    </el-form-item>
    <el-form-item label="新密码" prop="newPassWord">
      <el-input v-model="editForm.newPassWord"></el-input>
    </el-form-item>
    <el-form-item label="再次输入新密码" prop="checkPassWord">
      <el-input v-model="editForm.checkPassWord"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button @click="submit">提交</el-button>
    </el-form-item>
  </el-form>


</template>

<script>
import {ElMessageBox, ElMessage} from 'element-plus';
import {updatePasswd} from "../../../../../request/api/common";

export default {
  name: "resetPassWord",
  data() {
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.editForm.newPassWord) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    }
    return {
      editForm: {},
      editFormRules: {
        oldPassWord: [{required: true, message: '请输入旧密码', trigger: 'blur'},
          {min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur'}],
        newPassWord: [{required: true, message: '请输入新密码', trigger: 'blur'}],
        checkPassWord: [{required: true, validator: validatePass, trigger: 'blur'}],
      }
    }
  },
  methods: {
    submit() {
      this.$refs.edForm.validate((val) => {
        if (val) {
          const _this = this
          updatePasswd(this.editForm).then(res => {
            _this.$alert(res.data.msg, '提示', {
              confirmButtonText: '确定',
              callback: action => {
                this.$refs.edForm.resetFields();
              }
            })
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    }
  }
}


</script>

<style>
/*body{*/
/*  height: 100%;*/
/*}*/
/*#app{*/
/*  height: 100%;*/
/*}*/
</style>