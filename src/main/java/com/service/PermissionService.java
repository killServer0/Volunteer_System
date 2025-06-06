package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.entity.PermissionEntity;
import com.utils.PageUtils;

public interface PermissionService extends IService<PermissionEntity> {
    PageUtils queryPage(Map<String, Object> params);
} 