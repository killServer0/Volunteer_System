<template>
    <div class="container">
        <el-alert title="检查好账户哦" type="success" :closable="false" />
        <el-row style="margin: 20px 0 0 20px;">
            充值金额:<el-input v-model="newMoney" placeholder="充值金额" style="width: 40%" clearable />
        </el-row>
        <div class="pay-type-content">
            <label>
                <div class="pay-type-item" :span="8">
                    <el-radio v-model="type" label="微信支付" />
                    <img src="@/assets/img/test/weixin.png" alt="微信支付">
                </div>
            </label>
            <label>
                <div class="pay-type-item" :span="8">
                    <el-radio v-model="type" label="支付宝支付" />
                    <img src="@/assets/img/test/zhifubao.png" alt="支付宝支付">
                </div>
            </label>
            <label>
                <div class="pay-type-item" :span="8">
                    <el-radio v-model="type" label="建设银行" />
                    <img src="@/assets/img/test/jianshe.png" alt="建设银行">
                </div>
            </label>
            <label>
                <div class="pay-type-item">
                    <el-radio v-model="type" label="农业银行" />
                    <img src="@/assets/img/test/nongye.png" alt="农业银行">
                </div>
            </label>
            <label>
                <div class="pay-type-item">
                    <el-radio v-model="type" label="中国银行" />
                    <img src="@/assets/img/test/zhongguo.png" alt="中国银行">
                </div>
            </label>
            <label>
                <div class="pay-type-item">
                    <el-radio v-model="type" label="交通银行" />
                    <img src="@/assets/img/test/jiaotong.png" alt="交通银行">
                </div>
            </label>
            <label>
                <div class="pay-type-item">
                    <el-radio v-model="type" label="民生银行" />
                    <img src="@/assets/img/test/minsheng.png" alt="民生银行">
                </div>
            </label>
            <label>
                <div class="pay-type-item">
                    <el-radio v-model="type" label="工商银行" />
                    <img src="@/assets/img/test/gongshang.png" alt="工商银行">
                </div>
            </label>
        </div>
        <div class="buton-content">
            <el-button @click="submitTap" type="primary">确认支付</el-button>
            <el-button @click="back">返回</el-button>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useStorage } from '@/utils/storage'
import { http } from '@/utils/http'

const router = useRouter()
const storage = useStorage()

const sessionTable = ref('')
const role = ref('')
const userId = ref('')
const type = ref('支付宝支付')
const newMoney = ref('100')
const user = reactive({})

onMounted(async () => {
    sessionTable.value = storage.get('sessionTable')
    role.value = storage.get('role')
    userId.value = storage.get('userId')

    try {
        const { data } = await http({
            url: `${sessionTable.value}/session`,
            method: 'get'
        })

        if (data && data.code === 0) {
            Object.assign(user, data.data)
        } else {
            ElMessage.error(data.msg)
        }
    } catch (error) {
        console.error('获取用户信息失败:', error)
        ElMessage.error('获取用户信息失败')
    }
})

const submitTap = async () => {
    if (!newMoney.value || newMoney.value <= 0) {
        ElMessage.error('充值金额必须大于0')
        return
    }

    try {
        await ElMessageBox.confirm('确定充值么?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })

        const { data } = await http({
            url: `${sessionTable.value}/update`,
            method: 'post',
            data: {
                id: userId.value,
                newMoney: Number(newMoney.value) + Number(user.newMoney)
            }
        })

        if (data && data.code === 0) {
            ElMessage({
                message: '充值成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                    router.replace({ path: '/center' })
                }
            })
        } else {
            ElMessage.error(data.msg)
        }
    } catch (error) {
        if (error !== 'cancel') {
            console.error('充值失败:', error)
            ElMessage.error('充值失败')
        }
    }
}

const back = () => {
    router.replace({ path: '/center' })
}
</script>

<style lang="scss" scoped>
.container {
    margin: 10px;
    font-size: 14px;

    span {
        width: 60px;
    }

    .top-content {
        display: flex;
        align-items: center;
        padding: 20px;
    }

    .price-content {
        display: flex;
        align-items: center;
        margin-top: 20px;
        padding-bottom: 20px;
        padding: 20px;
        border-bottom: 1px solid #eeeeee;
        font-size: 20px;
        font-weight: bold;
        color: red;
    }

    .pay-type-content {
        display: flex;
        align-items: center;
        margin-top: 20px;
        flex-wrap: wrap;

        span {
            width: 100px;
        }

        .pay-type-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            width: 300px;
            margin: 20px;
            border: 1px solid #eeeeee;
            padding: 20px;
        }
    }

    .buton-content {
        margin: 20px;
    }
}
</style>