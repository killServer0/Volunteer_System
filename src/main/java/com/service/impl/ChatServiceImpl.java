package com.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.ChatDao;
import com.entity.ChatEntity;
import com.entity.view.ChatView;
import com.service.ChatService;
import com.utils.PageUtils;
import com.utils.Query;

/**
 * 客服聊天 服务实现类
 */
@Service("chatService")
@Transactional
public class ChatServiceImpl extends ServiceImpl<ChatDao, ChatEntity> implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        logger.debug("queryPage方法:,,params:{}", JSONObject.toJSONString(params));
        Page<ChatView> page =new Query<ChatView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,params));
        logger.debug("SQL查询结果:{}", JSONObject.toJSONString(page.getRecords()));
        return new PageUtils(page);
    }


}
