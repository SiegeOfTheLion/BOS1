package com.itheima.bos.fore.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.take_delivery.PageBean;
import com.itheima.bos.domain.take_delivery.Promotion;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

/**
 * ClassName:PromotionAction <br/>
 * Function: <br/>
 * Date: 2018年3月31日 下午10:25:18 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class PromotionAction extends ActionSupport {

    private String pageIndex;
    private String pageSize;

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    @Action("promotionAction_pageQuery")
    public String pageQuery() throws IOException {
        PageBean<Promotion> pageBean = WebClient
                .create("http://localhost:8080/bos_management_web/webService/promotionService/findAll4Fore")
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON).query("pageIndex", pageIndex)
                .query("pageSize", pageSize).get(PageBean.class);
        
        
        String json = JSONObject.fromObject(pageBean).toString();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(json);
        return NONE;
    }

}
