package com.itheima.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.StandardRepository;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.StandardService;

/**  
 * ClassName:StandardServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月14日 下午2:32:01 <br/>       
 */
@Transactional
@Service    //spring注解,业务层代码
public class StandardServiceImpl implements StandardService {
    @Autowired
    private StandardRepository standardRepository;
    /**
     * 保存
     */
    @Override
    public void save(Standard standard) {
        standardRepository.save(standard);
    }
    /**
     * 分页查询和局部查询
     */
    @Override
    public Page<Standard> findAll(Pageable pageable) {
          
        return standardRepository.findAll(pageable);
    }
    
}
  
