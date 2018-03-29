package com.itheima.bos.web.action.menu;

import java.io.IOException;
import java.util.List;

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
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.MenuService.RoleService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:RoleAction <br/>
 * Function: <br/>
 * Date: 2018年3月28日 下午8:51:00 <br/>
 */
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class RoleAction extends CommonAction<Role> {

    public RoleAction() {
        super(Role.class);
    }

    @Autowired
    private RoleService roleService;

    @Action("roleAction_pageQuery")
    public String pageQuery() throws IOException {
        // 获取pageable对象
        Pageable pageable = new PageRequest(page - 1, rows);
        // 使用业务层调用findAll方法
        Page<Role> page = roleService.findAll(pageable);
        // 灵活控制输出
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"menus", "permissions", "users"});
        page2json(page, jsonConfig);
        return NONE;
    }

    // 使用属性驱动获取获取菜单和权限的ID
    private String menuIds;

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    private Long[] permissionIds;

    public void setPermissionIds(Long[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    @Action(value = "roleAction_save", results = {@Result(name = "success",
            location = "/pages/system/role.html", type = "redirect")})
    public String save() throws IOException {
        roleService.save(getModel(),menuIds,permissionIds);
        return SUCCESS;
    }

    /**
     * 动态显示 findByPermission:. <br/>
     * 
     * @return
     * @throws IOException
     */
    @Action("roleAction_findAll")
    public String findByPermission() throws IOException {
        Page<Role> page = roleService.findAll(null);
        // 灵活控制输出
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"menus", "permissions", "users"});
        List<Role> list = page.getContent();
        list2json(list, jsonConfig);
        return NONE;
    }

}
