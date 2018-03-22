package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;

/**
 * ClassName:SubAreaRepository <br/>
 * Function: <br/>
 * Date: 2018年3月16日 下午4:08:48 <br/>
 */
public interface SubAreaRepository extends JpaRepository<SubArea, Long> {
    /**
     * 查询未关联
     */
    List<SubArea> findByFixedAreaIsNull();
    
    /**
     * 查询已关联的
     */
    List<SubArea> findByFixedArea(FixedArea fixedArea);

}
