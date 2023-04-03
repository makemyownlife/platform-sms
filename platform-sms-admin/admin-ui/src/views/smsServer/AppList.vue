<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.channelAppkey" placeholder="应用appkey" style="width: 200px;" class="filter-item"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建通道</el-button>
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
      <el-table-column label="应用编号" min-width="45" align="center">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="应用名称" min-width="45" align="center">
        <template slot-scope="scope">
          {{ scope.row.appName }}
        </template>
      </el-table-column>
      <el-table-column label="应用appkey" min-width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.appKey }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应用密钥" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.appSecret }}
        </template>
      </el-table-column>
      <el-table-column label="状态" min-width="50" align="center">
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
      <el-form ref="dataForm" :rules="rules" :model="channelModel" label-position="left" label-width="120px"
               style="width: 400px; margin-left:30px;">
        <el-form-item label="appkey" prop="channelAppkey">
          <el-input v-model="channelModel.channelAppkey"/>
        </el-form-item>
        <el-form-item label="appsecret" prop="channelAppsecret">
          <el-input v-model="channelModel.channelAppsecret"/>
        </el-form-item>
        <el-form-item label="请求地址" prop="channelDomain">
          <el-input v-model="channelModel.channelDomain"/>
        </el-form-item>
        <el-form-item label="签名名称" prop="signName">
          <el-input v-model="channelModel.signName"/>
        </el-form-item>
        <el-form-item label="附件属性" prop="extProperties">
          <el-input v-model="channelModel.extProperties" type="textarea"/>
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
        appkey: '',
        page: 1,
        size: 50
      },
      dialogFormVisible: false,
      dialogInstances: false,
      textMap: {
        create: '新建渠道',
        update: '修改渠道'
      },
      channelTypes: [
        {text: '阿里云', value: 'aliyun'},
        {text: '亿美', value: 'emay'}
      ],
      channelModel: {
        id: undefined,
        channelType: '',
        channelAppkey: null,
        channelAppsecret: null,
        channelDomain: null,
        signName: null,
        extProperties: null
      },
      rules: {
        channelType: [{required: true, message: '渠道类型不能为空', trigger: 'change'}],
        channelAppkey: [{required: true, message: '渠道key不能为空', trigger: 'change'}],
        channelDomain: [{required: true, message: '渠道访问地址不能为空', trigger: 'change'}],
        signName: [{required: true, message: '渠道签名不能为空', trigger: 'change'}],
        channelAppsecret: [{required: true, message: '渠道secret不能为空', trigger: 'change'}]
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
      this.channelModel = {
        id: undefined,
        channelType: '',
        channelAppkey: null,
        channelAppsecret: null,
        channelDomain: null,
        extProperties: null
      }
    },
    handleCreate() {
      this.resetModel()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    dataOperation() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.dialogStatus === 'create') {
            addSmsChannel(this.channelModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateSmsChannel(this.channelModel).then(res => {
              this.operationRes(res)
            })
          }
        }
      })
    },
    handleUpdate(row) {
      this.resetModel()
      this.channelModel = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('删除渠道信息密钥无法使用', '确定删除渠道信息', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteSmsChannel(row.id).then((res) => {
          if (res.data === 'success') {
            this.fetchData()
            this.$message({
              message: '删除渠道信息成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除渠道信息失败',
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
</script>
