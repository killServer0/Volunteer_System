package com.controller;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.MenuEntity;
import com.service.MenuService;
import com.utils.PageUtils;
import com.utils.R;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
    
    @Autowired
    private MenuService menuService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = menuService.queryPage(params);
        return R.ok().put("data", page);
    }

    @RequestMapping("/list")
    public R list() {
        return R.ok().put("data", menuService.selectList(null));
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        return R.ok().put("data", menuService.selectById(id));
    }

    @PostMapping("/save")
    public R save(@RequestBody MenuEntity menu) {
        menuService.insert(menu);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody MenuEntity menu) {
        menuService.updateById(menu);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        menuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 清空菜单表
     */
    @PostMapping("/clear")
    public R clear() {
        try {
            logger.info("清空菜单表");
            menuService.delete(new EntityWrapper<>());
            return R.ok();
        } catch (Exception e) {
            logger.error("清空菜单表失败", e);
            return R.error("清空菜单表失败: " + e.getMessage());
        }
    }
} 