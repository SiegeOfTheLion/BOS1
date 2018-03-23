package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;

/**  
 * ClassName:OrderRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月23日 下午12:17:48 <br/>       
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
  
