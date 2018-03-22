package com.itheima.bos.web.action.base;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.ui.Model;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ClassName:OrderAction <br/>
 * Function: <br/>
 * Date: 2018年3月22日 下午5:23:42 <br/>
 */
public class OrderAction extends ActionSupport implements ModelDriven<Order> {

    private Order model = new Order();

    @Override
    public Order getModel() {

        return model;
    }

    // 属性驱动获取到收件人和发件人的详细地址信息
    private String sendAreaInfo;
    private String recAreaInfo;

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }

    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }

    /**
     * 保存订单
     */
    @Action("orderAction_add")
    public String add() {
        
        System.out.println("执行了add方法.................");
        
       /* // 如果发件人的详细地址不为空的话
        if (StringUtils.isNotEmpty(sendAreaInfo)) {
            // 把数据保存到order中
            String[] split = sendAreaInfo.split("/");
            // 福建 莆田 仙游县 准确的地址
            // Area sendarea = new Area(split[0],split[1],split[2]);
            Area sendArea = new Area();
            sendArea.setProvince(split[0]);
            sendArea.setProvince(split[1]);
            sendArea.setProvince(split[2]);
            model.setSendArea(sendArea);
        }
        // 如果收件人的详细地址不为空的话
        if (StringUtils.isNotEmpty(recAreaInfo)) {
            //把收件人保存到order中
            String[] split = recAreaInfo.split(recAreaInfo);
            Area recArea = new Area();
            recArea.setProvince(split[0]);
            recArea.setProvince(split[1]);
            recArea.setProvince(split[2]);
            model.setRecArea(recArea);
        }*/

        return NONE;
    }

}
