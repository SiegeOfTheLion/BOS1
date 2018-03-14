package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import com.itheima.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;

/**
 * ClassName:StandardAction <br/>
 * Function: <br/>
 * Date: 2018年3月14日 下午2:23:25 <br/>
 */
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class StandardAction extends ActionSupport
        implements ModelDriven<Standard> {

    private Standard model = new Standard();

    @Override
    public Standard getModel() {
        return model;
    }

    @Autowired
    private StandardService standardService;

    /**
     * 1.保存收派标准
     * 
     * 
     * 3.因为SpringDataJPA的save方法有修改功能,所以这里会自动实现修改并保存
     *  
     * @return
     */
    @Action(value = "standardAction_save", results = {@Result(name = "success",
            location = "/pages/base/standard.html", type = "redirect")})
    public String save() {
        standardService.save(model);
        // 因为是ajax请求,所以返回值为NONE
        return SUCCESS;
    }

    /**
     * 分页查询
     */

    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    // ajax请求,不需要跳转页面
    @Action("standardAction.pageQuery")
    public String findAll() throws IOException{
        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1
        Pageable pageable = new PageRequest(page-1, rows);
        //封装数据
        Page<Standard> page = standardService.findAll(pageable);
        //总数据条数
        long total = page.getTotalElements();
        //当前页面要实现的内容
        List<Standard> list = page.getContent();
        //封装数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        // JSONObject : 封装对象或map集合
        // JSONArray : 数组,list集合
        // 把对象转化为json字符串
        String json = JSONObject.fromObject(map).toString();
        //输出流写出去
        //获取到response
        //解决中文乱码的问题
        
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        //响应页面
        response.getWriter().write(json);
        return NONE;
    }
    

}
