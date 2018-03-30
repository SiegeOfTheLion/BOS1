package com.itheima.bos.web.action.menu;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
import com.itheima.bos.service.system.MenuService.MenuService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:MenuAction <br/>
 * Function: <br/>
 * Date: 2018年3月28日 下午4:16:47 <br/>
 */
@Controller
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
public class MenuAction extends CommonAction<Menu> {

    public MenuAction() {
        super(Menu.class);
    }

    @Autowired
    private MenuService menuService;

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }

    @Action("menuAction_findAll")
    public String findAll() throws IOException {
        // System.out.println("执行了程序.........");
        // 查询所有
        List<Menu> list = menuService.findAll();

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(
                new String[] {"roles", "childrenMenus", "parentMenu"});

        list2json(list, jsonConfig);
        return NONE;
    }

    /**
     * 保存菜单数据 save:. <br/>
     * 
     * @return
     * @throws IOException
     */
    @Action(value = "menuAction_save", results = {@Result(name = "success",
            location = "/pages/system/menu.html", type = "redirect")})
    public String save() throws IOException {
        menuService.save(getModel());
        return SUCCESS;
    }

    /**
     * 分页查询
     * 
     * @throws IOException
     */

    @Action("menuAction_pageQuery")
    public String pageQuery() throws IOException {
        // 获取pageable对象
        Pageable pageable = new PageRequest(
                Integer.parseInt(getModel().getPage()) - 1, rows);
        // 使用业务层调用findAll方法
        Page<Menu> page = menuService.findAll(pageable);
        //
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(
                new String[] {"roles", "childrenMenus", "parentMenu"});
        page2json(page, jsonConfig);
        return NONE;
    }

    @Action(value = "menuAction_findbyUser")
    public String findbyUser() throws IOException {
        // 获取当前用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();

        List<Menu> list = menuService.findbyUser(user);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"roles", "childrenMenus",
                "parentMenu", "children"});

        list2json(list, jsonConfig);
        return NONE;
    }

}
