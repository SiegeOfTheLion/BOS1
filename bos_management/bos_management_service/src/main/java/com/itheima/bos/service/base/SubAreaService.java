package com.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.SubArea;

/**  
 * ClassName:SubAreaService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月16日 下午4:04:21 <br/>       
 */
public interface SubAreaService {

    void save(SubArea subArea);

    Page<SubArea> findAll(Pageable pageable);

}
  
