package com.itheima.bos.fore.web.action;

import java.util.Random;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.crm.domain.Customer;
import com.itheima.utils.SmsUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ClassName:CustomerAction <br/>
 * Function: <br/>
 * Date: 2018年3月20日 下午3:14:59 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class CustomerAction extends ActionSupport
        implements ModelDriven<Customer> {

    private Customer model = new Customer();

    @Override
    public Customer getModel() {

        return model;
    }

    @Action("customerAction_sendSMS")
    public String sendSMS() {

        try {
            // 随机验证码
            String code = RandomStringUtils.randomNumeric(6);
            System.out.println("code=" + code);

            // 把验证码保存到域对象中,用于和页面传递过来的做校验
            ServletActionContext.getRequest().getSession().setAttribute("code",
                    code);

            SmsUtils.sendSms(model.getTelephone(), code);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return NONE;
    }

    // 使用属性驱动获取用户传递过来验证码
    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    @Action(value = "customerAction_regist",
            results = {
                    @Result(name = "success", location = "/signup-success.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html",
                            type = "redirect")})
    public String regist() {

        // 校验验证码
        // 获取到生成的验证码
        String serverCode = (String) ServletActionContext.getRequest()
                .getSession().getAttribute("code");
        System.out.println("serverCode=" + serverCode);
        // 校验用户传递过来的和session域里面的作比较
        if (StringUtils.isNotEmpty(checkcode)
                && StringUtils.isNotEmpty(serverCode)
                && checkcode.equals(serverCode)) {

            // 提交注册用户
            WebClient
                    .create("http://localhost:8180/crm/webService/customerService/save")
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON).post(model);
            return SUCCESS;
        }
        return ERROR;

    }

}
