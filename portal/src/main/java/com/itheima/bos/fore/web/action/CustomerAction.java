package com.itheima.bos.fore.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.itheima.crm.domain.Customer;
import com.itheima.utils.MailUtils;
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
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    private Customer model = new Customer();
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Customer getModel() {

        return model;
    }
    //使用MQ:消息队列来发送验证码
    @Action("customerAction_sendSMS")
    public String sendSMS() {
       
            // 随机验证码
            final String code = RandomStringUtils.randomNumeric(6);
            System.out.println("code=" + code);

            // 把验证码保存到域对象中,用于和页面传递过来的做校验
            ServletActionContext.getRequest().getSession().setAttribute("code",
                    code);
            
            //使用jmsTemplate来发送验证码
            jmsTemplate.send("sms", new MessageCreator() {
                
                @Override
                public Message createMessage(Session session) throws JMSException {
                    MapMessage message = session.createMapMessage();
                    message.setString("telephone", model.getTelephone());
                    message.setString("code", code);
                    return message;
                }
            });

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

            // 生成验证码
            String activeCode = RandomStringUtils.randomNumeric(32);
            // 存储验证码
            redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 1,
                    TimeUnit.DAYS);

            String emailBody =
                    "感谢你注册本网站的账号,请您在24小时内点击<a href='http://localhost:8280/portal/customerAction_active.action?activeCode="
                            + activeCode + "&telephone=" + model.getTelephone()
                            + "'>本链接</a>,以便激活您的账号,谢谢";
            MailUtils.sendMail(model.getEmail(), "激活账号", emailBody);
            return SUCCESS;
        }
        return ERROR;

    }

    /**
     * 激活的实现
     */
    // 使用属性驱动
    private String activeCode;

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    @Action(value = "customerAction_active",
            results = {
                    @Result(name = "success", location = "/login.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html",
                            type = "redirect")})
    public String active() {
        String serverCode =
                redisTemplate.opsForValue().get(model.getTelephone());
        // 判断两个激活码是否相同(也就是判断用户是否已经激活)
        if (StringUtils.isNotEmpty(serverCode)
                && StringUtils.isNotEmpty(activeCode)
                && serverCode.equals(activeCode)) {
            // 激活后
            WebClient
                    .create("http://localhost:8180/crm/webService/customerService/active")
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone()).put(null);
            return SUCCESS;
        }
        return ERROR;
    }

    // 属性驱动

    @Action(value = "customerAction_login",
            results = {
                    @Result(name = "success", location = "/index.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/login.html",
                            type = "redirect"),
                    @Result(name = "unactived", location = "/login.html",
                            type = "redirect")})

    public String login() {

        // 登入
        String serverCode = (String) ServletActionContext.getRequest()
                .getSession().getAttribute("validateCode");
        // 校验 两个验证码相同
        if (StringUtils.isNotEmpty(serverCode)
                && StringUtils.isNotEmpty(checkcode)
                && serverCode.equals(checkcode)) {
            // 校验用户是否已经激活
            Customer customer = WebClient
                    .create("http://localhost:8180/crm/webService/customerService/isActive")
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone())
                    .get(Customer.class);
            if (customer != null && customer.getType() != null) {
                if (customer.getType() == 1) {
                    // 登入
                    Customer c = WebClient
                            .create("http://localhost:8180/crm/webService/customerService/login")
                            .accept(MediaType.APPLICATION_JSON)
                            .type(MediaType.APPLICATION_JSON)
                            .query("telephone", model.getTelephone())
                            .query("password", model.getPassword())
                            .get(Customer.class);
                    // 判断用户登入用户是否为空
                    if (c != null) {
                        ServletActionContext.getRequest().getSession()
                                .setAttribute("user", c);
                        return SUCCESS;
                    } else {
                        return ERROR;
                    }
                } else {
                    // 用户已经注册成功但是没有激活,那么就发送一条短信叫他激活
                    return "unactived";
                }
            }
        }

        return ERROR;
    }

   

    @Action(value = "customerAction_lostBlurs")
    public String lostBlurs() throws IOException {
        
        Customer customer = WebClient
                .create("http://localhost:8180/crm/webService/customerService/lostBlurs")
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON).query("telephone", model.getTelephone())
                .get(Customer.class);
        
        
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        
        if (customer!=null) {
            writer.write("该号码已存在!");
        }
        
        return NONE;
    }

}
