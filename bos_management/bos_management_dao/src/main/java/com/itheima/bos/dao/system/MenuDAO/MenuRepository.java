package com.itheima.bos.dao.system.MenuDAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.system.Menu;

/**  
 * ClassName:MenuRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午4:18:46 <br/>       
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByParentMenuIsNull();
}
  
