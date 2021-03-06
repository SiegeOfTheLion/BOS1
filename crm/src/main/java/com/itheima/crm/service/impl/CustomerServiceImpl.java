package com.itheima.crm.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.crm.dao.CustomerRepository;
import com.itheima.crm.domain.Customer;
import com.itheima.crm.service.CustomerService;

/**
 * ClassName:CustomerServiceImpl <br/>
 * Function: <br/>
 * Date: 2018年3月18日 下午4:34:10 <br/>
 */
@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {

        return customerRepository.findAll();
    }

    /**
     * 查询未关联定区的用户
     */
    @Override
    public List<Customer> findCustomersUnAssociated() {

        return customerRepository.findByFixedAreaIdIsNull();
    }

    /**
     * 查询关联到指定定区的用户 findCustomersAssociated2FixedArea
     */
    @Override
    public List<Customer> findCustomersAssociated2FixedArea(
            String fixedAreaId) {
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }

    @Override
    public void associatedCustomer2FixedArea(String fixedAreaId,
            Long[] customerIds) {
        // 根据定区ID,把关联到这个定区的所有客户解绑
        if (StringUtils.isNotEmpty(fixedAreaId)) {
            customerRepository.unbindCustomerByfixedAreaId(fixedAreaId);
        }

        // 把要关联的数据和定区id进行绑定
        if (customerIds != null && fixedAreaId.length() > 0) {
            for (Long customerId : customerIds) {
                customerRepository.bindCustomer2FixedArea(fixedAreaId,
                        customerId);
            }

        }
    }

    /**
     * 保存注册的用户
     */
    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }
    
    /**
     * 激活
     */
    @Override
    public void active(String telephone) {
          
        customerRepository.active(telephone);
    }
    /**
     * 是否激活
     * TODO 简单描述该方法的实现功能（可选）.  
     * @see com.itheima.crm.service.CustomerService#isActive(java.lang.String)
     */
    @Override
    public Customer isActive(String telephone) {
          
        return customerRepository.findByTelephone(telephone);
    }
    /**
     * 登录
     * TODO 简单描述该方法的实现功能（可选）.  
     * @see com.itheima.crm.service.CustomerService#login(java.lang.String, java.lang.String)
     */
    @Override
    public Customer login(String telephone, String password) {
        return customerRepository.findByTelephoneAndPassword(telephone, password);
    }
    /**
     * 异步校验是否已经纯在手机号
     * TODO 简单描述该方法的实现功能（可选）.  
     * @return 
     * @see com.itheima.crm.service.CustomerService#lostBlurs(java.lang.String)
     */
    @Override
    public Customer lostBlurs(String telephone) {
          
        return customerRepository.findByTelephone(telephone);
    }

    @Override
    public String findFixedAreaIdByAddress(String address) {
        return customerRepository.findFixedAreaIdByAddress(address);
    }
    
    
    
    
}
