package com.atguigu.dao;

import com.atguigu.base.BaseDao;
import com.atguigu.entity.Dict;

import java.util.List;


public interface DictDao extends BaseDao<Dict> {

    List<Dict> findByParentId(Long id);

    Integer countIsParent(Long id);

    String getNameById(Long id);

    Dict getByDictCode(String dictCode);
}