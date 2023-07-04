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
      <el-table-column label="编号" min-width="35" align="center">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="模版名称" min-width="45" align="center">
        <template slot-scope="scope">
          {{ scope.row.templateName }}
        </template>
      </el-table-column>
      <el-table-column label="模版内容" min-width="125" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.content }}</span>
        </template>
      </el-table-column>
      <el-table-column label="签名" min-width="45" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.signName }}</span>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="绑定渠道" min-width="50" align="center">
        <template slot-scope="scope">
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="操作" min-width="50" align="center">
        <template slot-scope="scope">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              操作<i class="el-icon-arrow-down el-icon--right"/>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleUpdate(scope.row)">修改模版</el-dropdown-item>
              <el-dropdown-item @click.native="handleDelete(scope.row)">删除模版</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="count>0" :total="count" :page.sync="listQuery.page" :limit.sync="listQuery.size"
                @pagination="fetchData()"/>

    <!--   模版窗口 start  -->
    <el-dialog :visible.sync="dialogFormVisible" :title="textMap[dialogStatus]" width="580px">
      <el-form ref="dataForm" :rules="rules" :model="templateModel" label-position="left" label-width="120px"
               style="width: 400px; margin-left:30px;">
        <el-form-item label="模版名称" prop="templateName">
          <el-input v-model="templateModel.templateName"/>
        </el-form-item>
        <el-form-item label="签名名称" prop="signName">
          <el-input v-model="templateModel.signName"/>
        </el-form-item>
        <el-form-item label="模版内容" prop="content" >
          <el-input v-model="templateModel.content"
                    type="textarea"
                    placeholder="例如：您的验证码为 ${code} ，该验证码5分钟内有效，请勿泄露于他人。"
                    :rows = "4"
          />
        </el-form-item>
        <el-form-item label="选择渠道" prop="channelIds">
          <el-select v-model="selectValue"
                     filterable multiple
                     placeholder="渠道类型"
                     clearable
                     style="width: 280px">
            <el-option v-for="item in smsChannels" :key="item.id" :label="item.channelName" :value="item.id"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dataOperation()">确定</el-button>
      </div>
    </el-dialog>
    <!--    模版窗口 end   -->

  </div>

</template>

<script>

import {getSmsTemplates, addSmsTemplate, deleteTemplate, updateSmsTemplate} from '@/api/template.js'
import {getSmsChannels} from '@/api/smsChannel.js'
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  filters: {
  },
  data() {
    return {
      list: null,
      listLoading: true,
      listLoading2: true,
      // 渠道属性 start
      selectValue: '',
      smsChannels: [],
      // 渠道属性 end
      count: 0,
      listQuery: {
        templateName: '',
        page: 1,
        size: 50
      },
      dialogFormVisible: false,
      selectedItem: "",
      textMap: {
        create: '新建模版',
        update: '修改模版'
      },
      templateModel: {
        id: undefined,
        templateName: null,
        signName: null,
        content: null,
        channelIds: null
      },
      rules: {
        templateName: [{required: true, message: '模版名称不能为空', trigger: 'change'}],
        signName: [{required: true, message: '签名名称不能为空', trigger: 'change'}],
        content: [{required: true, message: '内容不能为空', trigger: 'change'}],
        channelIds: [{required: true, message: '模版渠道不能为空', trigger: 'change'}]
      },
      dialogStatus: 'create'
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    querySearch(queryString, cb) {
      let options = this.options;
      let results = queryString
        ? options.filter(this.createFilter(queryString))
        : options;
      cb(results);
    },
    createFilter(queryString) {
      return (option) => {
        return option.value.toLowerCase().indexOf(queryString.toLowerCase()) >=
          0;
      };
    },
    fetchData() {
      this.listLoading = true
      getSmsTemplates(this.listQuery).then(res => {
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
      this.templateModel = {
        id: undefined,
        templateName: null,
        signName: null,
        content: null,
        channelIds: null
      }
    },
    handleCreate() {
      this.resetModel()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      //加载渠道
      this.loadSelectChannel()
    },
    dataOperation() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.dialogStatus === 'create') {
            addSmsTemplate(this.templateModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateSmsTemplate(this.templateModel).then(res => {
              this.operationRes(res)
            })
          }
        }
      })
    },
    handleUpdate(row) {
      this.resetModel()
      this.templateModel = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('删除模版信息后模版无法使用', '确定删除模版信息', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTemplate(row.id).then((res) => {
          if (res.data === 'success') {
            this.fetchData()
            this.$message({
              message: '删除模版信息成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除模版信息失败',
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
    loadSelectChannel() {
        getSmsChannels(null).then(res => {
          this.smsChannels = res.data.items;
         }).finally(() => {
         });
    }
  }
}

</script>
