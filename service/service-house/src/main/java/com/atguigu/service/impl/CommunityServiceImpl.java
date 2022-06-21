package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.CommunityDao;
import com.atguigu.dao.DictDao;
import com.atguigu.entity.Community;
import com.atguigu.service.CommunityService;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CommunityService.class)
@Transactional
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {

    @Autowired
    CommunityDao communityDao;

    @Autowired
    DictDao dictDao;


    @Override
    public BaseDao<Community> getEntityDao() {
        return communityDao;
    }

    @Override
    public PageInfo<Community> findPage(Map<String, Object> filters) {
        //当前页数
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        //每页显示的记录条数
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 10);

        PageHelper.startPage(pageNum, pageSize);
        Page<Community> page = communityDao.findPage(filters);
        List<Community> list = page.getResult();
        for (Community community : list) {
            community.setAreaName(dictDao.getNameById(community.getAreaId()));
            community.setPlateName(dictDao.getNameById(community.getPlateId()));
        }
        return new PageInfo<Community>(page, 10);
    }

    @Override
    public List<Community> findList() {
        return communityDao.findList();
    }

    @Override
    public Community getById(Serializable id) {
        Community community = communityDao.getById(id);

        if(community == null){
            return null;
        }

        community.setAreaName(dictDao.getNameById(community.getAreaId()));
        community.setPlateName(dictDao.getNameById(community.getPlateId()));

        return community;
    }
}
