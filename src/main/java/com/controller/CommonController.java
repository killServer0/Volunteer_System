package com.controller;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.IgnoreAuth;
import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.MatchRequest;
import com.baidu.aip.util.Base64Util;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.ConfigEntity;
import com.service.CommonService;
import com.service.ConfigService;
import com.utils.BaiduUtil;
import com.utils.FileUtil;
import com.utils.R;

/**
 * 通用接口
 */
@RestController
public class CommonController {
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private CommonService commonService;


    /**
     * Java代码实现MySQL数据库导出
     *
     * @param mysqlUrl     MySQL安装路径
     * @param hostIP       MySQL数据库所在服务器地址IP
     * @param userName     进入数据库所需要的用户名
     * @param hostPort     数据库端口
     * @param password     进入数据库所需要的密码
     * @param savePath     数据库文件保存路径
     * @param fileName     数据库导出文件文件名
     * @param databaseName 要导出的数据库名
     * @return 返回true表示导出成功，否则返回false。
     */
    @IgnoreAuth
    @RequestMapping("/beifen")
    public R beifen(String mysqlUrl, String hostIP, String userName, String hostPort, String password, String savePath, String fileName, String databaseName) {
        logger.info("开始备份数据库: 目标数据库:{}, 保存路径:{}, 文件名:{}", databaseName, savePath, fileName);
        
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在 
            boolean mkdirSuccess = saveFile.mkdirs();// 创建文件夹 
            if (!mkdirSuccess) {
                logger.error("创建备份目录失败: {}", savePath);
                return R.error("创建备份目录失败");
            }
        }
        if (!savePath.endsWith(File.separator)) {
            savePath = savePath + File.separator;
        }
        
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        BufferedReader errorReader = null;
        Process process = null;
        
        try {
            Runtime runtime = Runtime.getRuntime();
            // 构建MySQL导出命令
            String cmd = mysqlUrl + "mysqldump -h" + hostIP + " -u" + userName + " -P" + hostPort + " -p" + password + " " + databaseName;
            logger.debug("执行命令: {}", cmd);
            
            // 执行命令
            process = runtime.exec(cmd);
            
            // 读取标准输出
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            
            // 读取错误输出
            InputStreamReader errorStreamReader = new InputStreamReader(process.getErrorStream(), "utf8");
            errorReader = new BufferedReader(errorStreamReader);
            
            // 创建输出文件
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + fileName), "utf8"));
            
            // 处理标准输出
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                printWriter.println(line);
            }
            printWriter.flush();
            
            // 检查错误输出
            StringBuilder errorMsg = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorMsg.append(line).append("\n");
            }
            
            // 等待进程结束并获取退出码
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                logger.error("数据库备份命令执行失败，退出码: {}, 错误信息: {}", exitCode, errorMsg);
                return R.error("备份数据出错: " + (errorMsg.length() > 0 ? errorMsg.toString() : "未知错误"));
            }
            
            // 检查是否有任何错误输出
            if (errorMsg.length() > 0) {
                logger.warn("数据库备份过程中产生警告: {}", errorMsg);
            }
            
            logger.info("数据库备份成功: {}", savePath + fileName);
            return R.ok();
            
        } catch (Exception e) {
            logger.error("备份数据库时发生异常", e);
            return R.error("备份数据出错: " + e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (errorReader != null) {
                    errorReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                logger.error("关闭资源时发生异常", e);
            }
        }
    }

    /**
     * Java实现MySQL数据库导入
     *
     * @param mysqlUrl     MySQL安装路径
     * @param hostIP       MySQL数据库所在服务器地址IP
     * @param userName     进入数据库所需要的用户名
     * @param hostPort     数据库端口
     * @param password     进入数据库所需要的密码
     * @param savePath     数据库文件保存路径
     * @param fileName     数据库导出文件文件名
     * @param databaseName 要导出的数据库名
     */
    @IgnoreAuth
    @RequestMapping("/huanyuan")
    public R huanyuan(String mysqlUrl, String hostIP, String userName, String hostPort, String password, String savePath, String fileName, String databaseName) {
        logger.info("开始恢复数据库: 目标数据库:{}, 备份文件:{}/{}", databaseName, savePath, fileName);
        
        // 检查文件是否存在
        File inputFile = new File(savePath + "/" + fileName);
        if (!inputFile.exists() || !inputFile.isFile()) {
            logger.error("备份文件不存在: {}/{}", savePath, fileName);
            return R.error("备份文件不存在");
        }
        
        BufferedReader br = null;
        OutputStreamWriter writer = null;
        OutputStream out = null;
        Process process = null;
        BufferedReader errorReader = null;
        
        try {
            Runtime rt = Runtime.getRuntime();
            // 构建MySQL恢复命令
            String cmd = mysqlUrl + "mysql.exe  -h" + hostIP + " -u" + userName + " -P" + hostPort + " -p" + password + " " + databaseName;
            logger.debug("执行命令: {}", cmd);
            
            // 执行命令
            process = rt.exec(cmd);
            
            // 获取输出流
            out = process.getOutputStream(); // 控制台的输入信息作为输出流
            
            // 读取SQL文件
            br = new BufferedReader(new InputStreamReader(new FileInputStream(savePath + "/" + fileName), "utf-8"));
            StringBuffer sb = new StringBuffer();
            String inStr;
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr).append("\r\n");
            }
            String outStr = sb.toString();
            
            // 写入数据到MySQL
            writer = new OutputStreamWriter(out, "utf8");
            writer.write(outStr);
            writer.flush();
            
            // 读取错误输出
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "utf8"));
            StringBuilder errorMsg = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorMsg.append(line).append("\n");
            }
            
            // 等待进程结束并获取退出码
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                logger.error("数据库恢复命令执行失败，退出码: {}, 错误信息: {}", exitCode, errorMsg);
                return R.error("数据导入出错: " + (errorMsg.length() > 0 ? errorMsg.toString() : "未知错误"));
            }
            
            // 检查是否有任何错误输出
            if (errorMsg.length() > 0) {
                logger.warn("数据库恢复过程中产生警告: {}", errorMsg);
            }
            
            logger.info("数据库恢复成功");
            return R.ok();
            
        } catch (Exception e) {
            logger.error("恢复数据库时发生异常", e);
            return R.error("数据导入出错: " + e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (out != null) {
                    out.close();
                }
                if (br != null) {
                    br.close();
        }
                if (errorReader != null) {
                    errorReader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                logger.error("关闭资源时发生异常", e);
            }
        }
    }


    /**
     * 饼状图求和
     * @return
     */
    @RequestMapping("/pieSum")
    public R pieSum(@RequestParam Map<String,Object> params) {
        logger.debug("饼状图求和:,,Controller:{},,params:{}",this.getClass().getName(),params);
        List<Map<String, Object>> result = commonService.pieSum(params);
        return R.ok().put("data", result);
    }

    /**
     * 饼状图统计
     * @return
     */
    @RequestMapping("/pieCount")
    public R pieCount(@RequestParam Map<String,Object> params) {
        logger.debug("饼状图统计:,,Controller:{},,params:{}",this.getClass().getName(),params);
        List<Map<String, Object>> result = commonService.pieCount(params);
        return R.ok().put("data", result);
    }

    /**
     * 柱状图求和单列
     * @return
     */
    @RequestMapping("/barSumOne")
    public R barSumOne(@RequestParam Map<String,Object> params) {
        logger.debug("柱状图求和单列:,,Controller:{},,params:{}",this.getClass().getName(),params);
        List<Map<String, Object>> result = commonService.barSumOne(params);

        List<String> xAxis = new ArrayList<>();//报表x轴
        List<List<String>> yAxis = new ArrayList<>();//y轴
        List<String> legend = new ArrayList<>();//标题
        List<String> yAxis0 = new ArrayList<>();
        yAxis.add(yAxis0);
        legend.add("");
        for(Map<String, Object> map :result){
            String oneValue = String.valueOf(map.get("name"));
            String value = String.valueOf(map.get("value"));
            xAxis.add(oneValue);
            yAxis0.add(value);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xAxis",xAxis);
        resultMap.put("yAxis",yAxis);
        resultMap.put("legend",legend);
        return R.ok().put("data", resultMap);
    }
    /**
     * 柱状图统计单列
     * @return
     */
    @RequestMapping("/barCountOne")
    public R barCountOne(@RequestParam Map<String,Object> params) {
        logger.debug("柱状图统计单列:,,Controller:{},,params:{}",this.getClass().getName(),params);
        List<Map<String, Object>> result = commonService.barCountOne(params);

        List<String> xAxis = new ArrayList<>();//报表x轴
        List<List<String>> yAxis = new ArrayList<>();//y轴
        List<String> legend = new ArrayList<>();//标题

        List<String> yAxis0 = new ArrayList<>();
        yAxis.add(yAxis0);
        legend.add("");
        for(Map<String, Object> map :result){
            String oneValue = String.valueOf(map.get("name"));
            String value = String.valueOf(map.get("value"));
            xAxis.add(oneValue);
            yAxis0.add(value);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xAxis",xAxis);
        resultMap.put("yAxis",yAxis);
        resultMap.put("legend",legend);
        return R.ok().put("data", resultMap);
    }

    /**
     * 柱状图统计双列
     * @return
     */
    @RequestMapping("/barSumTwo")
    public R barSumTwo(@RequestParam Map<String,Object> params) {
        logger.debug("柱状图统计双列:,,Controller:{},,params:{}",this.getClass().getName(),params);
        List<Map<String, Object>> result = commonService.barSumTwo(params);
        List<String> xAxis = new ArrayList<>();//报表x轴
        List<List<String>> yAxis = new ArrayList<>();//y轴
        List<String> legend = new ArrayList<>();//标题

        Map<String, HashMap<String, String>> dataMap = new LinkedHashMap<>();
        for(Map<String, Object> map :result){
            String name1Value = String.valueOf(map.get("name1"));
            String name2Value = String.valueOf(map.get("name2"));
            String value = String.valueOf(map.get("value"));
            if(!legend.contains(name2Value)){
                legend.add(name2Value);//添加完成后 就是最全的第二列的类型
            }
            if(dataMap.containsKey(name1Value)){
                dataMap.get(name1Value).put(name2Value,value);
            }else{
                HashMap<String, String> name1Data = new HashMap<>();
                name1Data.put(name2Value,value);
                dataMap.put(name1Value,name1Data);
            }

        }

        for(int i =0; i<legend.size(); i++){
            yAxis.add(new ArrayList<String>());
        }

        Set<String> keys = dataMap.keySet();
        for(String key:keys){
            xAxis.add(key);
            HashMap<String, String> map = dataMap.get(key);
            for(int i =0; i<legend.size(); i++){
                List<String> data = yAxis.get(i);
                if(StringUtil.isNotEmpty(map.get(legend.get(i)))){
                    data.add(map.get(legend.get(i)));
                }else{
                    data.add("0");
                }
            }
        }
        System.out.println();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xAxis",xAxis);
        resultMap.put("yAxis",yAxis);
        resultMap.put("legend",legend);
        return R.ok().put("data", resultMap);
    }
    /**
     * 柱状图统计双列
     * @return
     */
    @RequestMapping("/barCountTwo")
    public R barCountTwo(@RequestParam Map<String,Object> params) {
        logger.debug("柱状图统计双列:,,Controller:{},,params:{}",this.getClass().getName(),params);
        List<Map<String, Object>> result = commonService.barCountTwo(params);
        List<String> xAxis = new ArrayList<>();//报表x轴
        List<List<String>> yAxis = new ArrayList<>();//y轴
        List<String> legend = new ArrayList<>();//标题

        Map<String, HashMap<String, String>> dataMap = new LinkedHashMap<>();
        for(Map<String, Object> map :result){
            String name1Value = String.valueOf(map.get("name1"));
            String name2Value = String.valueOf(map.get("name2"));
            String value = String.valueOf(map.get("value"));
            if(!legend.contains(name2Value)){
                legend.add(name2Value);//添加完成后 就是最全的第二列的类型
            }
            if(dataMap.containsKey(name1Value)){
                dataMap.get(name1Value).put(name2Value,value);
            }else{
                HashMap<String, String> name1Data = new HashMap<>();
                name1Data.put(name2Value,value);
                dataMap.put(name1Value,name1Data);
            }

        }

        for(int i =0; i<legend.size(); i++){
            yAxis.add(new ArrayList<String>());
        }

        Set<String> keys = dataMap.keySet();
        for(String key:keys){
            xAxis.add(key);
            HashMap<String, String> map = dataMap.get(key);
            for(int i =0; i<legend.size(); i++){
                List<String> data = yAxis.get(i);
                if(StringUtil.isNotEmpty(map.get(legend.get(i)))){
                    data.add(map.get(legend.get(i)));
                }else{
                    data.add("0");
                }
            }
        }
        System.out.println();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xAxis",xAxis);
        resultMap.put("yAxis",yAxis);
        resultMap.put("legend",legend);
        return R.ok().put("data", resultMap);
    }

    /**
     tableName 查询表
     condition1 条件1
     condition1Value 条件1值
     average 计算平均评分

     取值
     有值 Number(res.data.value.toFixed(1))
     无值 if(res.data){}
     * */
    @IgnoreAuth
    @RequestMapping("/queryScore")
    public R queryScore(@RequestParam Map<String, Object> params) {
        logger.debug("queryScore:,,Controller:{},,params:{}",this.getClass().getName(),params);
        Map<String, Object> queryScore = commonService.queryScore(params);
        return R.ok().put("data", queryScore);
    }

    /**
     * 查询字典表的分组统计总条数
     *  tableName  		表名
     *	groupColumn  	分组字段
     * @return
     */
    @RequestMapping("/newSelectGroupCount")
    public R newSelectGroupCount(@RequestParam Map<String,Object> params) {
        logger.debug("newSelectGroupCount:,,Controller:{},,params:{}",this.getClass().getName(),params);
        List<Map<String, Object>> result = commonService.newSelectGroupCount(params);
        return R.ok().put("data", result);
    }

    /**
     * 查询字典表的分组求和
     * tableName  		表名
     * groupColumn  		分组字段
     * sumCloum			统计字段
     * @return
     */
    @RequestMapping("/newSelectGroupSum")
    public R newSelectGroupSum(@RequestParam Map<String,Object> params) {
        logger.debug("newSelectGroupSum:,,Controller:{},,params:{}",this.getClass().getName(),params);
        List<Map<String, Object>> result = commonService.newSelectGroupSum(params);
        return R.ok().put("data", result);
    }

    /**
     * 柱状图求和 老的
     */
    @RequestMapping("/barSum")
    public R barSum(@RequestParam Map<String,Object> params) {
        logger.debug("barSum方法:,,Controller:{},,params:{}",this.getClass().getName(), com.alibaba.fastjson.JSONObject.toJSONString(params));
        Boolean isJoinTableFlag =  false;//是否有级联表相关
        String one =  "";//第一优先
        String two =  "";//第二优先

        //处理thisTable和joinTable 处理内容是把json字符串转为Map并把带有,的切割为数组
        //当前表
        Map<String,Object> thisTable = JSON.parseObject(String.valueOf(params.get("thisTable")),Map.class);
        params.put("thisTable",thisTable);

        //级联表
        String joinTableString = String.valueOf(params.get("joinTable"));
        if(StringUtil.isNotEmpty(joinTableString)) {
            Map<String, Object> joinTable = JSON.parseObject(joinTableString, Map.class);
            params.put("joinTable", joinTable);
            isJoinTableFlag = true;
        }

        if(StringUtil.isNotEmpty(String.valueOf(thisTable.get("date")))){//当前表日期
            thisTable.put("date",String.valueOf(thisTable.get("date")).split(","));
            one = "thisDate0";
        }
        if(isJoinTableFlag){//级联表日期
            Map<String, Object> joinTable = (Map<String, Object>) params.get("joinTable");
            if(StringUtil.isNotEmpty(String.valueOf(joinTable.get("date")))){
                joinTable.put("date",String.valueOf(joinTable.get("date")).split(","));
                if(StringUtil.isEmpty(one)){
                    one ="joinDate0";
                }else{
                    if(StringUtil.isEmpty(two)){
                        two ="joinDate0";
                    }
                }
            }
        }
        if(StringUtil.isNotEmpty(String.valueOf(thisTable.get("string")))){//当前表字符串
            thisTable.put("string",String.valueOf(thisTable.get("string")).split(","));
            if(StringUtil.isEmpty(one)){
                one ="thisString0";
            }else{
                if(StringUtil.isEmpty(two)){
                    two ="thisString0";
                }
            }
        }
        if(isJoinTableFlag){//级联表字符串
            Map<String, Object> joinTable = (Map<String, Object>) params.get("joinTable");
            if(StringUtil.isNotEmpty(String.valueOf(joinTable.get("string")))){
                joinTable.put("string",String.valueOf(joinTable.get("string")).split(","));
                if(StringUtil.isEmpty(one)){
                    one ="joinString0";
                }else{
                    if(StringUtil.isEmpty(two)){
                        two ="joinString0";
                    }
                }
            }
        }
        if(StringUtil.isNotEmpty(String.valueOf(thisTable.get("types")))){//当前表类型
            thisTable.put("types",String.valueOf(thisTable.get("types")).split(","));
            if(StringUtil.isEmpty(one)){
                one ="thisTypes0";
            }else{
                if(StringUtil.isEmpty(two)){
                    two ="thisTypes0";
                }
            }
        }
        if(isJoinTableFlag){//级联表类型
            Map<String, Object> joinTable = (Map<String, Object>) params.get("joinTable");
            if(StringUtil.isNotEmpty(String.valueOf(joinTable.get("types")))){
                joinTable.put("types",String.valueOf(joinTable.get("types")).split(","));
                if(StringUtil.isEmpty(one)){
                    one ="joinTypes0";
                }else{
                    if(StringUtil.isEmpty(two)){
                        two ="joinTypes0";
                    }
                }

            }
        }

        List<Map<String, Object>> result = commonService.barSum(params);

        List<String> xAxis = new ArrayList<>();//报表x轴
        List<List<String>> yAxis = new ArrayList<>();//y轴
        List<String> legend = new ArrayList<>();//标题

        if(StringUtil.isEmpty(two)){//不包含第二列
            List<String> yAxis0 = new ArrayList<>();
            yAxis.add(yAxis0);
            legend.add("");
            for(Map<String, Object> map :result){
                String oneValue = String.valueOf(map.get(one));
                String value = String.valueOf(map.get("value"));
                xAxis.add(oneValue);
                yAxis0.add(value);
            }
        }else{//包含第二列
            Map<String, HashMap<String, String>> dataMap = new LinkedHashMap<>();
            if(StringUtil.isNotEmpty(two)){
                for(Map<String, Object> map :result){
                    String oneValue = String.valueOf(map.get(one));
                    String twoValue = String.valueOf(map.get(two));
                    String value = String.valueOf(map.get("value"));
                    if(!legend.contains(twoValue)){
                        legend.add(twoValue);//添加完成后 就是最全的第二列的类型
                    }
                    if(dataMap.containsKey(oneValue)){
                        dataMap.get(oneValue).put(twoValue,value);
                    }else{
                        HashMap<String, String> oneData = new HashMap<>();
                        oneData.put(twoValue,value);
                        dataMap.put(oneValue,oneData);
                    }

                }
            }

            for(int i =0; i<legend.size(); i++){
                yAxis.add(new ArrayList<String>());
            }

            Set<String> keys = dataMap.keySet();
            for(String key:keys){
                xAxis.add(key);
                HashMap<String, String> map = dataMap.get(key);
                for(int i =0; i<legend.size(); i++){
                    List<String> data = yAxis.get(i);
                    if(StringUtil.isNotEmpty(map.get(legend.get(i)))){
                        data.add(map.get(legend.get(i)));
                    }else{
                        data.add("0");
                    }
                }
            }
            System.out.println();
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xAxis",xAxis);
        resultMap.put("yAxis",yAxis);
        resultMap.put("legend",legend);
        return R.ok().put("data", resultMap);
    }

    /**
     * 柱状图统计 老的
     */
    @RequestMapping("/barCount")
    public R barCount(@RequestParam Map<String,Object> params) {
        logger.debug("barCount方法:,,Controller:{},,params:{}",this.getClass().getName(), com.alibaba.fastjson.JSONObject.toJSONString(params));
        Boolean isJoinTableFlag =  false;//是否有级联表相关
        String one =  "";//第一优先
        String two =  "";//第二优先

        //处理thisTable和joinTable 处理内容是把json字符串转为Map并把带有,的切割为数组
        //当前表
        Map<String,Object> thisTable = JSON.parseObject(String.valueOf(params.get("thisTable")),Map.class);
        params.put("thisTable",thisTable);

        //级联表
        String joinTableString = String.valueOf(params.get("joinTable"));
        if(StringUtil.isNotEmpty(joinTableString)) {
            Map<String, Object> joinTable = JSON.parseObject(joinTableString, Map.class);
            params.put("joinTable", joinTable);
            isJoinTableFlag = true;
        }

        if(StringUtil.isNotEmpty(String.valueOf(thisTable.get("date")))){//当前表日期
            thisTable.put("date",String.valueOf(thisTable.get("date")).split(","));
            one = "thisDate0";
        }
        if(isJoinTableFlag){//级联表日期
            Map<String, Object> joinTable = (Map<String, Object>) params.get("joinTable");
            if(StringUtil.isNotEmpty(String.valueOf(joinTable.get("date")))){
                joinTable.put("date",String.valueOf(joinTable.get("date")).split(","));
                if(StringUtil.isEmpty(one)){
                    one ="joinDate0";
                }else{
                    if(StringUtil.isEmpty(two)){
                        two ="joinDate0";
                    }
                }
            }
        }
        if(StringUtil.isNotEmpty(String.valueOf(thisTable.get("string")))){//当前表字符串
            thisTable.put("string",String.valueOf(thisTable.get("string")).split(","));
            if(StringUtil.isEmpty(one)){
                one ="thisString0";
            }else{
                if(StringUtil.isEmpty(two)){
                    two ="thisString0";
                }
            }
        }
        if(isJoinTableFlag){//级联表字符串
            Map<String, Object> joinTable = (Map<String, Object>) params.get("joinTable");
            if(StringUtil.isNotEmpty(String.valueOf(joinTable.get("string")))){
                joinTable.put("string",String.valueOf(joinTable.get("string")).split(","));
                if(StringUtil.isEmpty(one)){
                    one ="joinString0";
                }else{
                    if(StringUtil.isEmpty(two)){
                        two ="joinString0";
                    }
                }
            }
        }
        if(StringUtil.isNotEmpty(String.valueOf(thisTable.get("types")))){//当前表类型
            thisTable.put("types",String.valueOf(thisTable.get("types")).split(","));
            if(StringUtil.isEmpty(one)){
                one ="thisTypes0";
            }else{
                if(StringUtil.isEmpty(two)){
                    two ="thisTypes0";
                }
            }
        }
        if(isJoinTableFlag){//级联表类型
            Map<String, Object> joinTable = (Map<String, Object>) params.get("joinTable");
            if(StringUtil.isNotEmpty(String.valueOf(joinTable.get("types")))){
                joinTable.put("types",String.valueOf(joinTable.get("types")).split(","));
                if(StringUtil.isEmpty(one)){
                    one ="joinTypes0";
                }else{
                    if(StringUtil.isEmpty(two)){
                        two ="joinTypes0";
                    }
                }

            }
        }

        List<Map<String, Object>> result = commonService.barCount(params);

        List<String> xAxis = new ArrayList<>();//报表x轴
        List<List<String>> yAxis = new ArrayList<>();//y轴
        List<String> legend = new ArrayList<>();//标题

        if(StringUtil.isEmpty(two)){//不包含第二列
            List<String> yAxis0 = new ArrayList<>();
            yAxis.add(yAxis0);
            legend.add("");
            for(Map<String, Object> map :result){
                String oneValue = String.valueOf(map.get(one));
                String value = String.valueOf(map.get("value"));
                xAxis.add(oneValue);
                yAxis0.add(value);
            }
        }else{//包含第二列
            Map<String, HashMap<String, String>> dataMap = new LinkedHashMap<>();
            if(StringUtil.isNotEmpty(two)){
                for(Map<String, Object> map :result){
                    String oneValue = String.valueOf(map.get(one));
                    String twoValue = String.valueOf(map.get(two));
                    String value = String.valueOf(map.get("value"));
                    if(!legend.contains(twoValue)){
                        legend.add(twoValue);//添加完成后 就是最全的第二列的类型
                    }
                    if(dataMap.containsKey(oneValue)){
                        dataMap.get(oneValue).put(twoValue,value);
                    }else{
                        HashMap<String, String> oneData = new HashMap<>();
                        oneData.put(twoValue,value);
                        dataMap.put(oneValue,oneData);
                    }

                }
            }

            for(int i =0; i<legend.size(); i++){
                yAxis.add(new ArrayList<String>());
            }

            Set<String> keys = dataMap.keySet();
            for(String key:keys){
                xAxis.add(key);
                HashMap<String, String> map = dataMap.get(key);
                for(int i =0; i<legend.size(); i++){
                    List<String> data = yAxis.get(i);
                    if(StringUtil.isNotEmpty(map.get(legend.get(i)))){
                        data.add(map.get(legend.get(i)));
                    }else{
                        data.add("0");
                    }
                }
            }
            System.out.println();
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("xAxis",xAxis);
        resultMap.put("yAxis",yAxis);
        resultMap.put("legend",legend);
        return R.ok().put("data", resultMap);
    }

    /**
     * 获取备份文件列表
     * @param savePath 文件保存路径
     * @return 文件列表
     */
    @IgnoreAuth
    @RequestMapping("/backup/list")
    public R backupList(String savePath) {
        logger.info("获取备份文件列表，路径: {}", savePath);
        
        if (StringUtils.isEmpty(savePath)) {
            return R.error("备份路径不能为空");
        }
        
        List<Map<String, Object>> fileList = new ArrayList<>();
        File dir = new File(savePath);
        
        if (!dir.exists() || !dir.isDirectory()) {
            // 目录不存在时返回空列表
            return R.ok().put("data", fileList);
        }
        
        // 获取所有sql文件
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".sql"));
        if (files != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (File file : files) {
                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("fileName", file.getName());
                fileInfo.put("fileSize", file.length());
                fileInfo.put("createTime", sdf.format(new Date(file.lastModified())));
                fileList.add(fileInfo);
            }
        }
        
        return R.ok().put("data", new HashMap<String, Object>() {{
            put("list", fileList);
            put("total", fileList.size());
        }});
    }

    /**
     * 删除备份文件
     * @param savePath 备份路径
     * @param fileName 文件名
     * @return 操作结果
     */
    @IgnoreAuth
    @RequestMapping("/backup/delete")
    public R deleteBackup(String savePath, String fileName) {
        logger.info("删除备份文件，路径: {}，文件: {}", savePath, fileName);
        
        if (StringUtils.isEmpty(savePath) || StringUtils.isEmpty(fileName)) {
            return R.error("备份路径和文件名不能为空");
        }
        
        File file = new File(savePath, fileName);
        if (!file.exists()) {
            return R.error("文件不存在");
        }
        
        boolean deleted = file.delete();
        if (deleted) {
            return R.ok();
        } else {
            return R.error("删除文件失败");
        }
    }
}
