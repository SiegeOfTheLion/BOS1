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

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * ClassName:CourierAction <br/>
 * Function: <br/>
 * Date: 2018年3月14日 下午5:42:11 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class CourierAction extends ActionSupport
        implements ModelDriven<Courier> {

    @Autowired
    private CourierService courierService;
    
    private Courier model = new Courier();

    @Override
    public Courier getModel() {
        return model;
    }

    // 保存快递员数据
    @Action(value = "courierAction_save", results = {@Result(name = "success",
            location = "/pages/base/courier.html", type = "redirect")})
    public String save() {
        courierService.save(model);
        return SUCCESS;
    }
    
    
    //当前页
    //每页总记录数
    private int page;
    private int rows;
    public void setPage(int page) {
        this.page = page;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    /**
     * 快递员的分页查询
     * @throws IOException 
     */
    @Action("courierAction_pageQuery")
    public String pageQuery() throws IOException{
        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1
        Pageable pageable = new PageRequest(page-1, rows);
        //调用业务层处理请求
        Page<Courier> page = courierService.findAll(pageable);
        //获取总数据条数
        long total = page.getTotalElements();
        //获取每页的记录数
        List<Courier> list = page.getContent();
        //把总记录数和每页记录数封装到Map集合中
        Map<String, Object> map =new HashMap<String, Object>();
        map.put("total", total);
        map.put("rows", list);
        // 使用jsonConfig可以灵活控制输出的内容
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[]{"fixedAreas","takeTime"});
        //把map集合转换成json数组返回给页面
        String json = JSONObject.fromObject(map,jsonConfig).toString();
        //获取response对象
        HttpServletResponse response = ServletActionContext.getResponse();
        //解决中文乱码问题
        response.setContentType("text/html;charset=UTF-8");
        //先给页面
        response.getWriter().write(json);
        return NONE;
    }

}
