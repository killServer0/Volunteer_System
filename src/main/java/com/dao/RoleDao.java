package com.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.RoleEntity;

/**
 * 角色数据访问接口
 * 
 * @Mapper 注解：将接口标记为 MyBatis 的 Mapper 接口，供 Spring 自动扫描注册
 * 继承 BaseMapper：MyBatis-Plus 提供的基础 Mapper 接口，包含基本的 CRUD 方法：
 * - insert：插入数据
 * - delete：删除数据
 * - update：更新数据
 * - selectById：根据 ID 查询
 * - selectList：查询列表
 * - selectPage：分页查询
 * 等等...
 */
@Mapper
public interface RoleDao extends BaseMapper<RoleEntity> {
    // 继承 BaseMapper 后已经拥有基础的 CRUD 功能，无需额外定义方法
} 