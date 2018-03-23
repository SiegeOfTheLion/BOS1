package com.itheima.bos.fore.web.action;

import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
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
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
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
    @Action(value="orderAction_add",results={@Result(name="success",location="/index.html",type="redirect")})
    public String add() {

        // 如果发件人的详细地址不为空的话
        if (StringUtils.isNotEmpty(sendAreaInfo)) {
            // 把数据保存到order中
            String[] split = sendAreaInfo.split("/");
            String province = split[0];
            String city = split[1];
            String district = split[2];

            // 去掉省 市 区/县 这些字段
            province = province.substring(0, province.length() - 1);
            city = city.substring(0, city.length() - 1);
            //district = district.substring(0, district.length() - 1)+"区";
            // 福建 莆田 仙游 准确的地址
            Area Area = new Area();

            Area.setProvince(province);
            Area.setCity(city);
            Area.setDistrict(district);

            model.setSendArea(Area);
        }
        // 如果收件人的详细地址不为空的话
        if (StringUtils.isNotEmpty(recAreaInfo)) {
            // 把收件人保存到order中
            String[] split = recAreaInfo.split("/");
            String province = split[0];
            String city = split[1];
            String district = split[2];

            // 去掉省 市 区/县 这些字段
            province = province.substring(0, province.length() - 1);
            city = city.substring(0, city.length() - 1);
            //district = district.substring(0, district.length() - 1)+"区";
            Area area = new Area();
            // 封装数据
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            model.setRecArea(area);
        }

        // 保存发件和收件地址
        WebClient
                .create("http://localhost:8080/bos_management_web/webService/orderService/saveOrder")
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON).post(model);
        
        

        return SUCCESS;
    }

}
