package com.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.DictionaryEntity;
import com.entity.view.DictionaryView;

/**
 * 字典 Dao 接口
 *
 * @author 
 */
public interface DictionaryDao extends BaseMapper<DictionaryEntity> {

   List<DictionaryView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

}
