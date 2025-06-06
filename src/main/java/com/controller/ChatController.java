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
import com.entity.ChatEntity;
import com.entity.ZhiyuanzheEntity;
import com.entity.view.ChatView;
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
 * 客服聊天
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/chat")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private static final String TABLE_NAME = "chat";

    @Autowired
    private ChatService chatService;


    @Autowired
    private TokenService tokenService;

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
    private GonggaoService gonggaoService;//公告
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
        logger.debug("当前角色:{}", role);
        if(false)
            return R.error(511,"永不会进入");
        else if("志愿者".equals(role))
            params.put("zhiyuanzheId",request.getSession().getAttribute("userId"));
        else if("游客".equals(role))
            params.put("feizhiyuanId",request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        logger.debug("查询参数:{}", JSONObject.toJSONString(params));
        PageUtils page = chatService.queryPage(params);
        logger.debug("查询结果:{}", JSONObject.toJSONString(page));

        //字典表数据转换
        List<ChatView> list =(List<ChatView>)page.getList();
        for(ChatView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
            logger.debug("处理后的数据:{}", JSONObject.toJSONString(c));
        }
        
        // 直接返回分页数据
        Map<String, Object> result = new HashMap<>();
        result.put("total", page.getTotal());
        result.put("pageSize", page.getPageSize());
        result.put("totalPage", page.getTotalPage());
        result.put("currPage", page.getCurrPage());
        result.put("list", list);
        
        logger.debug("最终返回数据:{}", JSONObject.toJSONString(result));
        return R.ok().put("data", result);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ChatEntity chat = chatService.selectById(id);
        if(chat !=null){
            //entity转view
            ChatView view = new ChatView();
            BeanUtils.copyProperties( chat , view );//把实体数据重构到view中
            //级联表 志愿者
            //级联表
            ZhiyuanzheEntity zhiyuanzhe = zhiyuanzheService.selectById(chat.getZhiyuanzheId());
            if(zhiyuanzhe != null){
            BeanUtils.copyProperties( zhiyuanzhe , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "zhiyuanzheId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setZhiyuanzheId(zhiyuanzhe.getId());
            }
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
    public R save(@RequestBody ChatEntity chat, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,chat:{}",this.getClass().getName(),chat.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");
        else if("志愿者".equals(role))
            chat.setZhiyuanzheId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

        Wrapper<ChatEntity> queryWrapper = new EntityWrapper<ChatEntity>()
            .eq("zhiyuanzhe_id", chat.getZhiyuanzheId())
            .eq("chat_issue", chat.getChatIssue())
            .eq("chat_reply", chat.getChatReply())
            .eq("zhuangtai_types", chat.getZhuangtaiTypes())
            .eq("chat_types", chat.getChatTypes())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ChatEntity chatEntity = chatService.selectOne(queryWrapper);
        if(chatEntity==null){
            chat.setInsertTime(new Date());
            chatService.insert(chat);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody ChatEntity chat, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,chat:{}",this.getClass().getName(),chat.toString());
        ChatEntity oldChatEntity = chatService.selectById(chat.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
//        else if("志愿者".equals(role))
//            chat.setZhiyuanzheId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

            chatService.updateById(chat);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<ChatEntity> oldChatList =chatService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        chatService.deleteBatchIds(Arrays.asList(ids));

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
            List<ChatEntity> chatList = new ArrayList<>();//上传的东西
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
                            ChatEntity chatEntity = new ChatEntity();
//                            chatEntity.setZhiyuanzheId(Integer.valueOf(data.get(0)));   //提问志愿者 要改的
//                            chatEntity.setChatIssue(data.get(0));                    //问题 要改的
//                            chatEntity.setIssueTime(sdf.parse(data.get(0)));          //问题时间 要改的
//                            chatEntity.setChatReply(data.get(0));                    //回复 要改的
//                            chatEntity.setReplyTime(sdf.parse(data.get(0)));          //回复时间 要改的
//                            chatEntity.setZhuangtaiTypes(Integer.valueOf(data.get(0)));   //状态 要改的
//                            chatEntity.setChatTypes(Integer.valueOf(data.get(0)));   //数据类型 要改的
//                            chatEntity.setInsertTime(date);//时间
                            chatList.add(chatEntity);


                            //把要查询是否重复的字段放入map中
                        }

                        //查询是否重复
                        chatService.insertBatch(chatList);
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
        PageUtils page = chatService.queryPage(params);

        //字典表数据转换
        List<ChatView> list =(List<ChatView>)page.getList();
        for(ChatView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        ChatEntity chat = chatService.selectById(id);
            if(chat !=null){


                //entity转view
                ChatView view = new ChatView();
                BeanUtils.copyProperties( chat , view );//把实体数据重构到view中

                //级联表
                    ZhiyuanzheEntity zhiyuanzhe = zhiyuanzheService.selectById(chat.getZhiyuanzheId());
                if(zhiyuanzhe != null){
                    BeanUtils.copyProperties( zhiyuanzhe , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setZhiyuanzheId(zhiyuanzhe.getId());
                }
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
    public R add(@RequestBody ChatEntity chat, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,chat:{}",this.getClass().getName(),chat.toString());
        Wrapper<ChatEntity> queryWrapper = new EntityWrapper<ChatEntity>()
            .eq("zhiyuanzhe_id", chat.getZhiyuanzheId())
            .eq("chat_issue", chat.getChatIssue())
            .eq("chat_reply", chat.getChatReply())
            .eq("zhuangtai_types", chat.getZhuangtaiTypes())
            .eq("chat_types", chat.getChatTypes())
//            .notIn("chat_types", new Integer[]{102})
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        ChatEntity chatEntity = chatService.selectOne(queryWrapper);
        if(chatEntity==null){
            chat.setInsertTime(new Date());
        chatService.insert(chat);

            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

}

