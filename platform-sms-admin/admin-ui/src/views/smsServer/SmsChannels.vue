<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="listQuery.channelType" placeholder="渠道类型" class="filter-item">
        <el-option key="" label="所有" value=""/>
        <el-option v-for="item in channelTypes"
                   :domain="item.domain"
                   :label="item.channelName"
                   :value="item.channelType"/>
      </el-select>
      <el-input v-model="listQuery.channelAppkey" placeholder="短信渠道appkey" style="width: 200px;"
                class="filter-item"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建渠道</el-button>
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
      <el-table-column label="渠道编号" min-width="45" align="center">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="渠道类型" min-width="40" align="center">
        <template slot-scope="scope">
          {{ scope.row.channelType }}
        </template>
      </el-table-column>
      <el-table-column label="渠道名称" min-width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.channelName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="appkey" min-width="90" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.channelAppkey }}</span>
        </template>
      </el-table-column>
      <el-table-column label="appsecret" min-width="90" align="center">
        <template slot-scope="scope">
          {{ scope.row.channelAppsecret }}
        </template>
      </el-table-column>
      <el-table-column label="请求主体" min-width="120" align="center">
        <template slot-scope="scope">
          {{ scope.row.channelDomain }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="修改时间" min-width="75" align="center">
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
        <el-form-item label="渠道类型" prop="channelType">
          <el-select v-model="channelModel.channelType"
                     @change='renderChannelModel'
                     style="width: 280px">
            <el-option key="" label="请选择" value=""/>
            <el-option v-for="item in channelTypes"
                       :domain="item.domain"
                       :label="item.channelName"
                       :value="item.channelType"/>
          </el-select>
        </el-form-item>
        <el-form-item label="渠道名称" prop="channelName">
          <el-input v-model="channelModel.channelName"/>
        </el-form-item>
        <el-form-item label="appkey" prop="channelAppkey">
          <el-input v-model="channelModel.channelAppkey"/>
        </el-form-item>
        <el-form-item label="appsecret" prop="channelAppsecret">
          <el-input v-model="channelModel.channelAppsecret"/>
        </el-form-item>
        <el-form-item label="请求地址" prop="channelDomain">
          <el-input v-model="channelModel.channelDomain"/>
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

import {getSmsChannels, addSmsChannel, deleteSmsChannel, updateSmsChannel, channelDict} from '@/api/smsChannel'

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
      smsChannels: [],
      count: 0,
      listQuery: {
        channelType: '',
        channelAppkey: '',
        page: 1,
        size: 50
      },
      dialogFormVisible: false,
      dialogInstances: false,
      textMap: {
        create: '新建渠道',
        update: '修改渠道'
      },
      channelTypes: [],
      channelModel: {
        id: undefined,
        channelType: '',
        channelName: '',
        channelAppkey: null,
        channelAppsecret: null,
        channelDomain: null,
        signName: null,
        extProperties: null
      },
      rules: {
        channelType: [{required: true, message: '渠道类型不能为空', trigger: 'change'}],
        channelAppkey: [{required: true, message: '渠道key不能为空', trigger: 'change'}],
        channelName: [{required: true, message: '渠道名称不能为空', trigger: 'change'}],
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
      //查询渠道字典表
      channelDict().then(res => {
        this.channelTypes = res.data;
      }).finally(() => {
      })
      //加载渠道列表
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
      this.$confirm('删除渠道信息后,密钥将无法使用', '确定删除渠道信息', {
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
    renderChannelModel(value, e) {
      var channelType = this.channelModel.channelType;
      console.log("channelType:" + channelType);
      // 从自定义属性获取模版值
      const selectedChannel = this.channelTypes.find(item => item.channelType === channelType);
      if (selectedChannel) {
        this.channelModel.channelDomain = selectedChannel.domain;
      }
    }
  }
}
</script>
