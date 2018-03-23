package com.itheima.bos.service.base;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.FixedArea;

/**
 * ClassName:FixedAreaService <br/>
 * Function: <br/>
 * Date: 2018年3月18日 下午6:15:54 <br/>
 */
public interface FixedAreaService {
    void save(FixedArea fixedArea);
    
    Page<FixedArea> findAll(Pageable pageable);

    void associationCourierToFixedArea(Long fixedAreaId, Long courierId,
            Long takeTimeId);

    void assignSubArea2FixedArea(Long fixedAreaId, Long[] subAreaIds);
    
    
}
