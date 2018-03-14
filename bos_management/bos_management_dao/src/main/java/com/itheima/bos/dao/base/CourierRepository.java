package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Courier;

/**
 * ClassName:CourierRepository <br/>
 * Function: <br/>
 * Date: 2018年3月14日 下午5:56:42 <br/>
 */
public interface CourierRepository extends JpaRepository<Courier, Long> {
    // 根据ID更改删除的标志位
    @Modifying
    @Query("update Courier set deltag = 1 where id = ?")
    void updateDelTagById(Long id);
    
    @Modifying
    @Query("update Courier set deltag = 0 where id = ?")
    void updateResTagById(Long id);
}
