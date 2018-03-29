package com.itheima.bos.service.system.MenuService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;

/**  
 * ClassName:UserService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月29日 下午4:23:17 <br/>       
 */
public interface UserService {
    void save(User user, Long[] roleIds);

    Page<User> findAll(Pageable pageable);
}
  
