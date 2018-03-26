package com.itheima.bos.service.take_deliverys.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.WaybillRepository;
import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_deliverys.WaybillService;

/**  
 * ClassName:WayBillServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午8:13:54 <br/>       
 */
@Service
@Transactional
public class WaybillServiceImpl implements WaybillService {
    
    @Autowired
    private WaybillRepository waybillRepository;
    
    @Override
    public void save(WayBill wayBill) {
        waybillRepository.save(wayBill);
    }

}
  
