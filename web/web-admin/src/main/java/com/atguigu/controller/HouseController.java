package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {


    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house/";
    private static final String PAGE_DETAIL = "house/detail";
    private static final String PAGE_UPLOADDEFAULT_SHOW = "house/uploadDefault";

    @Reference
    HouseService houseService;

    @Reference
    CommunityService communityService;

    @Reference
    DictService dictService;

    @Reference
    HouseBrokerService houseBrokerService;

    @Reference
    HouseImageService houseImageService;

    @Reference
    HouseUserService houseUserService;

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id ,Model model){

        //1.获取房源信息
        House house = houseService.getById(id);
        Long houseId = house.getId();

        //2.获取社区信息
        Community community = communityService.getById(house.getCommunityId());

        //3.获取房屋图片
        List<HouseImage> houseImage1List = houseImageService.findList(houseId, 1);

        //4.获取房产图片
        List<HouseImage> houseImage2List = houseImageService.findList(houseId, 2);

        //5.获取房产中介
        List<HouseBroker> houseBrokerList = houseBrokerService.findListByHouseId(houseId);

        //6.获取户主信息
        List<HouseUser> houseUserList = houseUserService.findListByHouseId(houseId);

        model.addAttribute("house",house);
        model.addAttribute("community",community);
        model.addAttribute("houseImage1List",houseImage1List);
        model.addAttribute("houseImage2List",houseImage2List);
        model.addAttribute("houseBrokerList",houseBrokerList);
        model.addAttribute("houseUserList",houseUserList);

        return PAGE_DETAIL;
    }

    //修改发布操作
    @RequestMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id") Long id ,@PathVariable("status") Integer status){
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseService.update(house);
        return LIST_ACTION;
    }

    //删除房源
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){

        houseService.delete(id);
        return LIST_ACTION;
    }

    //保存修改操作
    @RequestMapping("/update")
    public String update(House house,HttpServletRequest request){

        houseService.update(house);
        return this.successPage(null,request);
    }

    //修改房源界面
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id , Model model, HttpServletRequest request){

        House house = houseService.getById(id);

        List<Community> communityList = communityService.findList();

        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        List<Dict> floorList = dictService.findListByDictCode("floor");
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        List<Dict> directionList = dictService.findListByDictCode("direction");
        List<Dict> decorationList = dictService.findListByDictCode("decoration");
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");

        model.addAttribute("house",house);
        model.addAttribute("communityList",communityList);
        model.addAttribute("houseTypeList",houseTypeList);
        model.addAttribute("floorList",floorList);
        model.addAttribute("buildStructureList",buildStructureList);
        model.addAttribute("directionList",directionList);
        model.addAttribute("decorationList",decorationList);
        model.addAttribute("houseUseList",houseUseList);

        return PAGE_EDIT;
    }


    //保存新增房源
    @RequestMapping("/save")
    public String save(House house,HttpServletRequest request){
        houseService.insert(house);
        return this.successPage(null,request);
    }


    //新增房源界面
    @RequestMapping("/create")
    public String create(Model model, HttpServletRequest request){

        List<Community> communityList = communityService.findList();

        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        List<Dict> floorList = dictService.findListByDictCode("floor");
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        List<Dict> directionList = dictService.findListByDictCode("direction");
        List<Dict> decorationList = dictService.findListByDictCode("decoration");
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");

        model.addAttribute("communityList",communityList);
        model.addAttribute("houseTypeList",houseTypeList);
        model.addAttribute("floorList",floorList);
        model.addAttribute("buildStructureList",buildStructureList);
        model.addAttribute("directionList",directionList);
        model.addAttribute("decorationList",decorationList);
        model.addAttribute("houseUseList",houseUseList);

        return PAGE_CREATE;
    }

    //to 房源管理界面
    @RequestMapping
    public String index(Model model, HttpServletRequest request){

        Map<String, Object> filters = getFilters(request);

        PageInfo<House> page = houseService.findPage(filters);

        List<Community> communityList = communityService.findList();

        List<Dict> houseTypeList = dictService.findListByDictCode("houseType");
        List<Dict> floorList = dictService.findListByDictCode("floor");
        List<Dict> buildStructureList = dictService.findListByDictCode("buildStructure");
        List<Dict> directionList = dictService.findListByDictCode("direction");
        List<Dict> decorationList = dictService.findListByDictCode("decoration");
        List<Dict> houseUseList = dictService.findListByDictCode("houseUse");


        model.addAttribute("page",page);
        model.addAttribute("filters",filters);
        model.addAttribute("communityList",communityList);
        model.addAttribute("houseTypeList",houseTypeList);
        model.addAttribute("floorList",floorList);
        model.addAttribute("buildStructureList",buildStructureList);
        model.addAttribute("directionList",directionList);
        model.addAttribute("decorationList",decorationList);
        model.addAttribute("houseUseList",houseUseList);


        return PAGE_INDEX;
    }


    // 上传首页图片
    @RequestMapping("/uploadDefault/{houseId}")
    public String uploadDefault(@PathVariable("houseId") Long houseId,
                                @RequestParam("file") MultipartFile file,
                                HttpServletRequest request) throws IOException {


        String imageName = UUID.randomUUID().toString();

        byte[] bytes = file.getBytes();
        QiniuUtils.upload2Qiniu(bytes, imageName);

        House house = houseService.getById(houseId);

        house.setDefaultImageUrl("http://rdv11vxn9.hn-bkt.clouddn.com/" + imageName);

        houseService.update(house);

        return this.successPage(this.MESSAGE_SUCCESS, request);
    }

    @RequestMapping("/deleteDefault/{houseId}")
    public String deleteDefault( @PathVariable("houseId") Long houseId) throws IOException {

        House house = houseService.getById(houseId);
        String imageName = house.getDefaultImageUrl();
        house.setDefaultImageUrl(" ");
        QiniuUtils.deleteFileFromQiniu(imageName);
        houseService.update(house);
        return LIST_ACTION + houseId;
    }

    @RequestMapping("/uploadDefaultShow/{houseId}")
    public String uploadDefaultShow(@PathVariable("houseId") Long houseId, Model model) {

        model.addAttribute("houseId", houseId);

        return PAGE_UPLOADDEFAULT_SHOW;

    }

}
