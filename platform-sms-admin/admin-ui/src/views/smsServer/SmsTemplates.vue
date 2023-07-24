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
      <el-table-column label="编号" min-width="33" align="center">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="模版名称" min-width="36" align="center">
        <template slot-scope="scope">
          {{ scope.row.templateName }}
        </template>
      </el-table-column>
      <el-table-column label="模版类型" min-width="36" align="center">
        <template slot-scope="scope">
          <span  v-if="scope.row.templateType === '0'">
            验证码
          </span>
          <span v-if="scope.row.templateType === '1'">
           短信通知
          </span>
        </template>
      </el-table-column>

      <el-table-column label="签名" min-width="23" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.signName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="模版内容" min-width="105" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.content }}</span>
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="渠道" align="center">
        <template slot-scope="scope">
          <el-table :data="scope.row.bindingList">
            <el-table-column property="channelName" label="渠道名"></el-table-column>
            <el-table-column  property="templateCode" label="模版编号"></el-table-column>
            <el-table-column property="status" label="状态" :formatter="bindingStatus">
            </el-table-column>
          </el-table>
          <!--
          <el-tag v-for="binding in scope.row.bindingList" :key="binding.id" effect="dark" type="">
             {{ binding.channelName }}
          </el-tag>
          -->
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="操作" min-width="22" align="center">
        <template slot-scope="scope">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              操作<i class="el-icon-arrow-down el-icon--right"/>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleUpdate(scope.row)">修改模版</el-dropdown-item>
              <el-dropdown-item @click.native="autoBinding(scope.row)">自动绑定</el-dropdown-item>
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
        <el-form-item label="模版类型" prop="templateType">
          <el-select v-model="templateModel.templateType" placeholder="模版类型" style="width: 280px">
            <el-option key="0" label="验证码" value="0" />
            <el-option key="1" label="短信通知" value="1"/>
          </el-select>
        </el-form-item>
        <el-form-item label="签名名称" prop="signName">
          <el-input v-model="templateModel.signName"/>
        </el-form-item>
        <el-form-item label="模版内容" prop="content" >
          <el-input v-model="templateModel.content"
                    type="textarea"
                    placeholder="例如：您的验证码为 ${code} ，该验证码5分钟内有效，请勿泄露于他人。"
                    :rows = "3"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="templateModel.remark" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dataOperation()">确定</el-button>
        <el-button @click="dialogFormVisible = false">取消</el-button>
      </div>
    </el-dialog>
    <!--  模版窗口 end   -->

    <!-- 绑定渠道模版窗口 start  -->
    <el-dialog :visible.sync="bindingInfo.dialogFormVisible" title="绑定渠道" width="580px">
      <el-form label-position="left" label-width="120px"
               style="width: 400px; margin-left:30px;">
        <el-form-item label="选择渠道" prop="channelIds">
          <el-select v-model="selectValue"
                     @change="currChannelChange"
                     filterable
                     placeholder="渠道类型"
                     clearable
                     style="width: 280px">
            <el-option v-for="item in smsChannels"
                       :key="item.id"
                       :label="item.channelName"
                       :value="item.id"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAutoBinding()">确定</el-button>
        <el-button @click="bindingInfo.dialogFormVisible = false; clearSelectedValue();">取消</el-button>
      </div>
    </el-dialog>
    <!--  绑定渠道模版窗口 end   -->

  </div>

</template>

<script>

import {getSmsTemplates, addSmsTemplate, deleteTemplate, updateSmsTemplate , autoBindChannel} from '@/api/template.js'
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
      // 当前选择的模版

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
        templateType: null,
        templateName: null,
        signName: null,
        content: null,
        channelIds: null,
        remark: null
      },
      rules: {
        templateName: [{required: true, message: '模版名称不能为空', trigger: 'change'}],
        templateType: [{required: true, message: '模版类型不能为空', trigger: 'change'}],
        signName: [{required: true, message: '签名名称不能为空', trigger: 'change'}],
        content: [{required: true, message: '内容不能为空', trigger: 'change'}],
        remark: [{required: true, message: '备注不能为空', trigger: 'change'}]
      },
      dialogStatus: 'create',

      // 绑定信息
      bindingInfo : {
        dialogFormVisible: false ,
        templateId : null ,
        channelIds : null
      }
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
        content: null
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
      var bindingCount = row.bindingList.length;
      if( bindingCount > 0) {
        this.$message({
          message: '模版已绑定渠道，无法修改',
          type: 'error'
        })
      } else {
          this.resetModel()
          this.templateModel = Object.assign({}, row)
          this.dialogStatus = 'update'
          this.dialogFormVisible = true
          this.$nextTick(() => {
            this.$refs['dataForm'].clearValidate()
          })
      }
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
    },
    // 渠道改变事件
    currChannelChange(val){
      this.bindingInfo.channelIds = val;
    },
    // 点击绑定渠道按钮
    autoBinding(row) {
      this.bindingInfo.dialogFormVisible = true;
      this.clearSelectedValue();
      this.bindingInfo.templateId = row.id;
      this.bindingInfo.channelIds = null;
      this.loadSelectChannel();
    },
    clearSelectedValue() {
      this.selectValue = null;
    },
    // 绑定渠道请求
    submitAutoBinding(){
      var data = {
        templateId: this.bindingInfo.templateId,
        channelIds: this.bindingInfo.channelIds
      }
      autoBindChannel(data).then(res => {
        this.$message({
        message: '绑定渠道成功',
        type: 'success'
      })
      this.fetchData();
      this.bindingInfo.dialogFormVisible = false;
    })
    },
    bindingStatus(row, column) {
      // Map the status integer to the corresponding string
      const statusMap = {
        0: '待提交',
        1: '待审核',
        2: '审核成功',
        3: '审核失败'
      };
      return statusMap[row.status];
    },
  }

}

</script>
