package com.controller;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.GonggaoEntity;
import com.entity.view.GonggaoView;
import com.service.ChatService;
import com.service.DictionaryService;
import com.service.FeihuodongService;
import com.service.FeihuodongYuyueService;
import com.service.FeizhiyuanService;
import com.service.ForumService;
import com.service.GonggaoService;
import com.service.TokenService;
import com.service.UsersService;
import com.service.ZhihuodongService;
import com.service.ZhihuodongYuyueService;
import com.service.ZhiyuanzheLiuyanService;
import com.service.ZhiyuanzheService;
import com.utils.CommonUtil;
import com.utils.PageUtils;
import com.utils.PoiUtil;
import com.utils.R;

/**
 * 公告
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/gonggao")
public class GonggaoController {
    private static final Logger logger = LoggerFactory.getLogger(GonggaoController.class);

    private static final String TABLE_NAME = "gonggao";

    @Autowired
    private GonggaoService gonggaoService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private ChatService chatService;//客服聊天
    @Autowired
    private DictionaryService dictionaryService;//字典
    @Autowired
    private FeihuodongService feihuodongService;//活动
    @Autowired
    private FeihuodongYuyueService feihuodongYuyueService;//活动报名
    @Autowired
    private FeizhiyuanService feizhiyuanService;//游客
    @Autowired
    private ForumService forumService;//论坛
    @Autowired
    private ZhihuodongService zhihuodongService;//志愿者活动
    @Autowired
    private ZhihuodongYuyueService zhihuodongYuyueService;//志愿者活动报名
    @Autowired
    private ZhiyuanzheService zhiyuanzheService;//志愿者
    @Autowired
    private ZhiyuanzheLiuyanService zhiyuanzheLiuyanService;//志愿者留言
    @Autowired
    private UsersService usersService;//管理员


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("志愿者".equals(role))
            params.put("zhiyuanzheId",request.getSession().getAttribute("userId"));
        else if("游客".equals(role))
            params.put("feizhiyuanId",request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        PageUtils page = gonggaoService.queryPage(params);

        //字典表数据转换
        List<GonggaoView> list =(List<GonggaoView>)page.getList();
        for(GonggaoView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        GonggaoEntity gonggao = gonggaoService.selectById(id);
        if(gonggao !=null){
            //entity转view
            GonggaoView view = new GonggaoView();
            BeanUtils.copyProperties( gonggao , view );//把实体数据重构到view中
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody GonggaoEntity gonggao, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,gonggao:{}",this.getClass().getName(),gonggao.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        // 确保图片路径带有upload/前缀
        if(gonggao.getGonggaoPhoto() != null && !gonggao.getGonggaoPhoto().isEmpty() && 
           !gonggao.getGonggaoPhoto().equals("null") && !gonggao.getGonggaoPhoto().startsWith("upload/") && 
           !gonggao.getGonggaoPhoto().startsWith("http")) {
            gonggao.setGonggaoPhoto("upload/" + gonggao.getGonggaoPhoto());
        }

        Wrapper<GonggaoEntity> queryWrapper = new EntityWrapper<GonggaoEntity>()
            .eq("gonggao_name", gonggao.getGonggaoName())
            .eq("gonggao_types", gonggao.getGonggaoTypes())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        GonggaoEntity gonggaoEntity = gonggaoService.selectOne(queryWrapper);
        if(gonggaoEntity==null){
            gonggao.setInsertTime(new Date());
            gonggao.setCreateTime(new Date());
            gonggaoService.insert(gonggao);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody GonggaoEntity gonggao, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,gonggao:{}",this.getClass().getName(),gonggao.toString());
        GonggaoEntity oldGonggaoEntity = gonggaoService.selectById(gonggao.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        if("".equals(gonggao.getGonggaoPhoto()) || "null".equals(gonggao.getGonggaoPhoto())){
                gonggao.setGonggaoPhoto(null);
        } else if(gonggao.getGonggaoPhoto() != null && !gonggao.getGonggaoPhoto().startsWith("upload/") && !gonggao.getGonggaoPhoto().startsWith("http")) {
                // 确保图片路径带有upload/前缀
                gonggao.setGonggaoPhoto("upload/" + gonggao.getGonggaoPhoto());
        }

        gonggaoService.updateById(gonggao);//根据id更新
        return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<GonggaoEntity> oldGonggaoList =gonggaoService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        gonggaoService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer zhiyuanzheId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            List<GonggaoEntity> gonggaoList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            GonggaoEntity gonggaoEntity = new GonggaoEntity();
//                            gonggaoEntity.setGonggaoName(data.get(0));                    //公告名称 要改的
//                            gonggaoEntity.setGonggaoPhoto("");//详情和图片
//                            gonggaoEntity.setGonggaoTypes(Integer.valueOf(data.get(0)));   //公告类型 要改的
//                            gonggaoEntity.setInsertTime(date);//时间
//                            gonggaoEntity.setGonggaoContent("");//详情和图片
//                            gonggaoEntity.setCreateTime(date);//时间
                            gonggaoList.add(gonggaoEntity);


                            //把要查询是否重复的字段放入map中
                        }

                        //查询是否重复
                        gonggaoService.insertBatch(gonggaoList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }




    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = gonggaoService.queryPage(params);

        //字典表数据转换
        List<GonggaoView> list =(List<GonggaoView>)page.getList();
        for(GonggaoView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        GonggaoEntity gonggao = gonggaoService.selectById(id);
            if(gonggao !=null){


                //entity转view
                GonggaoView view = new GonggaoView();
                BeanUtils.copyProperties( gonggao , view );//把实体数据重构到view中

                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody GonggaoEntity gonggao, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,gonggao:{}",this.getClass().getName(),gonggao.toString());
        Wrapper<GonggaoEntity> queryWrapper = new EntityWrapper<GonggaoEntity>()
            .eq("gonggao_name", gonggao.getGonggaoName())
            .eq("gonggao_types", gonggao.getGonggaoTypes())
//            .notIn("gonggao_types", new Integer[]{102})
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        GonggaoEntity gonggaoEntity = gonggaoService.selectOne(queryWrapper);
        if(gonggaoEntity==null){
            gonggao.setInsertTime(new Date());
            gonggao.setCreateTime(new Date());
        gonggaoService.insert(gonggao);

            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

}

