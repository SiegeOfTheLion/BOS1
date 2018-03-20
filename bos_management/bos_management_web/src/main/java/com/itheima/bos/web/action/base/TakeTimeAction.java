package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.TakeTimeService;
import com.itheima.bos.web.action.CommonAction;

/**  
 * ClassName:TakeTimeAction <br/>  
 * Function:  <br/>  
 * Date:     2018年3月20日 下午1:39:33 <br/>       
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class TakeTimeAction extends CommonAction<TakeTime> {
    
    @Autowired
    private TakeTimeService takeTimeService;

    public TakeTimeAction() {
          
        super(TakeTime.class);  
    }
    @Action("takeTimeAction_findTimeWork")
    public String findTimeWork() throws IOException{
        
        List<TakeTime> list = takeTimeService.findAll();
        list2json(list, null);
        return NONE;
    }

}
  
