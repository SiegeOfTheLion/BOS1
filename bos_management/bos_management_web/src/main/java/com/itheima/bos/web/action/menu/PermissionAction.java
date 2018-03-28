package com.itheima.bos.web.action.menu;

import java.io.IOException;

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
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.service.system.MenuService.PermissionService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:PermissionAction <br/>
 * Function: <br/>
 * Date: 2018年3月28日 下午8:10:42 <br/>
 */
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class PermissionAction extends CommonAction<Permission> {

    public PermissionAction() {
        super(Permission.class);
    }
    
    @Autowired
    private PermissionService permissionService;

    @Action("permissionAction_pageQuery")
    public String pageQuery() throws IOException {
        // 获取pageable对象
        Pageable pageable = new PageRequest(page - 1, rows);
        // 使用业务层调用findAll方法
        Page<Permission> page = (Page<Permission>) permissionService.findAll(pageable);
        // 灵活控制输出
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"roles"});
        page2json(page, jsonConfig);
        return NONE;
    }
    
    
    @Action(value = "permissionAction_save", results = @Result(name = "success",
            location = "/pages/system/permission.html", type = "redirect"))
    public String save(){
        permissionService.save(getModel());
        return SUCCESS;
    }

}
