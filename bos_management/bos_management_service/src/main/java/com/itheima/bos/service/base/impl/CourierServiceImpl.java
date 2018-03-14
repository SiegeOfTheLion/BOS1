package com.itheima.bos.service.base.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;

/**
 * ClassName:CourierServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月14日 下午5:54:50 <br/>
 */
@Transactional
@Service
public class CourierServiceImpl implements CourierService {
    @Autowired
    private CourierRepository courierRepository;

    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);
    }

    /**
     * 分页查询
     */
    @Override
    public Page<Courier> findAll(Pageable pageable) {

        return courierRepository.findAll(pageable);
    }

    @Override
    public void batchdel(String ids) {
        // 真实开发中只有逻辑删除
        // 判断ids是否为空,不为空执行删除操作
        if (StringUtils.isNotEmpty(ids)) {
            // 根据什么来分割
            String[] split = ids.split(",");
            // 遍历
            for (String id : split) {
                courierRepository.updateDelTagById(Long.parseLong(id));
            }
        }
    }
    /**
     * 执行还原操作
     */
    @Override
    public void batchRes(String ids) {
          
     // 真实开发中只有逻辑删除
        // 判断ids是否为空,不为空执行删除操作
        if (StringUtils.isNotEmpty(ids)) {
            // 根据什么来分割
            String[] split = ids.split(",");
            // 遍历
            for (String id : split) {
                courierRepository.updateResTagById(Long.parseLong(id));
            }
        }
        
    }

}
