package com.itheima.bos.web.action.base;

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

import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:SubAreaAction <br/>
 * Function: <br/>
 * Date: 2018年3月16日 下午3:52:54 <br/>
 */
@Controller
@ParentPackage("struts-default")
@Namespace("/")
@Scope("prototype")
public class SubAreaAction extends CommonAction<SubArea> {

    public SubAreaAction() {
        super(SubArea.class);
    }

    @Autowired
    private SubAreaService subAreaService;

    @Action(value = "subareaAction_save", results = @Result(name = "success",
            location = "/pages/base/sub_area.html", type = "redirect"))
    public String save() {
        // 保存数据,所以不需要传值

        subAreaService.save(getModel());

        return SUCCESS;
    }

    /**
     * 分页查询
     * @throws IOException 
     */
    @Action("subAreaAction_pageQuery")
    public String pageQuery() throws IOException {
        
        Pageable pageable = new PageRequest(page-1, rows);
        //调用业务层
        Page<SubArea> page = subAreaService.findAll(pageable);
        //灵活控制输出
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"area","fixedArea"});
        page2json(page, jsonConfig);
        return NONE;
    }

}
