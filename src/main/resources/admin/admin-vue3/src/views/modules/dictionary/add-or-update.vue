<template>
  <div class="addOrUpdate-block">
    <el-form
      class="detail-form-content"
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      label-width="80px">
      <el-row>
        <el-col :span="12">
          <el-form-item class="input" v-if="type!='info'" label="字典编码" prop="dicCode">
            <el-select v-model="ruleForm.dicCode" placeholder="请选择字典编码" clearable>
              <el-option v-for="item in dictionaryTypes" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <div v-else>
            <el-form-item class="input" label="字典编码" prop="dicCode">
              <el-input v-model="ruleForm.dicCode" placeholder="字典编码" readonly></el-input>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="12">
          <el-form-item class="input" v-if="type!='info'" label="字典名称" prop="dicName">
            <el-input v-model="ruleForm.dicName" placeholder="字典名称" clearable></el-input>
          </el-form-item>
          <div v-else>
            <el-form-item class="input" label="字典名称" prop="dicName">
              <el-input v-model="ruleForm.dicName" placeholder="字典名称" readonly></el-input>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="12">
          <el-form-item class="input" v-if="type!='info'" label="编码" prop="codeIndex">
            <el-input v-model="ruleForm.codeIndex" placeholder="编码" clearable></el-input>
          </el-form-item>
          <div v-else>
            <el-form-item class="input" label="编码" prop="codeIndex">
              <el-input v-model="ruleForm.codeIndex" placeholder="编码" readonly></el-input>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="12">
          <el-form-item class="input" v-if="type!='info'" label="编码名称" prop="indexName">
            <el-input v-model="ruleForm.indexName" placeholder="编码名称" clearable></el-input>
          </el-form-item>
          <div v-else>
            <el-form-item class="input" label="编码名称" prop="indexName">
              <el-input v-model="ruleForm.indexName" placeholder="编码名称" readonly></el-input>
            </el-form-item>
          </div>
        </el-col>
        <el-col :span="24">
          <el-form-item class="textarea" v-if="type!='info'" label="备注" prop="beizhu">
            <el-input v-model="ruleForm.beizhu" type="textarea" placeholder="备注" clearable></el-input>
          </el-form-item>
          <div v-else>
            <el-form-item class="textarea" label="备注" prop="beizhu">
              <el-input v-model="ruleForm.beizhu" type="textarea" placeholder="备注" readonly></el-input>
            </el-form-item>
          </div>
        </el-col>
      </el-row>
      <el-form-item class="btn">
        <el-button v-if="type!='info'" type="primary" class="btn-success" @click="onSubmit">提交</el-button>
        <el-button v-if="type!='info'" class="btn-close" @click="back()">取消</el-button>
        <el-button v-if="type=='info'" class="btn-close" @click="back()">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getDictionaryInfo, addDictionary, updateDictionary, getDictionaryTypes } from '@/api/dictionary'

const props = defineProps({
  parent: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['refreshDataList'])

const ruleForm = reactive({
  id: '',
  dicCode: '',
  dicName: '',
  codeIndex: '',
  indexName: '',
  beizhu: ''
})

const dictionaryTypes = ref([])

const rules = {
  dicCode: [
    { required: true, message: '字典编码不能为空', trigger: 'blur' }
  ],
  dicName: [
    { required: true, message: '字典名称不能为空', trigger: 'blur' }
  ],
  codeIndex: [
    { required: true, message: '编码不能为空', trigger: 'blur' }
  ],
  indexName: [
    { required: true, message: '编码名称不能为空', trigger: 'blur' }
  ]
}

const type = ref('')
const ruleFormRef = ref(null)

// 获取字典类型
const fetchDictionaryTypes = async () => {
  try {
    const res = await getDictionaryTypes()
    if (res.code === 0 && res.data) {
      dictionaryTypes.value = res.data
    } else {
      // 如果API不可用，添加一个基础类型
      dictionaryTypes.value = ['sex_types']
    }
  } catch (error) {
    console.error('获取字典类型失败:', error)
    // 添加默认类型
    dictionaryTypes.value = ['sex_types']
  }
}

// 初始化
const init = async (id, typeParam) => {
  console.log('add-or-update初始化，id=', id, 'typeParam=', typeParam);
  
  await fetchDictionaryTypes();
  
  // 设置类型：info(详情)、update(编辑)或空(新增)
  if (typeParam) {
    type.value = typeParam;
  } else {
    type.value = id ? 'update' : '';
  }
  
  console.log('设置type.value=', type.value);
  
  // 设置ID
  ruleForm.id = id || '';
  
  // 如果是查看详情或编辑，加载数据
  if (id) {
    try {
      console.log('获取字典详情，id=', id);
      const res = await getDictionaryInfo(id);
      if (res.code === 0) {
        Object.assign(ruleForm, res.data);
        console.log('字典详情加载成功:', ruleForm);
      }
    } catch (error) {
      console.error('获取字典详情失败:', error);
      ElMessage.error('获取字典详情失败');
    }
  } else {
    // 新建的情况下，清空表单
    ruleForm.dicCode = '';
    ruleForm.dicName = '';
    ruleForm.codeIndex = '';
    ruleForm.indexName = '';
    ruleForm.beizhu = '';
    console.log('新建字典，表单已清空');
  }
}

// 提交
const onSubmit = async () => {
  try {
    await ruleFormRef.value.validate()
    
    // 输出调试信息
    console.log('准备提交字典数据:', JSON.stringify(ruleForm, null, 2))
    
    const api = ruleForm.id ? updateDictionary : addDictionary
    const res = await api(ruleForm)
    if (res.code === 0) {
      ElMessage.success(ruleForm.id ? '修改成功' : '新增成功')
      back()
      emit('refreshDataList')
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败，请检查表单信息')
  }
}

// 返回
const back = () => {
  console.log('调用back函数，尝试返回列表页面');
  console.log('父组件引用:', props.parent);
  
  // 确保父组件引用存在
  if (props.parent) {
    try {
      // 更新父组件状态
      if (typeof props.parent.showFlag !== 'undefined') {
        props.parent.showFlag = true;
        console.log('设置父组件showFlag=true');
      }
      
      if (typeof props.parent.addOrUpdateFlag !== 'undefined') {
        props.parent.addOrUpdateFlag = false;
        console.log('设置父组件addOrUpdateFlag=false');
      }
    } catch (err) {
      console.error('更新父组件状态时出错:', err);
    }
  } else {
    console.error('父组件引用不存在');
  }
}

defineExpose({
  init
})
</script>

<style scoped>
.addOrUpdate-block {
  margin: 20px;
}

.detail-form-content {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
}

.btn {
  margin-top: 20px;
  text-align: center;
}

.btn-success {
  margin-right: 10px;
}
</style>