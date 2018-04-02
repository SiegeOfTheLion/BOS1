package com.itheima.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

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
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

/**
 * ClassName:ImageAction <br/>
 * Function: <br/>
 * Date: 2018年3月25日 下午10:10:18 <br/>
 */

@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class ImageAction extends ActionSupport {
    
    
    // 获取用户上传的文件
    private File imgFile;

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    // 使用属性驱动获取用户上传的文件名
    private String imgFileFileName;

    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }

    @Action("imageAction_upload")
    public void upload() throws IOException {

        // D:aa/girl/a.jpg
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            // 指定保存文件的文件夹
            String girl = "/girl";
            // 如果文件上传成功,要怎么样,不成功呢?
            // D:aa--->绝对路径怎么获取
            ServletContext servletContext =
                    ServletActionContext.getServletContext();
            String absolutePath = servletContext.getRealPath(girl);
            // 获取文件名的后缀
            String suffix =
                    imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
            // 我们要把文件放在那个里面
            // 文件名
            String fileName =
                    UUID.randomUUID().toString().replace("-", "") + suffix;
            // 图片途径
            File destFile = new File(absolutePath + "/" + fileName);
            // 使用file
            // imgFile:上传的文件
            // destFile:文件的名字
            FileUtils.copyFile(imgFile, destFile);

            // 项目路径
            String contextPath = servletContext.getContextPath();

            // 上传成功的话
            // http://localhost:8080/bos_management_web/upload/a.jpg
            map.put("error", 0);
            map.put("url", contextPath + "/girl" + fileName);
        } catch (IOException e) {
            e.printStackTrace();

            // 如果上传不成功的话
            // 不成功的内容又要怎么写出去
            map.put("error", 1);
            map.put("message", "错误信息");

        }

        String json = JSONObject.fromObject(map).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);

    }

    // 图片扩展名
    String[] fileTypes = new String[] {"gif", "jpg", "jpeg", "png", "bmp"};

    /**
     * 图片空间图片回显
     */
    @Action("imageAction_manager")
    public String manager() throws IOException {

        // 指定保存文件的文件夹
        String girl = "/girl";
        // 如果文件上传成功,要怎么样,不成功呢?
        // D:aa--->绝对路径怎么获取
        ServletContext servletContext =
                ServletActionContext.getServletContext();
        String absolutePath = servletContext.getRealPath(girl);
        // 当前文件的绝对路径
        File currentPathFile = new File(absolutePath);

        // 遍历目录取的文件信息
        List<Hashtable> fileList = new ArrayList<Hashtable>();

        if (currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Hashtable<String, Object> hash =
                        new Hashtable<String, Object>();
                String fileName = file.getName();
                if (file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if (file.isFile()) {
                    String fileExt =
                            fileName.substring(fileName.lastIndexOf(".") + 1)
                                    .toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());

                    hash.put("is_photo",
                            Arrays.<String>asList(fileTypes).contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(file.lastModified()));
                fileList.add(hash);
            }
        }
        JSONObject result = new JSONObject();
        // 获取本项目路径
        String contextPath = servletContext.getContextPath();
        result.put("current_url", contextPath + girl + "/");
        // 把所有的图片信息查询出来,封装在fileList对象的身上
        result.put("file_list", fileList);

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(result.toString());
        return NONE;
    }
    
    

}
