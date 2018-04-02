package com.itheima.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.domain.take_delivery.Promotion;
import com.itheima.bos.service.take_deliverys.PromotionService;
import com.itheima.bos.web.action.CommonAction;

/**
 * ClassName:PromotionAction <br/>
 * Function: <br/>
 * Date: 2018年3月31日 下午4:35:43 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class PromotionAction extends CommonAction<Promotion> {

    public PromotionAction() {
        super(Promotion.class);
    }

    // 使用属性驱动获取封面图片和图片的文件名
    private File titleImgFile;
    private String titleImgFileFileName;

    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }

    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    @Autowired
    private PromotionService promotionService;

    /**
     * 保存数据
     */

    @Action(value = "promotionAction_save",
            results = {@Result(name = "success",
                    location = "/pages/take_delivery/promotion.html",
                    type = "redirect")})
    public String save() {
        Promotion promotion = getModel();

        try {
            // 图片存储的文件夹
            String dirPath = "/upload";
            // 获取绝对路径
            ServletContext servletContext =
                    ServletActionContext.getServletContext();
            String dirRealPath = servletContext.getRealPath(dirPath);

            // 获取文件名的后缀
            String suffix = titleImgFileFileName
                    .substring(titleImgFileFileName.lastIndexOf("."));

            System.out.println("suffix=" + suffix);

            // 使用UUID生成文件名
            String fileName =
                    UUID.randomUUID().toString().replaceAll("-", "") + suffix;

            // 拼接文件
            File destFile = new File(dirRealPath + "/" + fileName);

            // 保存
            FileUtils.copyFile(titleImgFile, destFile);
            // http://localhost:8080/bos_management_web/upload/a.jpg
            promotion.setTitleImg("/upload/" + fileName);
        } catch (IOException e) {

            e.printStackTrace();
            promotion.setTitleImg(null);
        }
        // 设置状态
        promotion.setStatus("1");
        promotionService.save(promotion);
        return SUCCESS;
    }

    /**
     * 分页查询
     */
    @Action("promotionAction_pageQuery")
    public String pageQuery() throws IOException {
        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1
        Pageable pageable = new PageRequest(page - 1, rows);
        // 封装数据
        Page<Promotion> page = promotionService.findAll(pageable);
        // JSONObject : 封装对象或map集合
        // JSONArray : 数组,list集合
        // 把对象转化为json字符串
        // 输出流写出去
        // 获取到response
        // 解决中文乱码的问题
        System.out.println(page);
        page2json(page, null);
        return NONE;
    }

}
