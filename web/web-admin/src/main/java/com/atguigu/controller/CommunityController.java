package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {

    private static final String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";
    private static final String LIST_ACTION = "redirect:/community";
    @Reference
    CommunityService communityService;

    @Reference
    DictService dictService;

    //删除

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        communityService.delete(id);
        return LIST_ACTION;
    }

    //修改
    @PostMapping("/update")
    public String update(Community community,HttpServletRequest request){
        communityService.update(community);
        return this.successPage("修改社区成功",request);
    }

    //进入修改页面
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Long id){
        Community community = communityService.getById(id);
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        model.addAttribute("areaList",areaList);
        model.addAttribute("community",community);
        return PAGE_EDIT;
    }

    //保存
    @PostMapping("/save")
    public String save(Community community,HttpServletRequest request){
        communityService.insert(community);
        return this.successPage("添加社区成功",request);
    }

    //进入新增页面
    @RequestMapping("/create")
    public String create(Model model){
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        model.addAttribute("areaList",areaList);
        return PAGE_CREATE;
    }


    @RequestMapping
    public String index(HttpServletRequest request, Model model){
        Map<String, Object> filters = getFilters(request);
        if (!filters.containsKey("areaId")) {
            filters.put("areaId","");
        }
        if (!filters.containsKey("plateId")) {
            filters.put("plateId", "");
        }
        //1.分页数据查询
        PageInfo<Community> page = communityService.findPage(filters);
        //2.获取下拉列选（区域）
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        //3.返回请求参数
        model.addAttribute("areaList",areaList);
        model.addAttribute("page",page);
        model.addAttribute("filters",filters);
        return PAGE_INDEX;
    }


}
