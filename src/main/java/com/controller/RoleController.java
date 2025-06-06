package com.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entity.RoleEntity;
import com.service.RoleService;
import com.utils.PageUtils;
import com.utils.R;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = roleService.queryPage(params);
        return R.ok().put("data", page);
    }

    @RequestMapping("/list")
    public R list() {
        return R.ok().put("data", roleService.selectList(null));
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        return R.ok().put("data", roleService.selectById(id));
    }

    @PostMapping("/save")
    public R save(@RequestBody RoleEntity role) {
        roleService.insert(role);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody RoleEntity role) {
        roleService.updateById(role);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        roleService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
} 