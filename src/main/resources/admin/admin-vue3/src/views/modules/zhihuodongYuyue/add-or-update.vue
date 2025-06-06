<template>
    <div class="addEdit-block">
        <el-dialog :title="!dataForm.id ? '新增' : '修改'" 
            :visible="visible" 
            @update:visible="$emit('update:visible', $event)"
            width="50%" 
            :close-on-click-modal="false" 
            :close-on-press-escape="false" 
            :destroy-on-close="true">
            <el-form :model="dataForm" :rules="dataRule" ref="dataFormRef" label-width="100px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="活动名称" prop="zhihuodongId">
                            <el-select v-model="dataForm.zhihuodongId" placeholder="请选择活动" 
                                @change="zhihuodongChange" style="width: 100%">
                                <el-option v-for="item in zhihuodongList" :key="item.id" 
                                    :label="item.zhihuodongName" :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="志愿者" prop="zhiyuanzheId">
                            <el-select v-model="dataForm.zhiyuanzheId" placeholder="请选择志愿者"
                                @change="zhiyuanzheChange" style="width: 100%">
                                <el-option v-for="item in zhiyuanzheList" :key="item.id" 
                                    :label="item.zhiyuanzheName" :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="活动编号">
                            <el-input v-model="zhihuodongForm.zhihuodongUuidNumber" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="活动照片">
                            <el-image v-if="zhihuodongForm.zhihuodongPhoto"
                                :src="getImageUrl(zhihuodongForm.zhihuodongPhoto)"
                                style="width: 100px; height: 100px" fit="cover">
                                <template #error>
                                    <div class="image-slot">
                                        <el-icon><Picture /></el-icon>
                                    </div>
                                </template>
                            </el-image>
                            <span v-else>无图片</span>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="活动类型">
                            <el-input v-model="zhihuodongForm.zhihuodongValue" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="活动地点">
                            <el-input v-model="zhihuodongForm.zhihuodongAddress" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="志愿者姓名">
                            <el-input v-model="zhiyuanzheForm.zhiyuanzheName" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="志愿者头像">
                            <el-image v-if="zhiyuanzheForm.zhiyuanzhePhoto"
                                :src="getImageUrl(zhiyuanzheForm.zhiyuanzhePhoto)"
                                style="width: 100px; height: 100px" fit="cover">
                                <template #error>
                                    <div class="image-slot">
                                        <el-icon><Picture /></el-icon>
                                    </div>
                                </template>
                            </el-image>
                            <span v-else>无图片</span>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="志愿者手机号">
                            <el-input v-model="zhiyuanzheForm.zhiyuanzhePhone" disabled></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="志愿者邮箱">
                            <el-input v-model="zhiyuanzheForm.zhiyuanzheEmail" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="报名原因" prop="zhihuodongYuyueText">
                    <el-input type="textarea" v-model="dataForm.zhihuodongYuyueText" 
                        placeholder="请输入报名原因" :rows="4"></el-input>
                </el-form-item>

                <el-form-item label="报名状态" prop="zhihuodongYuyueYesnoTypes">
                    <el-select v-model="dataForm.zhihuodongYuyueYesnoTypes" 
                        placeholder="请选择报名状态" style="width: 100%">
                        <el-option v-for="(item, index) in zhihuodongYuyueYesnoTypesOptions" 
                            :key="index" :label="item.indexName" :value="item.codeIndex">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="审核回复" prop="zhihuodongYuyueYesnoText">
                    <el-input type="textarea" v-model="dataForm.zhihuodongYuyueYesnoText" 
                        placeholder="请输入审核回复" :rows="4"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="visible = false">取 消</el-button>
                    <el-button type="primary" @click="dataFormSubmit">确 定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Picture } from '@element-plus/icons-vue'
import { getToken } from '@/utils/auth'
import config from '@/config'
import http from '@/utils/http'

const props = defineProps({
    visible: {
        type: Boolean,
        default: false
    }
})

const emit = defineEmits(['update:visible', 'refreshDataList'])

const baseUrl = config.baseUrl
const dataFormRef = ref(null)

// 表单数据
const dataForm = reactive({
    id: '',
    zhihuodongId: '',
    zhiyuanzheId: '',
    zhihuodongYuyueText: '',
    zhihuodongYuyueYesnoTypes: '',
    zhihuodongYuyueYesnoText: ''
})

// 活动表单数据
const zhihuodongForm = reactive({
    zhihuodongUuidNumber: '',
    zhihuodongPhoto: '',
    zhihuodongValue: '',
    zhihuodongAddress: ''
})

// 志愿者表单数据
const zhiyuanzheForm = reactive({
    zhiyuanzheName: '',
    zhiyuanzhePhoto: '',
    zhiyuanzhePhone: '',
    zhiyuanzheEmail: ''
})

// 表单验证规则
const dataRule = {
    zhihuodongId: [
        { required: true, message: '请选择活动', trigger: 'change' }
    ],
    zhiyuanzheId: [
        { required: true, message: '请选择志愿者', trigger: 'change' }
    ],
    zhihuodongYuyueText: [
        { required: true, message: '报名原因不能为空', trigger: 'blur' },
        { min: 10, message: '报名原因不能少于10个字符', trigger: 'blur' }
    ],
    zhihuodongYuyueYesnoTypes: [
        { required: true, message: '请选择报名状态', trigger: 'change' }
    ]
}

// 选项数据
const zhihuodongList = ref([])
const zhiyuanzheList = ref([])
const zhihuodongYuyueYesnoTypesOptions = ref([])

// 获取图片URL
const getImageUrl = (path) => {
    if (!path) return ''
    return path.startsWith('http') ? path : `${baseUrl}${path}`
}

// 获取活动列表
const getZhihuodongList = async () => {
    try {
        const res = await http.get('/zhihuodong/list')
        if (res.code === 0) {
            zhihuodongList.value = res.data
        }
    } catch (error) {
        console.error('获取活动列表失败:', error)
        ElMessage.error('获取活动列表失败')
    }
}

// 获取志愿者列表
const getZhiyuanzheList = async () => {
    try {
        const res = await http.get('/zhiyuanzhe/list')
        if (res.code === 0) {
            zhiyuanzheList.value = res.data
        }
    } catch (error) {
        console.error('获取志愿者列表失败:', error)
        ElMessage.error('获取志愿者列表失败')
    }
}

// 获取报名状态选项
const getZhihuodongYuyueYesnoTypesOptions = async () => {
    try {
        const res = await http.get('dictionary/page', {
            params: {
                dicCode: 'zhihuodong_yuyue_yesno_types'
            }
        })
        if (res.code === 0) {
            zhihuodongYuyueYesnoTypesOptions.value = res.data.list
            console.log('报名状态选项:', zhihuodongYuyueYesnoTypesOptions.value)
        }
    } catch (error) {
        console.error('获取报名状态选项失败:', error)
        ElMessage.error('获取报名状态选项失败')
    }
}

// 活动选择变更
const zhihuodongChange = async (val) => {
    try {
        const res = await http.get(`/zhihuodong/info/${val}`)
        if (res.code === 0) {
            Object.assign(zhihuodongForm, res.data)
        }
    } catch (error) {
        console.error('获取活动信息失败:', error)
        ElMessage.error('获取活动信息失败')
    }
}

// 志愿者选择变更
const zhiyuanzheChange = async (val) => {
    try {
        const res = await http.get(`/zhiyuanzhe/info/${val}`)
        if (res.code === 0) {
            Object.assign(zhiyuanzheForm, res.data)
        }
    } catch (error) {
        console.error('获取志愿者信息失败:', error)
        ElMessage.error('获取志愿者信息失败')
    }
}

// 初始化
const init = async (id) => {
    dataForm.id = id || ''
    
    // 重置表单
    if (dataFormRef.value) {
        dataFormRef.value.resetFields()
    }

    // 获取选项数据
    await getZhihuodongList()
    await getZhiyuanzheList()
    await getZhihuodongYuyueYesnoTypesOptions()

    // 如果是修改,获取详情
    if (id) {
    try {
            const res = await http.get(`/zhihuodongYuyue/info/${id}`)
            if (res.code === 0) {
                console.log('获取到的详情数据:', res.data)
                Object.assign(dataForm, res.data)
            // 获取活动和志愿者信息
                await zhihuodongChange(dataForm.zhihuodongId)
                await zhiyuanzheChange(dataForm.zhiyuanzheId)
        }
    } catch (error) {
            console.error('获取详情失败:', error)
            ElMessage.error('获取详情失败')
        }
    }
}

// 表单提交
const dataFormSubmit = async () => {
    if (!dataFormRef.value) return
    
    await dataFormRef.value.validate(async (valid) => {
        if (valid) {
            try {
                const url = dataForm.id ? '/zhihuodongYuyue/update' : '/zhihuodongYuyue/add'
                const res = await http.post(url, dataForm)
                if (res.code === 0) {
                    ElMessage.success('操作成功')
                    emit('update:visible', false)
                    emit('refreshDataList')
                } else {
                    ElMessage.error(res.msg)
                }
            } catch (error) {
                console.error('提交失败:', error)
                ElMessage.error('提交失败')
            }
        }
    })
}

defineExpose({
    init
})
</script>

<style scoped>
.addEdit-block {
    margin: -20px;
}

.dialog-footer {
    text-align: right;
    margin-top: 20px;
}

.image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #909399;
    font-size: 30px;
}

.el-image {
    border-radius: 4px;
}
</style>