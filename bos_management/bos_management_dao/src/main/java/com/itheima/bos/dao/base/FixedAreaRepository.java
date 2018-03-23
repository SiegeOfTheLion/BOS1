package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.FixedArea;

/**  
 * ClassName:FixedAreaRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午7:54:10 <br/>       
 */
public interface FixedAreaRepository extends JpaRepository<FixedArea, Long> {
    
}
  
