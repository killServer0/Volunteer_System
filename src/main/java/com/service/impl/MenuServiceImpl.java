package com.service.impl;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.MenuDao;
import com.entity.MenuEntity;
import com.service.MenuService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;

/**
 * 菜单服务实现类
 * 
 * @Service 注解：标记该类为 Spring 的服务类，并指定 bean 名称为 "menuService"
 * 继承 ServiceImpl：继承 MyBatis-Plus 提供的服务实现基类，提供基础的服务实现
 * 实现 MenuService：实现自定义的菜单服务接口
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, MenuEntity> implements MenuService {

    /**
     * 分页查询菜单数据
     * 
     * @param params 查询参数，包含分页信息（页码、每页记录数等）
     * @return PageUtils 自定义的分页工具类，包含：
     *         - totalCount：总记录数
     *         - pageSize：每页记录数
     *         - totalPage：总页数
     *         - currPage：当前页码
     *         - list：当前页的数据列表
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 创建分页查询对象，并执行查询
        Page<MenuEntity> page = this.selectPage(
                new Query<MenuEntity>(params).getPage(),  // 将请求参数转换为分页对象
                new EntityWrapper<MenuEntity>()           // 创建空的查询条件包装器
        );
        // 将查询结果封装为自定义的分页对象并返回
        return new PageUtils(page);
    }
} 