<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.mobile" placeholder="请输入手机号" style="width: 200px;" class="filter-item"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">发送短信</el-button>
      <el-button class="filter-item" type="info" @click="fetchData()">刷新列表</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      element-loading-text="Loading"
      :data="list"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="渠道名称" min-width="50" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.channelName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号" min-width="48" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.mobile }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应用名" min-width="50" align="center">
        <template slot-scope="scope">
          {{ scope.row.appName }}
        </template>
      </el-table-column>
      <el-table-column label="内容" min-width="120" align="center">
        <template slot-scope="scope">
          {{ scope.row.channelDomain }}
        </template>
      </el-table-column>
      <el-table-column label="三方短信编号" min-width="120" align="center">
        <template slot-scope="scope">
          {{ scope.row.msgid }}
        </template>
      </el-table-column>
      <el-table-column label="状态"  align="center">

      </el-table-column>
      <el-table-column class-name="status-col" label="创建时间" min-width="75" align="center">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
    </el-table>

  </div>

</template>

<script>

import {getSmsRecords, addSmsRecord} from '@/api/smsRecord'

export default {
  filters: {
  },
  data() {
    return {
      list: null,
      listLoading: true,
      smsChannels: [],
      count: 0,
      listQuery: {
        mobile: '',
        page: 1,
        size: 50
      },
      dialogFormVisible: false,
      textMap: {
        create: '发送短信'
      },
      rules: {
        mobile: [{required: true, message: '手机号不能为空', trigger: 'change'}]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getSmsRecords(this.listQuery).then(res => {
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
    },
    handleCreate() {
      this.resetModel()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      // this.$nextTick(() => {
      //   this.$refs['dataForm'].clearValidate()
      // })
    },
    dataOperation() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.dialogStatus === 'create') {
            addSmsChannel(this.channelModel).then(res => {
              this.operationRes(res)
            })
          }
        }
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
