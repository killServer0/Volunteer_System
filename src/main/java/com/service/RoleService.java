package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.entity.RoleEntity;
import com.utils.PageUtils;

public interface RoleService extends IService<RoleEntity> {
    PageUtils queryPage(Map<String, Object> params);
} 