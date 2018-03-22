package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.bos.web.action.CommonAction;
import com.itheima.crm.domain.Customer;

import net.sf.json.JsonConfig;

/**
 * ClassName:FixedAreaAction <br/>
 * Function: <br/>
 * Date: 2018年3月18日 下午6:07:38 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class FixedAreaAction extends CommonAction<FixedArea> {

    public FixedAreaAction() {

        super(FixedArea.class);
    }

    @Autowired
    private FixedAreaService fixedAreaService;

    @Action(value = "fixedAreaAction_save",
            results = {@Result(name = "success",
                    location = "/pages/base/fixed_area.html",
                    type = "redirect")})
    public String save() {
        fixedAreaService.save(getModel());
        return SUCCESS;
    }

    @Action(value = "fixedAreaAction_pageQuery")
    public String pageQuery() throws IOException {

        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1

        Pageable pageable = new PageRequest(page - 1, rows);

        Page<FixedArea> page = fixedAreaService.findAll(pageable);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas", "couriers"});

        page2json(page, jsonConfig);
        return NONE;
    }

    /**
     * 向crm发送请求,查询未关联的客户
     * 
     * @throws IOException
     */
    // http://localhost:8180/crm/webService/customerService?_wadl
    @Action("fixedAreaAction_findUnAssociatedCustomer")
    public String findUnAssociatedCustomer() throws IOException {
        List<Customer> list = (List<Customer>) WebClient
                .create("http://localhost:8180/crm/webService/customerService/findCustomersUnAssociated")
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        list2json(list, null);
        return NONE;
    }

    /**
     * 向CRM系统发起请求,查询已关联指定定区的客户 findCustomersAssociated2FixedArea
     * 
     * @throws IOException
     */

    @Action("fixedAreaAction_findAssociatedCustomer")
    public String findAssociatedCustomer() throws IOException {
        List<Customer> list = (List<Customer>) WebClient
                .create("http://localhost:8180/crm/webService/customerService/findCustomersAssociated2FixedArea")
                .query("fixedAreaId", getModel().getId())
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        list2json(list, null);
        return NONE;
    }

    /**
     * 向CRM系统发起请求,关联客户到指定定区
     */

    private Long[] customerIds;

    public void setCustomerIds(Long[] customerIds) {
        this.customerIds = customerIds;
    }

    /**
     * 向CRM发起请求,关联客户到指定的定区
     */
    @Action(value = "fixedAreaAction_assignCustomers2FixedArea",
            results = {@Result(name = "success",
                    location = "/pages/base/fixed_area.html",
                    type = "redirect")})
    public String assignCustomers2FixedArea() {

        WebClient
                .create("http://localhost:8180/crm/webService/customerService/associatedCustomer2FixedArea")
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .query("customerIds", customerIds)
                .query("fixedAreaId", getModel().getId()).put(null);

        return SUCCESS;
    }

    private Long courierId;
    private Long takeTimeId;

    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Long takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    @Action(value = "fixedAreaAction_associationCourierToFixedArea",
            results = {@Result(name = "success",
                    location = "/pages/base/fixed_area.html",
                    type = "redirect")})
    public String associationCourierToFixedArea() {
        //System.out.println("程序执行了.......................");
        fixedAreaService.associationCourierToFixedArea(getModel().getId(),
                courierId, takeTimeId);
        return SUCCESS;
    }
    
    
    /**
     * 关联定区
     */
    //使用属性驱动关联分区
    private Long[] subAreaIds;
    public void setSubAreaIds(Long[] subAreaIds) {
        this.subAreaIds = subAreaIds;
    }

    @Action(value = "fixedAreaAction_assignSubArea2FixedArea",
            results = {@Result(name = "success",
                    location = "/pages/base/sub_area.html",
                    type = "redirect")})
    public String assignSubArea2FixedArea() {
        //System.out.println("程序执行了.......................");
        fixedAreaService.assignSubArea2FixedArea(getModel().getId(),subAreaIds);
        return SUCCESS;
    }

}
