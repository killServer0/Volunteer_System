<template>
    <div class="main-content">
        <!-- 帖子回复详情对话框 -->
        <el-dialog v-model="forumReplyDialogVisible" title="帖子回复详情" width="80%" :style="contents.dialogStyle">
            <el-dialog v-model="forumReplyInfoDialogVisible" width="30%" :title="forumReplyInfoContent" append-to-body
                :style="contents.dialogStyle" />
            <div class="demo-input-suffix" :style="contents.formStyle">
                <span style="width: 20%">帖子标题：</span>
                <el-input v-model="forumTitle" disabled placeholder="帖子标题" style="width: 80%"
                    :style="contents.inputStyle" />
            </div>
            <div class="demo-input-suffix" :style="contents.formStyle">
                <span style="width: 20%">帖子内容：</span>
                <span style="width: 80%; height: 20%" v-html="forumContent"></span>
            </div>
            <el-table :data="forumData" height="250" :style="contents.tableStyle" :header-row-style="headerRowStyle"
                :header-cell-style="headerCellStyle" :row-style="rowStyle" :cell-style="cellStyle">
                <el-table-column label="身份" :style="contents.tableHeaderStyle">
                    <template #default="{ row }">
                        <span v-if="row.zhiyuanzheId">志愿者</span>
                        <span v-if="row.feizhiyuanId">游客</span>
                        <span v-if="row.usersId">管理员</span>
                    </template>
                </el-table-column>
                <el-table-column label="姓名" :style="contents.tableHeaderStyle">
                    <template #default="{ row }">
                        <span v-if="row.zhiyuanzheId">{{ row.zhiyuanzheName }}</span>
                        <span v-if="row.feizhiyuanId">{{ row.feizhiyuanName }}</span>
                        <span v-if="row.usersId">管理员</span>
                    </template>
                </el-table-column>
                <el-table-column label="手机号" :style="contents.tableHeaderStyle">
                    <template #default="{ row }">
                        <span v-if="row.zhiyuanzheId">{{ row.zhiyuanzhePhone }}</span>
                        <span v-if="row.feizhiyuanId">{{ row.feizhiyuanPhone }}</span>
                        <span v-if="row.usersId">管理员</span>
                    </template>
                </el-table-column>
                <el-table-column label="回复内容" :style="contents.tableHeaderStyle">
                    <template #default="{ row }">
                        {{ row.forumContent.length > 20 ? row.forumContent.substring(0, 20) + '...' : row.forumContent }}
                    </template>
                </el-table-column>
                <el-table-column prop="insertTime" label="回帖时间" :style="contents.tableHeaderStyle" />
                <el-table-column label="操作" :style="contents.tableHeaderStyle">
                    <template #default="{ row }">
                        <el-button type="info" :style="contents.buttonStyle"
                            @click="seeForumContent(row.forumContent)">回帖详情</el-button>
                        <el-button v-if="canDelete(row)" type="danger" :icon="Delete" size="small"
                            :style="contents.buttonStyle" @click="deleteHandler(row.id)">删除帖子</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="demo-input-suffix" :style="contents.formStyle">
                <span>回帖内容：</span>
                <el-input v-model="forumReplyContent" placeholder="回帖内容" type="textarea" :style="contents.inputStyle" />
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="forumReplyDialogVisible = false" :style="contents.buttonStyle">取 消</el-button>
                    <el-button type="primary" @click="forumReply" :style="contents.buttonStyle">回 帖</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 条件查询 -->
        <div v-if="showFlag">
            <el-form :inline="true" :model="searchForm" class="form-content" :style="contents.formStyle">
                <el-row :gutter="20" class="slt">
                    <el-form-item label="帖子标题">
                        <el-input v-model="searchForm.forumName" placeholder="帖子标题" clearable
                            :style="contents.inputStyle">
                            <template #prefix>
                                <el-icon>
                                    <Search />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="游客姓名">
                        <el-input v-model="searchForm.feizhiyuanName" placeholder="游客姓名" clearable
                            :style="contents.inputStyle">
                            <template #prefix>
                                <el-icon>
                                    <Search />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="志愿者姓名">
                        <el-input v-model="searchForm.zhiyuanzheName" placeholder="志愿者姓名" clearable
                            :style="contents.inputStyle">
                            <template #prefix>
                                <el-icon>
                                    <Search />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="search" :style="contents.buttonStyle">
                            查询<el-icon class="el-icon--right">
                                <Search />
                            </el-icon>
                        </el-button>
                    </el-form-item>
                </el-row>
                <el-row class="ad">
                    <el-form-item>
                        <el-button v-if="isAuth('forum', '新增')" type="primary" :icon="Plus"
                            :style="contents.buttonStyle" @click="addOrUpdateHandler()">新增</el-button>
                        <el-button v-if="isAuth('forum', '报表')" type="primary" :icon="PieChart"
                            :style="contents.buttonStyle" @click="chartDialog()">报表</el-button>
                        <a v-if="isAuth('forum', '导入导出')" class="el-button el-button--primary"
                            style="text-decoration: none" :style="contents.buttonStyle"
                            href="http://localhost:8081/zhiyuanzheguanlixitong/upload/forumMuBan.xls">批量导入论坛数据模板</a>
                        <el-upload v-if="isAuth('forum', '导入导出')" style="display: inline-block" :action="getActionUrl"
                            :on-success="forumUploadSuccess" :on-error="forumUploadError" :show-file-list="false">
                            <el-button type="primary" :icon="Upload" :style="contents.buttonStyle">批量导入论坛数据</el-button>
                        </el-upload>
                        <el-button v-if="isAuth('forum', '导入导出')" type="primary" :icon="Download"
                            :style="contents.buttonStyle" @click="exportExcel">导出</el-button>
                        <el-button v-if="isAuth('forum', '打印')" type="primary" :icon="Printer"
                            :style="contents.buttonStyle" @click="print">打印</el-button>
                    </el-form-item>
                </el-row>
            </el-form>

            <!-- 表格 -->
            <div class="table-content">
                <el-table v-loading="dataListLoading" :data="dataList" border style="width: 100%"
                    @selection-change="selectionChangeHandler" :style="contents.tableStyle"
                    :header-row-style="headerRowStyle" :header-cell-style="headerCellStyle" :row-style="rowStyle"
                    :cell-style="cellStyle">
                    <el-table-column type="index" label="索引" width="50" :style="contents.tableHeaderStyle" />
                    <el-table-column label="身份" align="center" :style="contents.tableHeaderStyle">
                        <template #default="{ row }">
                            <span v-if="row.zhiyuanzheId">志愿者</span>
                            <span v-if="row.feizhiyuanId">游客</span>
                            <span v-if="row.usersId">管理员</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="姓名" align="center" :style="contents.tableHeaderStyle">
                        <template #default="{ row }">
                            <span v-if="row.zhiyuanzheId">{{ row.zhiyuanzheName }}</span>
                            <span v-if="row.feizhiyuanId">{{ row.feizhiyuanName }}</span>
                            <span v-if="row.usersId">管理员</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="手机号" align="center" :style="contents.tableHeaderStyle">
                        <template #default="{ row }">
                            <span v-if="row.zhiyuanzheId">{{ row.zhiyuanzhePhone }}</span>
                            <span v-if="row.feizhiyuanId">{{ row.feizhiyuanPhone }}</span>
                            <span v-if="row.usersId">管理员</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="forumName" label="帖子标题" align="center" :style="contents.tableHeaderStyle" />
                    <el-table-column prop="forumContent" label="帖子内容" align="center" :style="contents.tableHeaderStyle">
                        <template #default="{ row }">
                            {{ row.forumContent.length > 20 ? row.forumContent.substring(0, 20) + '...' : row.forumContent }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="insertTime" label="发帖时间" align="center" :style="contents.tableHeaderStyle" />
                    <el-table-column label="操作" align="center" width="150" :style="contents.tableHeaderStyle">
                        <template #default="{ row }">
                            <el-button type="primary" :icon="View" size="small" :style="contents.buttonStyle"
                                @click="addOrUpdateHandler(row.id)">查看</el-button>
                            <el-button v-if="canDelete(row)" type="danger" :icon="Delete" size="small"
                                :style="contents.buttonStyle" @click="deleteHandler(row.id)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <!-- 分页 -->
            <el-pagination v-model:current-page="pageIndex" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]"
                :total="totalPage" :layout="layouts" @size-change="sizeChangeHandle"
                @current-change="currentChangeHandle" :style="contents.paginationStyle" />
        </div>

        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdateRef" @refreshDataList="getDataList" />

        <!-- 打印对话框 -->
        <el-dialog v-model="printVisiable" title="打印" width="500px" :style="contents.dialogStyle">
            <el-form :model="printFrom" ref="printFrom" label-width="100px" :style="contents.formStyle">
                <el-form-item label="帖子标题" prop="name1">
                    <el-input v-model="printFrom.name1" :style="contents.inputStyle" disabled></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="printVisiable = false" :style="contents.buttonStyle">取 消</el-button>
                    <el-button type="primary" @click="print" :style="contents.buttonStyle">打 印</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 报表对话框 -->
        <el-dialog v-model="chartVisiable" title="报表" width="800px" :style="contents.dialogStyle">
            <div id="chartRef" style="width: 100%; height: 400px;"></div>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Plus, Search, Upload, Download, View, PieChart, Printer } from '@element-plus/icons-vue'
import { http } from '@/utils/http'
import AddOrUpdate from './add-or-update.vue'
import styleJs from '@/utils/style.js'
import { isAuth } from '@/utils/utils.js'
import { useRoute } from 'vue-router'

// 样式配置
const contents = ref({
    formStyle: {
        padding: '20px',
        backgroundColor: '#fff',
        borderRadius: '4px',
        boxShadow: '0 2px 12px 0 rgba(0, 0, 0, 0.1)'
    },
    inputStyle: {
        width: '200px'
    },
    buttonStyle: {
        marginRight: '10px'
    },
    tableStyle: {
        marginTop: '20px'
    },
    tableHeaderStyle: {
        backgroundColor: '#f5f7fa',
        color: '#606266',
        fontWeight: 'bold'
    },
    dialogStyle: {
        padding: '20px'
    },
    paginationStyle: {
        marginTop: '20px',
        textAlign: 'right'
    }
})
const layouts = ref('')

// 数据列表
const dataList = ref([])
const dataListLoading = ref(false)
const pageIndex = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
const showFlag = ref(true)

// 查询条件
const searchForm = reactive({
    forumName: '',
    feizhiyuanName: '',
    zhiyuanzheName: ''
})

// 帖子回复相关
const forumReplyDialogVisible = ref(false)
const forumReplyInfoDialogVisible = ref(false)
const forumReplyInfoContent = ref('')
const forumTitle = ref('')
const forumContent = ref('')
const forumData = ref([])
const forumReplyContent = ref('')

// 新增/修改弹窗
const addOrUpdateVisible = ref(false)
const addOrUpdateRef = ref(null)

// 获取数据列表
const getDataList = async () => {
    console.log('开始获取数据列表')
    dataListLoading.value = true
    try {
        const response = await http({
            url: 'forum/page',
            method: 'get',
            params: {
                page: pageIndex.value,
                limit: pageSize.value,
                ...searchForm
            }
        })

        console.log('获取数据列表响应:', response)

        if (response && response.code === 0) {
            dataList.value = response.data.list || []
            totalPage.value = response.data.total || 0
            console.log('数据列表设置完成:', {
                dataListLength: dataList.value.length,
                totalPage: totalPage.value
            })
        } else {
            console.error('获取数据列表失败: 响应数据为空')
            ElMessage.error(response.msg || '获取数据列表失败')
        }
    } catch (error) {
        console.error('获取数据列表请求失败:', error)
        ElMessage.error('获取数据列表失败')
    } finally {
        dataListLoading.value = false
    }
}

// 搜索
const search = () => {
    console.log('执行搜索操作')
    console.log('搜索条件:', searchForm)
    pageIndex.value = 1
    getDataList()
}

// 分页
const sizeChangeHandle = (val) => {
    console.log('每页条数改变:', val)
    pageSize.value = val
    pageIndex.value = 1
    getDataList()
}

const currentChangeHandle = (val) => {
    console.log('当前页改变:', val)
    pageIndex.value = val
    getDataList()
}

// 多选
const selectionChangeHandler = (val) => {
    dataListSelections.value = val
}

// 新增/修改
const addOrUpdateHandler = (id) => {
    console.log('打开新增/修改弹窗，ID:', id)
    addOrUpdateVisible.value = true
    nextTick(() => {
        console.log('nextTick 回调执行')
        if (addOrUpdateRef.value) {
            console.log('addOrUpdateRef 存在，准备初始化')
        if (id) {
            addOrUpdateRef.value.init(id)
        } else {
            addOrUpdateRef.value.init()
            }
        } else {
            console.error('addOrUpdateRef 不存在')
        }
    })
}

// 删除
const deleteHandler = async (id) => {
    console.log('准备删除记录，ID:', id)
        try {
        const response = await http({
                url: 'forum/delete',
                method: 'post',
                data: [id]
            })

        console.log('删除响应:', response)

        if (response && response.code === 0) {
                ElMessage({
                message: '删除成功',
                    type: 'success',
                    duration: 1500,
                    onClose: () => {
                        getDataList()
                    }
                })
            } else {
            console.error('删除失败:', response?.msg || '未知错误')
            ElMessage.error(response?.msg || '删除失败')
            }
        } catch (error) {
            console.error('删除请求失败:', error)
        ElMessage.error('删除失败，请稍后重试')
        }
}

// 查看回帖详情
const seeForumContent = (content) => {
    forumReplyInfoContent.value = content
    forumReplyInfoDialogVisible.value = true
}

// 回帖
const forumReply = async () => {
    if (!forumReplyContent.value) {
        ElMessage.warning('请输入回帖内容')
        return
    }

    try {
        const { data } = await http({
            url: 'forum/save',
            method: 'post',
            data: {
                forumContent: forumReplyContent.value,
                forumId: forumId.value
            }
        })

        if (data && data.code === 0) {
            ElMessage({
                message: '回帖成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                    forumReplyContent.value = ''
                    getForumData()
                }
            })
        } else {
            ElMessage.error(data.msg)
        }
    } catch (error) {
        console.error('回帖失败:', error)
        ElMessage.error('回帖失败')
    }
}

// 获取回帖列表
const getForumData = async () => {
    try {
        const { data } = await http({
            url: 'forum/page',
            method: 'get',
            params: {
                forumId: forumId.value
            }
        })

        if (data && data.code === 0) {
            forumData.value = data.data.list
        } else {
            ElMessage.error(data.msg)
        }
    } catch (error) {
        console.error('获取回帖列表失败:', error)
        ElMessage.error('获取回帖列表失败')
    }
}

// 判断是否可以删除
const canDelete = (row) => {
    // 简化删除权限判断，允许所有用户删除
    return true
}

// 导出Excel
const exportExcel = () => {
    // 实现导出Excel功能
    const json_fields = {
        '游客姓名': 'feizhiyuanName',
        '游客手机号': 'feizhiyuanPhone',
        '游客身份证号': 'feizhiyuanIdNumber',
        '游客头像': 'feizhiyuanPhoto',
        '游客邮箱': 'feizhiyuanEmail',
        '志愿者姓名': 'zhiyuanzheName',
        '志愿者手机号': 'zhiyuanzhePhone',
        '志愿者身份证号': 'zhiyuanzheIdNumber',
        '志愿者头像': 'zhiyuanzhePhoto',
        '志愿者邮箱': 'zhiyuanzheEmail',
        '儿童名': 'username',
        '角色': 'role',
        '新增时间': 'addtime',
        '帖子标题': "forumName",
        '父id': "superIds",
        '帖子状态': "forumStateValue",
        '发帖时间': "insertTime",
        '修改时间': "updateTime",
    }
    // 使用json2excel库导出Excel
    import('@/utils/Export2Excel').then(excel => {
        excel.export_json_to_excel({
            header: Object.keys(json_fields),
            data: dataList.value.map(item => {
                return Object.values(json_fields).map(key => item[key])
            }),
            filename: 'forum.xls',
            autoWidth: true,
            bookType: 'xls'
        })
    })
}

// 上传成功回调
const forumUploadSuccess = (response) => {
    if (response.code === 0) {
        ElMessage.success('导入成功')
        getDataList()
    } else {
        ElMessage.error(response.msg)
    }
}

// 上传失败回调
const forumUploadError = () => {
    ElMessage.error('导入失败')
}

// 表格样式
const headerRowStyle = () => {
    return {
        color: contents.value.tableHeaderFontColor,
        fontSize: contents.value.tableHeaderFontSize,
        backgroundColor: contents.value.tableHeaderBgColor
    }
}

const headerCellStyle = () => {
    return {
        color: contents.value.tableHeaderFontColor,
        fontSize: contents.value.tableHeaderFontSize,
        backgroundColor: contents.value.tableHeaderBgColor
    }
}

const rowStyle = () => {
    return {
        color: contents.value.tableContentFontColor,
        fontSize: contents.value.tableContentFontSize,
        backgroundColor: contents.value.tableContentBgColor
    }
}

const cellStyle = () => {
    return {
        color: contents.value.tableContentFontColor,
        fontSize: contents.value.tableContentFontSize,
        backgroundColor: contents.value.tableContentBgColor
    }
}

// 分页样式
const contentPageStyleChange = () => {
    let arr = []
    if (contents.value.pageTotal) arr.push('total')
    if (contents.value.pageSizes) arr.push('sizes')
    if (contents.value.pagePrevNext) {
        arr.push('prev')
        if (contents.value.pagePager) arr.push('pager')
        arr.push('next')
    }
    if (contents.value.pageJumper) arr.push('jumper')
    layouts.value = arr.join()
    contents.value.pageEachNum = 10
}

// 打印功能
const printFrom = ref({
    name1: ''
})
const printVisiable = ref(false)

const dayinOpen = (row) => {
    printFrom.value.name1 = row.forumName
    printVisiable.value = true
}

// 打印
const print = () => {
    // 实现打印功能
    const printWindow = window.open('', '_blank')
    printWindow.document.write(`
        <html>
            <head>
                <title>打印</title>
                <style>
                    body { font-family: Arial, sans-serif; }
                    .print-content { padding: 20px; }
                </style>
            </head>
            <body>
                <div class="print-content">
                    <h2>${printFrom.value.name1}</h2>
                </div>
            </body>
        </html>
    `)
    printWindow.document.close()
    printWindow.print()
    printWindow.close()
}

// 报表功能
const chartVisiable = ref(false)
const chartDialog = () => {
    chartVisiable.value = true
    nextTick(() => {
        const chart = echarts.init(document.getElementById('chartRef'))
        const option = {
            title: {
                text: '论坛统计'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: ['发帖数']
            },
            xAxis: {
                type: 'category',
                data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            },
            yAxis: {
                type: 'value'
            },
            series: [
                {
                    name: '发帖数',
                    type: 'bar',
                    data: [10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65]
                }
            ]
        }
        chart.setOption(option)
    })
}

// 组件挂载
onMounted(() => {
    console.log('论坛管理页面初始化')
    const route = useRoute()
    console.log('当前路由:', route)
    console.log('组件已挂载')

    // 获取数据
    getDataList()

    // 配置分页样式
    const pagination = document.querySelector('.pagination-container')
    if (pagination) {
        pagination.style.marginTop = '20px'
        pagination.style.marginBottom = '20px'
    }
})

// 组件卸载
onUnmounted(() => {
    console.log('论坛管理页面卸载')
    // 清理数据
    dataList.value = []
    totalPage.value = 0
    dataListLoading.value = false
    // 关闭弹窗
    addOrUpdateVisible.value = false
    addOrUpdateRef.value = null
})
</script>

<style lang="scss" scoped>
.main-content {
    padding: 20px;

    .form-content {
        margin-bottom: 20px;
    }

    .table-content {
        margin-bottom: 20px;
    }

    .demo-input-suffix {
        margin-bottom: 20px;
    }
}
</style>