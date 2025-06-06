package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.entity.MenuEntity;
import com.utils.PageUtils;

public interface MenuService extends IService<MenuEntity> {
    PageUtils queryPage(Map<String, Object> params);
} 