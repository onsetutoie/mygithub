package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.atguigu.vo.HouseQueryVo;
import com.atguigu.vo.HouseVo;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController //@Controller + @ResponseBody
@RequestMapping("/house")
public class HouseController {

    @Reference
    HouseService houseService;

    @Reference
    private CommunityService communityService;

    @Reference
    private DictService dictService;

    @Reference
    private HouseImageService houseImageService;

    @Reference
    private HouseBrokerService houseBrokerService;

    @Reference
    private UserFollowService userFollowService;

    @RequestMapping("/list/{pageNum}/{pageSize}")
    public Result<PageInfo<HouseVo>> list(@PathVariable("pageNum") Integer pageNum,
                                @PathVariable("pageSize") Integer pageSize,
                                @RequestBody HouseQueryVo houseQueryVo){

        PageInfo<HouseVo> page = houseService.findListPage(pageNum, pageSize, houseQueryVo);
        return Result.ok(page);
    }

    /**
     * 展示详情页面
     * @param id 房源id
     * @return 将数据封装返回
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable Long id, HttpServletRequest request){
        House house = houseService.getById(id);
        Community community = communityService.getById(house.getCommunityId());
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(id);
        List<HouseImage> houseImage1List = houseImageService.findList(id, 1);

        Map<String, Object> map = new HashMap<>();
        map.put("house",house);
        map.put("community",community);
        map.put("houseBrokerList",houseBrokerList);
        map.put("houseImage1List",houseImage1List);

        //*******补充代码 start*******
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        Boolean isFollow =false;
        if (!StringUtils.isEmpty(userInfo)) {
            isFollow = userFollowService.isFollow(userInfo.getId(), id);
        }
        //*******补充代码 end*******

        map.put("isFollow",isFollow);

        return Result.ok(map);

    }

}
