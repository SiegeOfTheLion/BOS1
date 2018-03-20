package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.base.TakeTime;

/**
 * ClassName:TakeTimeRepository <br/>
 * Function: <br/>
 * Date: 2018年3月20日 下午1:37:06 <br/>
 */
public interface TakeTimeRepository extends JpaRepository<TakeTime, Long> {

}
