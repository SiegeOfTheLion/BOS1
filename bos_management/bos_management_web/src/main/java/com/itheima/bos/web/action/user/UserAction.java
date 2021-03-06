package com.itheima.bos.web.action.user;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.MenuService.UserService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:UserAction <br/>
 * Function: <br/>
 * Date: 2018年3月26日 下午3:22:34 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class UserAction extends CommonAction<User> {
    
    public UserAction() {
        super(User.class);
    }

    // 使用属性驱动获取客户端输出的验证码
    private String checked;

    public void setChecked(String checked) {
        this.checked = checked;
    }

    // 我们不需要写保存的方法,因为shiro框架封装了保存的方法
    @Action(value = "userAction_login",
            results = {
                    @Result(name = "success", location = "/index.html",
                            type = "redirect"),
                    @Result(name = "login", location = "/login.html",
                            type = "redirect")})
    public String login() {

        // 验证码
        String serverCode = (String) ServletActionContext.getRequest()
                .getSession().getAttribute("key");

        if (StringUtils.isNotEmpty(serverCode)
                && StringUtils.isNotEmpty(checked)
                && serverCode.equals(checked)) {
            // 主体,代表当前用户
            Subject subject = SecurityUtils.getSubject();
            // 认证用户名和密码
            AuthenticationToken token = new UsernamePasswordToken(
                    getModel().getUsername(), getModel().getPassword());
            try {
                // 登录
                subject.login(token);
                // 该方法的返回值是由Realm中创建SimpleAuthenticationInfo对象时,传入的第一个参数决定的
                User user = (User) subject.getPrincipal();
                // 把用户存到session中间
                ServletActionContext.getRequest().getSession()
                        .setAttribute("user", user);
                return SUCCESS;
            } catch (UnknownAccountException e) {
                e.printStackTrace();
                System.out.println("用户名错误!");
            } catch (IncorrectCredentialsException e) {
                e.printStackTrace();
                System.out.println("密码错误!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("其他错误!");
            }
        }
        return LOGIN;
    }
    /**
     * 退出当前系统
     * logout:. <br/>  
     *  
     * @return
     */
    @Action(value = "userAction_logout", results = {@Result(name = "login",
            location = "/login.html", type = "redirect")})
    public String logout() {
        // 获取当前用户
        Subject subject = SecurityUtils.getSubject();
        // 当前用户退出系统
        subject.logout();
        return LOGIN;
    }
    
    // 使用属性驱动分装角色
    private Long[] roleIds;
    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }
    
    @Autowired
    private UserService userService;
    
    /**
     * 保存用户信息
     */
    @Action(value = "userAction_save", results = {@Result(name = "success",
            location = "/pages/system/userlist.html", type = "redirect")})
    public String save() {
        System.out.println("保存成功............");
        userService.save(getModel() , roleIds);
        return SUCCESS;
    }
    
    /**
     * 分页查询
     */
    
    @Action(value = "userAction_pageQuery")
    public String pageQuery() throws IOException{
        // 获取pageable对象
        Pageable pageable = new PageRequest(page - 1, rows);
        // 使用业务层调用findAll方法
        Page<User> page = userService.findAll(pageable);
        //
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"roles"});
        
        page2json(page, jsonConfig);
        return NONE;
    }
}
