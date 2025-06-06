package com.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.ZhiyuanzheDao;
import com.entity.ZhiyuanzheEntity;
import com.entity.view.ZhiyuanzheView;
import com.service.ZhiyuanzheService;
import com.utils.PageUtils;
import com.utils.Query;

/**
 * 志愿者 服务实现类
 */
@Service("zhiyuanzheService")
@Transactional
public class ZhiyuanzheServiceImpl extends ServiceImpl<ZhiyuanzheDao, ZhiyuanzheEntity> implements ZhiyuanzheService {

    @Override
    public PageUtils queryPage(Map<String,Object> params) {
        Page<ZhiyuanzheView> page =new Query<ZhiyuanzheView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,params));
        return new PageUtils(page);
    }


}
