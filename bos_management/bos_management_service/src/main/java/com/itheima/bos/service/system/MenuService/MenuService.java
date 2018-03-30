package com.itheima.bos.service.system.MenuService;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;

/**  
 * ClassName:MenuService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午4:26:04 <br/>       
 */
public interface MenuService {

    List<Menu> findAll();

    void save(Menu model);

    Page<Menu> findAll(Pageable pageable);

    List<Menu> findbyUser(User user);


}
  
