package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.take_delivery.WorkBill;

/**  
 * ClassName:WorkBillRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月23日 下午5:22:15 <br/>       
 */
public interface WorkBillRepository extends JpaRepository<WorkBill, Long> {
    
}
  
