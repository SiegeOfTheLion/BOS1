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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * ClassName:CommonAction <br/>
 * Function: <br/>
 * Date: 2018年3月15日 下午9:30:34 <br/>
 */
public class CommonAction<T> extends ActionSupport implements ModelDriven<T> {
    //定义model对象
    private T model;
    private Class<T> clazz;
    //定义一个字节码文件对像
    public CommonAction(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public T getModel() {
        try {
            if (model==null) {
                model = clazz.newInstance();
            }
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
    /**
     * 分页查询
     * page2json:. <br/>  
     *  
     * @param page
     * @param jsonConfig
     * @throws IOException
     */
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
        //当不需要jsonconfig的时候就给它赋值为null
        if (jsonConfig == null) {
            json = JSONObject.fromObject(map).toString();
        } else {
            json = JSONObject.fromObject(map, jsonConfig).toString();
        }
        //获得response对象
        HttpServletResponse response = ServletActionContext.getResponse();
        //解决中文乱码
        response.setContentType("application/json;charset=UTF-8");
        //写出
        response.getWriter().write(json);
    }
    
    /**
     * list集合
     * list2json:. <br/>  
     *  
     * @param list
     * @param jsonConfig
     * @throws IOException
     */
    public void list2json(List<T> list,JsonConfig jsonConfig) throws IOException{
        //将数据转换成Json字符串
        String json;
        if (jsonConfig!=null) {
            
            json = JSONArray.fromObject(list, jsonConfig).toString();
        }else {
            json = JSONArray.fromObject(list).toString();
        }
        //构建response对象
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }

}
