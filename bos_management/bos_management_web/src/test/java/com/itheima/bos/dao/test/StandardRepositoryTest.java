package com.itheima.bos.dao.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itheima.bos.dao.base.StandardRepository;
import com.itheima.bos.domain.base.Standard;

/**  
 * ClassName:StandardRepositoryTest <br/>  
 * Function:  <br/>  
 * Date:     2018年3月12日 下午10:22:22 <br/>       
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class StandardRepositoryTest {
    @Autowired
    private StandardRepository standardRepository;
    
    @Test
    public void test() {
        List<Standard> list = standardRepository.findAll();
        for (Standard standard : list) {
            System.out.println(standard);
        }
    }

}
  
