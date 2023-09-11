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
          {{ scope.row.content }}
        </template>
      </el-table-column>
      <el-table-column label="三方短信编号" min-width="120" align="center">
        <template slot-scope="scope">
          {{ scope.row.msgid }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <font v-if="scope.row.sendStatus === -1" color="gray">待发送</font>
          <font v-if="scope.row.sendStatus === 0" color="green">成功</font>
          <font v-if="scope.row.sendStatus === 1" color="red">失败</font>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="创建时间" min-width="75" align="center">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="count>0" :total="count" :page.sync="listQuery.page" :limit.sync="listQuery.size"
                @pagination="fetchData()"/>

    <!--   模态窗口 start  -->
    <el-dialog :visible.sync="dialogFormVisible" :title="textMap[dialogStatus]" width="580px">
      <el-form ref="dataForm"
               :rules="rules"
               :model="sendModel"
               label-position="left"
               label-width="120px"
               style="width: 400px; margin-left:30px;">
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="sendModel.mobile"/>
        </el-form-item>
        <el-form-item label="选择模版" prop="templateId">
          <el-select v-model="sendModel.templateId"
                     remote
                     :remote-method="queryTemplate"
                     @change='createDynamicTemplateParam'
                     filterable
                     placeholder=""
                     clearable
                     style="width: 280px">
            <el-option v-for="item in templateList"
                       :key="item.id"
                       :content="item.content"
                       :label="item.templateName"
                       :value="item.id"/>
          </el-select>
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

import {getSmsRecords, addSmsRecord} from '@/api/smsRecord'
import {getSmsTemplates} from '@/api/template'
import Pagination from '@/components/Pagination'

export default {
  components: {Pagination},
  filters: {},
  data() {
    return {
      dialogStatus: null,
      list: null,
      listLoading: true,
      count: 0,
      templateList: [],
      // 模版参数
      paramArray: [],
      listQuery: {
        mobile: '',
        page: 1,
        size: 50
      },
      dialogFormVisible: false,
      sendModel: {
        templateId: null,
        mobile: null,
        templateParam: null
      },
      textMap: {
        create: '发送短信'
      },
      rules: {
        mobile: [{required: true, message: '手机号不能为空', trigger: 'change'}],
        templateId: [{required: true, message: '模版不能为空', trigger: 'change'}],
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
      this.sendModel.mobile = null;
      this.sendModel.templateId = null;
      this.sendModel.templateParam = null;
    },
    handleCreate() {
      this.resetModel()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
      // 加载模版列表第一页
      this.queryTemplate('')
    },
    dataOperation() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.dialogStatus === 'create') {
            addSmsRecord(this.sendModel).then(res => {
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
    queryTemplate(query) {
      var param = {
        templateName: query,
        page: 1,
        size: 10
      }
      getSmsTemplates(param).then(res => {
        this.templateList = res.data.items;
      }).finally(() => {

      });
    },
    //解析模版内容
    parsePlaceholders(str) {
      const regex = /\$\{([^}]+)\}/g;
      const placeholders = [];
      let match;
      while ((match = regex.exec(str)) !== null) {
        placeholders.push(match[1]);
      }
      return placeholders;
    },
    createDynamicTemplateParam( value , e ) {
      var templateId = this.sendModel.templateId;
      console.log("templateId:" + templateId);
      // 从自定义属性获取模版值
      const selectedTemplate = this.templateList.find(item => item.id === templateId);
      if (selectedTemplate) {
        // 获取自定义属性 content 的值
        const contentValue = selectedTemplate.content;
        // 现在您可以使用 contentValue 做其他操作
        const placeholders = this.parsePlaceholders(contentValue);
        console.log(placeholders);
      }
    }

  }
}
</script>
