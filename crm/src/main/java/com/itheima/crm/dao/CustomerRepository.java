package com.itheima.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

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
    List<Customer> findCustomersUnAssociated();

    /**
     * 查询关联到指定定区的用户 
     * 已经关联的话,那么需要指定定区的ID
     */
    List<Customer> findCustomersAssociated2FixedArea(String fixedAreaId);

}
