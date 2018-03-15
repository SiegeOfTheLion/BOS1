package com.itheima.bos.web.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * ClassName:CommonAction <br/>
 * Function: <br/>
 * Date: 2018年3月15日 下午9:30:34 <br/>
 */
public class CommonAction<T> extends ActionSupport implements ModelDriven<T> {

    private T model;
    private Class<T> clazz;

    public CommonAction(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T getModel() {
        try {
            model = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    // 属性
    protected int page;
    protected int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void page2json(Page<T> page, JsonConfig jsonConfig)
            throws IOException {
        //当前页记录数
        List<T> list = page.getContent();
        //总数据条数
        long total = page.getTotalElements();

        // 构建map集合
        Map<String, Object> map = new HashMap<>();
        //封装
        map.put("rows", list);
        map.put("total", total);
        String json;
        //当不需要jsonconfig的时候就给它赋值为零
        if (jsonConfig == null) {
            json = JSONObject.fromObject(map).toString();
        } else {
            json = JSONObject.fromObject(map, jsonConfig).toString();
        }

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }

}
