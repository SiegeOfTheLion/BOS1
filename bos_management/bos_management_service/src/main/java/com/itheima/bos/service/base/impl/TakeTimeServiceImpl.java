package com.itheima.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.TakeTimeService;

/**  
 * ClassName:TakeTimeService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月20日 下午1:40:40 <br/>       
 */
@Transactional
@Service
public class TakeTimeServiceImpl implements TakeTimeService {
    @Autowired
    private TakeTimeRepository takeTimeRepository;
    @Override
    public List<TakeTime> findAll() {
        return takeTimeRepository.findAll();
    }

}
  
