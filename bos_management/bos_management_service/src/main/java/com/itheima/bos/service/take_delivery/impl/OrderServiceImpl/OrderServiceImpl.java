package com.itheima.bos.service.take_delivery.impl.OrderServiceImpl;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.OrderRepository;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.dao.base.WorkBillRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.domain.take_delivery.WorkBill;
import com.itheima.bos.service.take_delivery.OrderService.OrderService;

/**
 * ClassName:OrderServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月23日 下午12:19:28 <br/>
 */
@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Autowired
    private WorkBillRepository workBillRepository;
    @Autowired
    private SubAreaRepository subAreaRepository;

    @Override
    public void saveOrder(Order order) {

        // 因为Area在前端是被new出来的所以是一个瞬时态,而不是从数据库中查出来的,所以没有ID
        Area sendArea = order.getSendArea();
        // 如果发件人地址不为空的话
        if (sendArea != null) {
            // 那么就把省/市/区存进去
            Area sendAreaOB = areaRepository.findByProvinceAndCityAndDistrict(
                    sendArea.getProvince(), sendArea.getCity(),
                    sendArea.getDistrict());
            System.out.println();
            order.setSendArea(sendAreaOB);
        }
        // 把瞬时态转变成持久态
        Area recArea = order.getRecArea();
        if (recArea != null) {
            Area recAreaOB = areaRepository.findByProvinceAndCityAndDistrict(
                    recArea.getProvince(), recArea.getCity(),
                    recArea.getDistrict());
            System.out.println(recAreaOB);
            order.setRecArea(recAreaOB);
        }
        // 保存订单
        // 订单编号
        order.setOrderNum(UUID.randomUUID().toString().replace("-", ""));
        order.setOrderTime(new Date());
        orderRepository.save(order);

        // 自动分单
        // 根据发件地址完全匹配
        // 需要获取发件地址sendAddress
        String sendAddress = order.getSendAddress();
        // 根据发件地址查询定区ID
        if (StringUtils.isNotEmpty(sendAddress)) {
            // 查询
            String fixedAreaId = WebClient
                    .create("http://localhost:8180/crm/webService/customerService/findFixedAreaIdByAddress")
                    .accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .query("address", sendAddress).get(String.class);
            // 根据定区id查询定区
            if (fixedAreaId != null) {
                FixedArea fixedArea = fixedAreaRepository
                        .findOne(Long.parseLong(fixedAreaId));
                if (fixedArea != null) {

                    // 根据定区关联的快递员
                    Set<Courier> couriers = fixedArea.getCouriers();
                    if (couriers != null) {
                        // 使用迭代器获取第一个快递员
                        Iterator<Courier> iterator = couriers.iterator();
                        Courier courier = iterator.next();
                        // 指定快递员
                        order.setCourier(courier);
                        // 生成工单
                        WorkBill workBill = new WorkBill();
                        workBill.setOrder(order);
                        workBill.setPickstate("新单");
                        workBill.setBuildtime(new Date());
                        workBill.setAttachbilltimes(0);
                        workBill.setRemark(order.getRemark());
                        workBill.setCourier(courier);
                        workBill.setSmsNumber("java是世界上最好的语言??????");
                        workBill.setType("新");
                        // 把工单保存起来
                        order.setOrderType("自动分单");
                        workBillRepository.save(workBill);
                        return;
                    }
                }
            } else {
                // 根据发件地址对关键字进行模糊查询(sendArea)
                // 根据sendArea查询
                Area sendArea2 = order.getSendArea();
                if (sendArea2 != null) {
                    // 根据发货地址查询所以分区
                    Set<SubArea> subareas = sendArea2.getSubareas();
                    // 遍历获取分区ID
                    for (SubArea subArea : subareas) {
                        Long subAreaId = subArea.getId();
                        // 通过分区获取关键字
                        String assistKeyWords = subArea.getAssistKeyWords();// 辅助关键字
                        String keyWords = subArea.getKeyWords();// 关键字
                        // 通过关键字和sendAddress进行模糊查询
                        if (sendAddress.contains(keyWords)
                                || sendAddress.contains(assistKeyWords)) {
                            // 如果地址包含这关键字或是辅助关键字,那就就查询定区id
                            FixedArea fixedArea = subArea.getFixedArea();
                            if (fixedArea != null) {
                                // 根据定区和快递员关联获取快递员
                                Set<Courier> couriers = fixedArea.getCouriers();
                                // 使用迭代器获取第一个快递员
                                if (!couriers.isEmpty()) {
                                    Iterator<Courier> iterator =
                                            couriers.iterator();
                                    Courier courier = iterator.next();
                                    // 把快递员放在订单里面
                                    order.setCourier(courier);
                                    // 创建工单把
                                    WorkBill workBill = new WorkBill();
                                    
                                    workBill.setOrder(order);
                                    workBill.setPickstate("新单");
                                    workBill.setBuildtime(new Date());
                                    workBill.setAttachbilltimes(0);
                                    workBill.setRemark(order.getRemark());
                                    workBill.setCourier(courier);
                                    workBill.setSmsNumber(
                                            "java是世界上最好的语言??????");
                                    workBill.setType("新");
                                    // 自动分单
                                    order.setOrderType("自动分单");
                                    // 保存工单
                                    workBillRepository.save(workBill);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        order.setOrderType("人工分单");
    }

}
