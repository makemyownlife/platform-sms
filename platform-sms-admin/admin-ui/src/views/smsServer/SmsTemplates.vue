<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.templateName" placeholder="模版名称" style="width: 200px;" class="filter-item"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建模版</el-button>
      <el-button class="filter-item" type="info" @click="fetchData()">刷新列表</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="模版编号" min-width="45" align="center">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="模版名称" min-width="45" align="center">
        <template slot-scope="scope">
          {{ scope.row.appName }}
        </template>
      </el-table-column>
      <el-table-column label="模版内容" min-width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.appKey }}</span>
        </template>
      </el-table-column>
      <el-table-column label="适配渠道" min-width="100" align="center">

      </el-table-column>
      <el-table-column label="状态" min-width="50" align="center">
        <template slot-scope="scope">
          <font v-if="scope.row.status === 0" color="green">正常</font>
          <font v-if="scope.row.status === 1" color="gray">失效</font>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="修改时间" min-width="62" align="center">
        <template slot-scope="scope">
          {{ scope.row.updateTime }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="操作" min-width="65" align="center">
        <template slot-scope="scope">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              操作<i class="el-icon-arrow-down el-icon--right"/>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleUpdate(scope.row)">修改</el-dropdown-item>
              <el-dropdown-item @click.native="handleDelete(scope.row)">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!--   模态窗口 start  -->
    <el-dialog :visible.sync="dialogFormVisible" :title="textMap[dialogStatus]" width="580px">
      <el-form ref="dataForm" :rules="rules" :model="appInfoModel" label-position="left" label-width="120px"
               style="width: 400px; margin-left:30px;">
        <el-form-item label="应用名称" prop="appName">
          <el-input v-model="appInfoModel.appName"/>
        </el-form-item>
        <el-form-item label="应用 key" prop="appkey">
          <el-input v-model="appInfoModel.appKey"/>
        </el-form-item>
        <el-form-item label="应用密钥" prop="appsecret">
          <el-input v-model="appInfoModel.appSecret"/>
        </el-form-item>
        <el-form-item label="备注" prop="extProperties">
          <el-input v-model="appInfoModel.remark" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dataOperation()">确定</el-button>
      </div>
    </el-dialog>
    <!--    模态窗口 end   -->
  </div>

</template>

<script>

import {getAppList, addAppInfo, updateAppInfo, deleteAppInfo} from '@/api/appInfo.js'

const crypto = require('crypto');

// Define function to generate MD5 hash
function generateMD5Hash(data) {
  // Create hash object
  const hash = crypto.createHash('md5');

  // Update hash object with data
  hash.update(data);

  // Generate hash digest
  const digest = hash.digest('hex');

  // Return hash digest
  return digest;
}


export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        '1': 'success',
        '0': 'gray',
        '-1': 'danger'
      }
      return statusMap[status]
    },
    statusLabel(status) {
      const statusMap = {
        '1': '启动',
        '0': '停止',
        '-1': '断开'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      listLoading: true,
      listLoading2: true,
      serverIdTmp: null,
      smsChannels: [],
      count: 0,
      listQuery: {
        templateName: '',
        page: 1,
        size: 50
      },
      dialogFormVisible: false,
      dialogInstances: false,
      textMap: {
        create: '新建应用',
        update: '修改应用'
      },
      appInfoModel: {
        id: undefined,
        appKey: null,
        appName: null,
        appSecret: null,
        remark: null,
        updateTime: null
      },
      rules: {
        appName: [{required: true, message: '应用名称不能为空', trigger: 'change'}],
        appKey: [{required: true, message: '应用key不能为空', trigger: 'change'}],
        appSecret: [{required: true, message: '应用密钥不能为空', trigger: 'change'}]
      },
      dialogStatus: 'create'
    }
  },
  // { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'change' }
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getAppList(this.listQuery).then(res => {
        this.list = res.data.items
        this.count = res.data.count
      }).finally(() => {
        this.listLoading = false
      })
    },
    queryData() {
      this.listQuery.page = 1
      this.fetchData()
    },
    resetModel() {
      this.appInfoModel = {
        id: undefined,
        appKey: null,
        appName: null,
        appSecret: null,
        remark: null,
        updateTime: null
      }
    },
    handleCreate() {
      this.resetModel()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.appInfoModel.appKey = generateAppKey();
      this.appInfoModel.appSecret = generateMD5Hash(generateAppKey() + '1235')
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    dataOperation() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.dialogStatus === 'create') {
            addAppInfo(this.appInfoModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateAppInfo(this.appInfoModel).then(res => {
              this.operationRes(res)
            })
          }
        }
      })
    },
    handleUpdate(row) {
      this.resetModel()
      this.appInfoModel = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('删除应用后无法使用', '确定删除应用信息', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAppInfo(row.id).then((res) => {
          if (res.data === 'success') {
            this.fetchData()
            this.$message({
              message: '删除应用信息成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除应用信息失败',
              type: 'error'
            })
          }
        })
      })
    },
    operationRes(res) {
      if (res.data === 'success') {
        this.fetchData()
        this.dialogFormVisible = false
        this.$message({
          message: this.textMap[this.dialogStatus] + '成功',
          type: 'success'
        })
      } else {
        this.$message({
          message: this.textMap[this.dialogStatus] + '失败',
          type: 'error'
        })
      }
    },
  }
}

function generateAppKey() {
  const chars = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  let appKey = '';
  for (let i = 0; i < 16; i++) {
    appKey += chars.charAt(Math.floor(Math.random() * chars.length));
  }
  return appKey;
}

</script>
