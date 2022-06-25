package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseDao;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.dao.UserFollowDao;
import com.atguigu.entity.UserFollow;
import com.atguigu.service.DictService;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = UserFollowService.class)
@Transactional
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {

    @Autowired
    UserFollowDao userFollowDao;

    @Reference(check = false)
    DictService dictService;

    @Override
    public BaseDao<UserFollow> getEntityDao() {
        return userFollowDao;
    }

    @Override
    public Boolean follow(Long userId, Long houseId) {
        Integer count = userFollowDao.countByUserIdAndHouserId(userId, houseId);
        if (count == 0) {
            UserFollow userFollow = new UserFollow();
            userFollow.setHouseId(houseId);
            userFollow.setUserId(userId);
            userFollowDao.insert(userFollow);
        }
        return true;
    }

    @Override
    public Boolean isFollow(Long userId, Long houseId) {
        Integer count = userFollowDao.countByUserIdAndHouserId(userId, houseId);

        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public PageInfo<UserFollowVo> findListPage(int pageNum, int pageSize, Long userId) {
        PageHelper.startPage(pageNum, pageSize);
        Page<UserFollowVo> page = userFollowDao.findListPage(userId);
        List<UserFollowVo> list = page.getResult();
        for (UserFollowVo userFollowVo : list) {
            //户型
            userFollowVo.setHouseTypeName(dictService.getNameById(userFollowVo.getHouseTypeId()));
            //楼层
            userFollowVo.setFloorName(dictService.getNameById(userFollowVo.getFloorId()));
            //朝向
            userFollowVo.setDirectionName(dictService.getNameById(userFollowVo.getDirectionId()));
        }
        return new PageInfo<UserFollowVo>(page,10);
    }

    @Override
    public Boolean cancelFollow(Long id) {
        userFollowDao.delete(id);
        return true;
    }
}
