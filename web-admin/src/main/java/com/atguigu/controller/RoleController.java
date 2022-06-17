package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private final static String PAGE_INDEX = "role/index";
    private static final String PAGE_CREATE = "role/create";
    private static final String ACTION_LIST = "redirect:/role";
    private static final String PAGE_SUCCESS = "common/successPage";
    private static final String PAGE_EDIT = "role/edit";

    @Autowired
    RoleService roleService;

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id ,Model model){
        Role role = roleService.getById(id);
        model.addAttribute("role",role);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Role role,HttpServletRequest request) {
        roleService.update(role);
        return this.successPage("修改成功",request);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        roleService.delete(id);
        return ACTION_LIST;
    }

    @RequestMapping("/save")
    public String save(Role role,HttpServletRequest request){
        roleService.insert(role);
        return this.successPage("新增用户成功",request);
    }


    @RequestMapping("/create")
    public String create(){
        return PAGE_CREATE;
    }

    /**
     * 分页查询
     * @param model
     * @return
     */
    @RequestMapping
    public String index(HttpServletRequest request, Model model) {
        Map<String,Object> filters = getFilters(request);
        PageInfo<Role> pageInfo =  roleService.findPage(filters);
        model.addAttribute("page",pageInfo);
        model.addAttribute("filters",filters);
        return PAGE_INDEX;
    }

    /**
     * 列表
     * @param model
     * @return
     */
   /* @RequestMapping
    public String index(Model model) {
        List<Role> list = roleService.findAll();
        model.addAttribute("list", list);
        return PAGE_INDEX;
    }*/


}
