package com.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.PermissionEntity;

@Mapper
public interface PermissionDao extends BaseMapper<PermissionEntity> {
} 