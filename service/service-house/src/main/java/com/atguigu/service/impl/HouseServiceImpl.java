package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.DictDao;
import com.atguigu.dao.HouseDao;
import com.atguigu.entity.House;
import com.atguigu.service.HouseService;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service(interfaceClass = HouseService.class)
@Transactional
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {

    @Autowired
    HouseDao houseDao;

    @Autowired
    DictDao dictDao;


    @Override
    public BaseDao<House> getEntityDao() {
        return houseDao;
    }

    @Override
    public House getById(Serializable id) {
        House house = houseDao.getById(id);

        if (null == house) return null;

        house.setDecorationName(dictDao.getNameById(house.getDecorationId()));
        house.setHouseTypeName(dictDao.getNameById(house.getHouseTypeId()));
        house.setFloorName(dictDao.getNameById(house.getFloorId()));
        house.setBuildStructureName(dictDao.getNameById(house.getBuildStructureId()));
        house.setDirectionName(dictDao.getNameById(house.getDirectionId()));
        house.setHouseUseName(dictDao.getNameById(house.getHouseUseId()));

        return house;
    }

    @Override
    public PageInfo<HouseVo> findListPage(int pageNum, int pageSize, HouseQueryVo houseQueryVo) {
        PageHelper.startPage(pageNum, pageSize);
        Page<HouseVo> page = houseDao.findListPage(houseQueryVo);

        if (page != null && page.size() > 0) {
            for (HouseVo houseVo : page) {
                houseVo.setHouseTypeName(dictDao.getNameById(houseVo.getHouseTypeId()));
                houseVo.setFloorName(dictDao.getNameById(houseVo.getFloorId()));
                houseVo.setDirectionName(dictDao.getNameById(houseVo.getDirectionId()));
            }
        }
        return new PageInfo<HouseVo>(page, 10);

    }
}
