const beifen = async () => {
    if (!ruleFormRef.value) return

    await ruleFormRef.value.validate(async (valid) => {
        if (valid) {
            loading.value = true
            const loadingInstance = ElLoading.service({
                text: '正在创建备份，请稍候...',
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
                console.log('发送备份请求，参数:', formData);
                const res = await http.get('/beifen', {
                    params: formData
                })
                
                if (res.code === 0) {
                    ElMessage.success('备份创建成功')
                    // 通知父组件备份成功
                    emits('success')
                } else {
                    ElMessage.error(res.msg || '备份创建失败')
                }
            } catch (error) {
                console.error('备份创建失败:', error)
                ElMessage.error('备份创建失败：' + (error.message || '未知错误'))
            } finally {
                loadingInstance.close()
                loading.value = false
            }
        }
    })
} 