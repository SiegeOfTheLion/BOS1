package com.itheima.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.crm.domain.Customer;

/**
 * ClassName:CustomerRepository <br/>
 * Function: <br/>
 * Date: 2018年3月18日 下午4:08:43 <br/>
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * 查询未关联定区的用户
     */
    List<Customer> findByFixedAreaIdIsNull();

    /**
     * 查询关联到指定定区的用户 已经关联的话,那么需要指定定区的ID
     */
    List<Customer> findByFixedAreaId(String fixedAreaId);

    /**
     * 解绑
     */
    @Modifying
    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
    void unbindCustomerByfixedAreaId(String fixedAreaId);

    /**
     * 绑定
     */
    @Modifying
    @Query("update Customer set fixedAreaId = ? where id = ?")
    void bindCustomer2FixedArea(String fixedAreaId, Long customerId);

}
