package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.PermissionDao;
import com.entity.PermissionEntity;
import com.service.PermissionService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("permissionService")
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, PermissionEntity> implements PermissionService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<PermissionEntity> page = this.selectPage(
                new Query<PermissionEntity>(params).getPage(),
                new EntityWrapper<PermissionEntity>()
        );
        return new PageUtils(page);
    }
} 