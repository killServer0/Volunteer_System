package com.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.DictionaryDao;
import com.entity.DictionaryEntity;
import com.entity.view.DictionaryView;
import com.service.DictionaryService;
import com.utils.PageUtils;
import com.utils.Query;
import com.utils.StringUtil;

/**
 * 字典 服务实现类
 */
@Service("dictionaryService")
@Transactional
public class DictionaryServiceImpl extends ServiceImpl<DictionaryDao, DictionaryEntity> implements DictionaryService {

    @Override
    public PageUtils queryPage(Map<String,Object> params) {
        Page<DictionaryView> page =new Query<DictionaryView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,params));
        return new PageUtils(page);
    }

     /**
     * 赋值给字典表
     * @param obj view对象
     */
    public void dictionaryConvert(Object obj, HttpServletRequest request) {
        try {
            if (obj == null) return;
            //当前view和entity中的所有types的字段
            List<String> fieldNameList = new ArrayList<>();
            Class tempClass = obj.getClass();
            while (tempClass !=null) {
                Field[] declaredFields = tempClass.getDeclaredFields();
                for (Field f : declaredFields) {
                    f.setAccessible(true);
                    if (f.getType().getName().equals("java.lang.Integer") && 
                        (f.getName().contains("Types") || f.getName().equals("huodongStatus"))) {
                        fieldNameList.add(f.getName());
                        System.out.println("找到字段: " + f.getName() + " 在类: " + tempClass.getName());
                    }
                }
                tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
            }

            // 获取监听器中的字典表
            ServletContext servletContext = request.getServletContext();
            Map<String, Map<Integer, String>> dictionaryMap= (Map<String, Map<Integer, String>>) servletContext.getAttribute("dictionaryMap");
            
            System.out.println("字典表内容: " + dictionaryMap.keySet());

            //通过Types的值给Value字段赋值
            for (String s : fieldNameList) {
                Field types = null;
                if(hasField(obj.getClass(),s)){
                    //判断view中有没有这个字段,有就通过反射取出字段
                    types= obj.getClass().getDeclaredField(s);//获取Types私有字段
                }else{
                    //本表中没有这个字段,说明它是父表中的字段,也就是entity中的字段,从entity中取值
                    types=obj.getClass().getSuperclass().getDeclaredField(s);
                }
                
                // 对于 huodongStatus 字段，使用 huodong_status 作为字典表键
                String valueFieldName = s.equals("huodongStatus") ? "huodongStatusValue" : s.replace("Types", "Value");
                Field value = obj.getClass().getDeclaredField(valueFieldName);//获取value私有字段
                
                //设置权限
                types.setAccessible(true);
                value.setAccessible(true);

                //赋值
                if (StringUtil.isNotEmpty(String.valueOf(types.get(obj)))) { //types的值不为空
                    int i = Integer.parseInt(String.valueOf(types.get(obj)));//type
                    //把s1字符中的所有大写转小写,并在前面加 _
                    char[] chars = s.toCharArray();
                    StringBuffer sbf = new StringBuffer();
                    for(int  b=0; b< chars.length; b++){
                        char ch = chars[b];
                        if(ch <= 90 && ch >= 65){
                            sbf.append("_");
                            ch += 32;
                        }
                        sbf.append(ch);
                    }
                    
                    String dicKey = sbf.toString();
                    // 对于huodongStatus字段，强制使用huodong_status作为字典键
                    if (s.equals("huodongStatus")) {
                        dicKey = "huodong_status";
                    }
                    
                    System.out.println("字段: " + s + " 转换为字典键: " + dicKey + " 值索引: " + i);
                    
                    if (!dictionaryMap.containsKey(dicKey)) {
                        System.out.println("警告: 字典表中不存在键 " + dicKey);
                        value.set(obj, "未知");
                        continue;
                    }
                    
                    Map<Integer, String> valueMap = dictionaryMap.get(dicKey);
                    if (!valueMap.containsKey(i)) {
                        System.out.println("警告: 字典表 " + dicKey + " 中不存在索引 " + i);
                        value.set(obj, "未知");
                        continue;
                    }
                    
                    String s2 = valueMap.get(i);
                    System.out.println("设置字段 " + valueFieldName + " 的值为: " + s2);
                    value.set(obj, s2);
                } else {
                    new Exception("字典表赋值出现问题::::"+value.getName());
                    value.set(obj, "");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断本实体有没有这个字段
     * @param c
     * @param fieldName
     * @return
     */
    public boolean hasField(Class c, String fieldName){
        Field[] fields = c.getDeclaredFields();

        for (Field f : fields) {
            if (fieldName.equals(f.getName())) {
                return true;

            }

        }

        return false;
    }

}
