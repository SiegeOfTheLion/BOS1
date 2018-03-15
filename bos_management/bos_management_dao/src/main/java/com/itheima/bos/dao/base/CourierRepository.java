package com.itheima.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.base.Courier;

/**
 * ClassName:CourierRepository <br/>
 * Function: <br/>
 * Date: 2018年3月14日 下午5:56:42 <br/>
 */
// JpaSpecificationExecutor一般和JpaRepository一起使用
// 使用JpaSpecificationExecutor接口时需要传递两个参数
public interface CourierRepository extends JpaRepository<Courier, Long>,
        JpaSpecificationExecutor<Courier> {
    // 根据ID更改删除的标志位
    @Modifying
    @Query("update Courier set deltag = 1 where id = ?")
    void updateDelTagById(Long id);

    @Modifying
    @Query("update Courier set deltag = 0 where id = ?")
    void updateResTagById(Long id);
}
