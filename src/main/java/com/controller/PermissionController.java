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

import com.entity.PermissionEntity;
import com.service.PermissionService;
import com.utils.PageUtils;
import com.utils.R;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = permissionService.queryPage(params);
        return R.ok().put("data", page);
    }

    @RequestMapping("/list")
    public R list() {
        return R.ok().put("data", permissionService.selectList(null));
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        return R.ok().put("data", permissionService.selectById(id));
    }

    @PostMapping("/save")
    public R save(@RequestBody PermissionEntity permission) {
        permissionService.insert(permission);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody PermissionEntity permission) {
        permissionService.updateById(permission);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        permissionService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
} 