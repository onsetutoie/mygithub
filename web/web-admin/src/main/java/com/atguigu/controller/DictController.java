package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    private static final String PAGE_INDEX = "dict/index";

    @Reference
    DictService dictService;

    @RequestMapping("/findZnodes")
    @ResponseBody
    public Result findByParentId(@RequestParam(value = "id",defaultValue = "0") Long id){
        //每一个树节点都是一个json对象，用map集合来表示json
        //[{ id:1, pId:0, name:"全部分类", isParent:true}]
        List<Map<String,Object>> data = dictService.findByParentId(id);
        return Result.ok(data);
    }

    //根据上级id获取子节点数据列表
    @GetMapping("/findListByParentId/{parentId}")
    @ResponseBody
    public Result<List<Dict>> findListByParentId(@PathVariable(value = "parentId") Long parentId){
        List<Dict> list = dictService.findListByParentId(parentId);
        return Result.ok(list);
    }

    //根据编码获取子节点数据列表
    @GetMapping(value = "/findListByDictCode/{dictCode}")
    @ResponseBody
    public Result<List<Dict>> findListByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findListByDictCode(dictCode);
        return Result.ok(list);
    }

    @RequestMapping
    public String index(){
        return PAGE_INDEX;
    }
}
