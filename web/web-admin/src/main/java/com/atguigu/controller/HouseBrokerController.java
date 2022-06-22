package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {

    private static final String PAGE_CREATE = "houseBroker/create";
    private static final String PAGE_EDIT = "houseBroker/edit";
    private static final String LIST_ACTION = "redirect:/house/";
    @Reference
    HouseBrokerService houseBrokerService;

    @Reference
    AdminService adminService;

    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable Long houseId, @PathVariable Long id) {
        houseBrokerService.delete(id);
        return LIST_ACTION + houseId;
    }

    @RequestMapping("/update/{id}")
    public String update(@PathVariable Long id, HouseBroker houseBroker, HttpServletRequest request) {

        HouseBroker currentHouseBroker = houseBrokerService.getById(id);
        //修改经纪人 也要对姓名和头像更新
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        currentHouseBroker.setBrokerName(admin.getName());
        currentHouseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        currentHouseBroker.setBrokerId(admin.getId());
        houseBrokerService.update(currentHouseBroker);

        return this.successPage(null, request);
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, HttpServletRequest request) {

        HouseBroker houseBroker = houseBrokerService.getById(id);

        List<Admin> adminList = adminService.findAll();

        model.addAttribute("houseBroker", houseBroker);
        model.addAttribute("adminList", adminList);

        return PAGE_EDIT;

    }

    @RequestMapping("/save")
    public String save(HouseBroker houseBroker, HttpServletRequest request) {
        Long brokerId = houseBroker.getBrokerId();

        //给冗余字段赋值，来自admin
        Admin admin = adminService.getById(brokerId);
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());

        houseBrokerService.insert(houseBroker);

        return this.successPage(null, request);

    }


    @RequestMapping("/create")
    public String create(HouseBroker houseBroker, Model model) {
        List<Admin> adminList = adminService.findAll();


        model.addAttribute("houseBroker", houseBroker);
        model.addAttribute("adminList", adminList);

        return PAGE_CREATE;

    }

}
