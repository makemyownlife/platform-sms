<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="listQuery.channelType" placeholder="渠道类型" class="filter-item">
        <el-option key="" label="所有" value="" />
        <el-option key="-1" label="支付宝" value="aliyun" />
        <el-option key="0" label="亿美" value="emay" />
        <el-option key="1" label="绿城" value="greencity" />
      </el-select>
      <el-input v-model="listQuery.channelAppkey" placeholder="appkey" style="width: 200px;" class="filter-item" />
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
      <el-table-column label="渠道编号" min-width="35" align="center">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="渠道类型" min-width="45" align="center">
        <template slot-scope="scope">
          {{ scope.row.channelType }}
        </template>
      </el-table-column>
      <el-table-column label="appkey" min-width="80" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.channelAppkey }}</span>
        </template>
      </el-table-column>
      <el-table-column label="appsecret" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.channelAppsecret }}
        </template>
      </el-table-column>
      <el-table-column label="请求主体" min-width="150" align="center">
        <template slot-scope="scope">
          {{ scope.row.channelDomain }}
        </template>
      </el-table-column>
      <el-table-column label="备用参数" min-width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.extProperties }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="修改时间" min-width="80" align="center">
        <template slot-scope="scope">
          {{ scope.row.updateTime }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="操作" min-width="120" align="center">
        <template slot-scope="scope">
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="count>0" :total="count" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="fetchData()" />

  </div>

</template>

<script>

import { addNodeServer, getNodeServers, updateNodeServer, deleteNodeServer, startNodeServer, stopNodeServer } from '@/api/nodeServer'

import { getSmsChannels } from '@/api/smsChannel'

import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
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
      instanceList: null,
      listLoading: true,
      listLoading2: true,
      serverIdTmp: null,
      smsChannels: [],
      count: 0,
      listQuery: {
        name: '',
        channelAppkey: '',
        page: 1,
        size: 20
      },
      dialogFormVisible: false,
      dialogInstances: false,
      textMap: {
        create: '新建渠道',
        update: '修改渠道'
      },
      nodeModel: {
        id: undefined,
        clusterId: null,
        name: null,
        ip: null,
        adminPort: 11110,
        tcpPort: 11111,
        metricPort: 11112
      },
      rules: {
        name: [{ required: true, message: 'Server 名称不能为空', trigger: 'change' }],
        ip: [{ required: true, message: 'Server IP不能为空', trigger: 'change' }],
        adminPort: [{ required: true, message: 'Server admin端口不能为空', trigger: 'change' }]
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
      getSmsChannels(this.listQuery).then(res => {
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
      this.nodeModel = {
        id: undefined,
        clusterId: null,
        name: null,
        ip: null,
        adminPort: null,
        tcpPort: null,
        metricPort: null
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
            addNodeServer(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateNodeServer(this.nodeModel).then(res => {
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
    handleConfig(row) {
      if (row.canalCluster !== null) {
        this.$message({ message: '集群模式Server不允许单独变更配置，请在集群配置变更', type: 'error' })
        return
      }
      this.$router.push('/canalServer/nodeServer/config?serverId=' + row.id)
    },
    handleUpdate(row) {
      this.resetModel()
      this.nodeModel = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('删除Server信息会导致节点服务停止', '确定删除Server信息', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteNodeServer(row.id).then((res) => {
          if (res.data === 'success') {
            this.fetchData()
            this.$message({
              message: '删除Server信息成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除Server信息失败',
              type: 'error'
            })
          }
        })
      })
    },
    handleStart(row) {
      if (row.status !== '0') {
        this.$message({ message: '当前Server不是停止状态，无法启动', type: 'error' })
        return
      }
      this.$confirm('启动Server服务', '确定启动Server服务', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        startNodeServer(row.id).then((res) => {
          if (res.data) {
            this.fetchData()
            this.$message({
              message: '启动成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '启动Server服务出现异常',
              type: 'error'
            })
          }
        })
      })
    },
    handleStop(row) {
      if (row.status !== '1') {
        this.$message({ message: '当前Server不是启动状态，无法停止', type: 'error' })
        return
      }
      this.$confirm('停止Server服务会导致所有Instance都停止服务', '确定停止Server服务', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        stopNodeServer(row.id).then((res) => {
          if (res.data) {
            this.fetchData()
            this.$message({
              message: '停止成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '停止Server服务出现异常',
              type: 'error'
            })
          }
        })
      })
    },
    handleLog(row) {
      this.$router.push('nodeServer/log?id=' + row.id)
    },
    handleStartInstance(row) {
      if (row.runningStatus !== '0') {
        this.$message({ message: '当前Instance不是停止状态，无法启动', type: 'error' })
        return
      }
      this.$confirm('启动Instance服务', '确定启动Instance服务', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        startInstance(row.id, this.serverIdTmp).then((res) => {
          if (res.data) {
            this.activeInstances()
            this.$message({
              message: '启动成功, 稍后请刷新列表查看状态',
              type: 'success'
            })
          } else {
            this.$message({
              message: '启动Instance服务出现异常',
              type: 'error'
            })
          }
        })
      })
    },
    handleStopInstance(row) {
      if (row.runningStatus !== '1') {
        this.$message({ message: '当前Instance不是运行状态，无法停止', type: 'error' })
        return
      }
      this.$confirm('集群模式下停止实例其它主机将会抢占执行该实例', '停止 Instance 服务', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        stopInstance(row.id, this.serverIdTmp).then((res) => {
          if (res.data) {
            this.activeInstances()
            this.$message({
              message: '停止成功, 稍后请刷新列表查看状态',
              type: 'success'
            })
          } else {
            this.$message({
              message: '停止Instance服务出现异常',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>
