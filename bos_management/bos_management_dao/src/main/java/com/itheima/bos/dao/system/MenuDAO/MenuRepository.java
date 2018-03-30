package com.itheima.bos.dao.system.MenuDAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;

/**  
 * ClassName:MenuRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午4:18:46 <br/>       
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByParentMenuIsNull();
    @Query("select m from Menu m inner join m.roles r inner join r.users u where u.id = ?")
    List<Menu> findbyUser(Long id);
}
  
