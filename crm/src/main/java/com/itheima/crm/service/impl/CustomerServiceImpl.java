package com.itheima.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.crm.dao.CustomerRepository;
import com.itheima.crm.domain.Customer;
import com.itheima.crm.service.CustomerService;

/**  
 * ClassName:CustomerServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月18日 下午4:34:10 <br/>       
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
          
        return customerRepository.findCustomersUnAssociated();
    }
    
    
    /**
     * 查询关联到指定定区的用户
     */
    @Override
    public List<Customer> findCustomersAssociated2FixedArea(
            String fixedAreaId) {
          
        return customerRepository.findCustomersAssociated2FixedArea(fixedAreaId);
    }

}
  
