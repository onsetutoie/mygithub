package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.HouseImage;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController extends BaseController {

    private static final String PAGE_UPLOAD_SHOW = "house/upload";
    private static final String ACTION_LIST = "redirect:/house/";


    @Reference
    HouseImageService houseImageService;


    // <input type="file" name="file" multiple="multiple">  支持上传多个文件，name的名称“file”是固定的。
    @RequestMapping("/upload/{houseId}/{type}")
    public String upload(@PathVariable("houseId") Long houseId,
                         @PathVariable("type") Integer type,
                         @RequestParam("file") MultipartFile[] files) throws IOException {

        if (files.length > 0) {
            for (MultipartFile file : files) {
                String imageName = UUID.randomUUID().toString();//不能使用原始文件名称进行上传，否则，后上传的文件可能覆盖先上传的文件

                byte[] bytes = file.getBytes();
                QiniuUtils.upload2Qiniu(bytes, imageName);

                HouseImage houseImage = new HouseImage();
                houseImage.setHouseId(houseId);
                houseImage.setImageName(imageName);
                houseImage.setType(type);
                houseImage.setImageUrl("http://rdv11vxn9.hn-bkt.clouddn.com/" + imageName);

                houseImageService.insert(houseImage);
            }
        }

        return ACTION_LIST + houseId;
    }

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId, @PathVariable("id") Long id) throws IOException {

        HouseImage houseImage = houseImageService.getById(id);
        String imageName = houseImage.getImageName();
        houseImageService.delete(id);
        QiniuUtils.deleteFileFromQiniu(imageName);

        return ACTION_LIST + houseId;
    }


    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable("houseId") Long houseId, @PathVariable("type") Integer type, Model model) {

        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);

        return PAGE_UPLOAD_SHOW;

    }


}
