const huanyuan = async () => {
    if (!ruleFormRef.value) return

    await ruleFormRef.value.validate(async (valid) => {
        if (valid) {
            // 二次确认弹窗
            try {
                await ElMessageBox.confirm(
                    '数据库恢复操作将覆盖现有数据，是否确认继续？', 
                    '警告', 
                    {
                        confirmButtonText: '确认恢复',
                        cancelButtonText: '取消',
                        type: 'warning',
                    }
                )
                
                loading.value = true
                const loadingInstance = ElLoading.service({
                    text: '正在恢复数据库，请稍候...',
                    background: 'rgba(0, 0, 0, 0.7)'
                })

                // 处理路径格式
                const formData = {
                    ...ruleForm,
                    // 确保使用正确的路径分隔符
                    mysqlUrl: ruleForm.mysqlUrl.replaceAll("\\", "/").endsWith("/") ? ruleForm.mysqlUrl.replaceAll("\\", "/") : ruleForm.mysqlUrl.replaceAll("\\", "/") + "/",
                    hostIP: ruleForm.hostIP.replaceAll("\\", "/"),
                    savePath: ruleForm.savePath.replaceAll("\\", "/")
                }

                try {
                    console.log('发送恢复请求，参数:', formData);
                    const res = await http.get('/huanyuan', {
                        params: formData
                    })
                    
                    if (res.code === 0) {
                        ElMessage.success('数据库恢复成功')
                        // 通知父组件恢复成功
                        emits('success')
                    } else {
                        ElMessage.error(res.msg || '数据库恢复失败')
                    }
                } catch (error) {
                    console.error('恢复失败:', error)
                    ElMessage.error('恢复失败：' + (error.message || '未知错误'))
                } finally {
                    loadingInstance.close()
                    loading.value = false
                }
            } catch (e) {
                // 用户取消操作
                console.log('用户取消了恢复操作')
            }
        }
    })
} 