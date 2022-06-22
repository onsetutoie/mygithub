package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    private static final String PAGE_INDEX = "admin/index";
    private static final String PAGE_CREATE = "admin/create";
    private static final String PAGE_EDIT = "admin/edit";
    private static final String LIST_ACTION = "redirect:/admin";
    private final static String PAGE_UPLOAD_SHOW = "admin/upload";

    @Reference
    AdminService adminService;

    @PostMapping("/upload/{id}")
    public String upload(@PathVariable Long id, @RequestParam(value = "file") MultipartFile file, HttpServletRequest request) throws IOException {
        String newFileName =  UUID.randomUUID().toString() ;
        // 上传图片
        QiniuUtils.upload2Qiniu(file.getBytes(),newFileName);
        String url= "http://rdv11vxn9.hn-bkt.clouddn.com/"+ newFileName;
        Admin admin = new Admin();
        admin.setId(id);
        admin.setHeadUrl(url);
        adminService.update(admin);
        return this.successPage(this.MESSAGE_SUCCESS, request);
    }


    @RequestMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id") Long id,Model model){

        model.addAttribute("id",id);

        return PAGE_UPLOAD_SHOW;

    }

    //用户列表 （带分页功能）
    @RequestMapping
    public String index(Model model, HttpServletRequest request) {

        Map<String, Object> filters = getFilters(request);

        PageInfo<Admin> page = adminService.findPage(filters);

        model.addAttribute("page", page);
        model.addAttribute("filters", filters);

        return PAGE_INDEX;
    }

    //新增页面
    @GetMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    //保存新增
    @PostMapping("/save")
    public String save(Admin admin, HttpServletRequest request) {

        admin.setHeadUrl("http://47.93.148.192:8080/group1/M00/03/F0/rBHu8mHqbpSAU0jVAAAgiJmKg0o148.jpg");

        adminService.insert(admin);

        return this.successPage("新增用户成功", request);
    }

    //进入编辑页面
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(value = "id") Long id, Model model) {
        Admin admin = adminService.getById(id);
        model.addAttribute("admin", admin);
        return PAGE_EDIT;
    }

    //保存编辑后的数据
    @PostMapping("/update")
    public String update(Admin admin, HttpServletRequest request) {
        adminService.update(admin);
        return this.successPage("保存成功", request);
    }

    //删除
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        adminService.delete(id);
        return LIST_ACTION;
    }
}
