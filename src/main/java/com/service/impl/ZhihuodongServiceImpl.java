package com.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.ZhihuodongDao;
import com.entity.ZhihuodongEntity;
import com.entity.view.ZhihuodongView;
import com.service.ZhihuodongService;
import com.utils.PageUtils;
import com.utils.Query;

/**
 * 志愿者活动 服务实现类
 */
@Service("zhihuodongService")
public class ZhihuodongServiceImpl extends ServiceImpl<ZhihuodongDao, ZhihuodongEntity> implements ZhihuodongService {
    private static final Logger logger = LoggerFactory.getLogger(ZhihuodongServiceImpl.class);

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        logger.debug("开始执行queryPage方法");
        logger.debug("查询参数: {}", params);
        
        // 处理删除标记参数
        if (params.get("zhihuodongDeleteStart") != null && params.get("zhihuodongDeleteEnd") != null) {
            params.put("zhihuodongDelete", params.get("zhihuodongDeleteStart"));
        }
        
        // 处理志愿者ID参数
        if (params.get("zhiyuanzheId") != null) {
            params.put("zhiyuanzheId", params.get("zhiyuanzheId"));
        }
        
        // 添加默认排序参数
        if (!params.containsKey("sort")) {
            params.put("sort", "id");
        }
        if (!params.containsKey("order")) {
            params.put("order", "desc");
        }
        
        Page<ZhihuodongView> page = new Query<ZhihuodongView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, params));
        
        logger.debug("查询到的总记录数: {}", page.getTotal());
        logger.debug("当前页数据: {}", page.getRecords());
        
        return new PageUtils(page);
    }

    @Override
    public boolean deleteByMap(Map<String, Object> columnMap) {
        return baseMapper.deleteByMap(columnMap) > 0;
    }

    @Override
    public boolean delete(Wrapper<ZhihuodongEntity> wrapper) {
        return baseMapper.delete(wrapper) > 0;
    }

    @Override
    public boolean insertOrUpdate(ZhihuodongEntity entity) {
        if (entity.getId() == null) {
            return baseMapper.insert(entity) > 0;
        } else {
            return baseMapper.updateById(entity) > 0;
        }
    }

    @Override
    public boolean updateForSet(String setStr, Wrapper<ZhihuodongEntity> wrapper) {
        EntityWrapper<ZhihuodongEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.setSqlSelect(setStr);
        return baseMapper.update(null, entityWrapper) > 0;
    }

    @Override
    public boolean insertOrUpdateAllColumn(ZhihuodongEntity entity) {
        if (entity.getId() == null) {
            return baseMapper.insertAllColumn(entity) > 0;
        } else {
            return baseMapper.updateAllColumnById(entity) > 0;
        }
    }

    @Override
    public boolean insertOrUpdateBatch(List<ZhihuodongEntity> entityList, int batchSize) {
        for (ZhihuodongEntity entity : entityList) {
            if (entity.getId() == null) {
                baseMapper.insert(entity);
            } else {
                baseMapper.updateById(entity);
            }
        }
        return true;
    }
}
