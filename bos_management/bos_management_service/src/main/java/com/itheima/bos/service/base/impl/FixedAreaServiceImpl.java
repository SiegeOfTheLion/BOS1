package com.itheima.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.FixedAreaService;

/**
 * ClassName:FixedAreaServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月18日 下午7:52:05 <br/>
 */
@Transactional
@Service
public class FixedAreaServiceImpl implements FixedAreaService {

    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private TakeTimeRepository takeTimeRepository;

    @Override
    public void save(FixedArea fixedArea) {

        fixedAreaRepository.save(fixedArea);
    }

    @Override
    public Page<FixedArea> findAll(Pageable pageable) {
        return fixedAreaRepository.findAll(pageable);
    }

    @Override
    public void associationCourierToFixedArea(Long fixedAreaId, Long courierId,
            Long takeTimeId) {

        // 代码执行成功以后,快递员表发生update操作,快递员和定区中间表会发生insert操作
        FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
        Courier courier = courierRepository.findOne(courierId);
        TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);

        // 建立时间和快递员的关系
        courier.setTakeTime(takeTime);

        // 建立定区id和快递员的关系
        // 建立快递员和定区的关联
        // 下面的写法是错的,因为在Courier实体类中fixedAreas字段的上方添加了mappedBy属性
        // 就代表快递员放弃了对关系的维护
        // courier.getFixedAreas().add(fixedArea);
        fixedArea.getCouriers().add(courier);

    }

}
