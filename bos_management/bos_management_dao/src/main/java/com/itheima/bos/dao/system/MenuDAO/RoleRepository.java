package com.itheima.bos.dao.system.MenuDAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.system.Role;

/**  
 * ClassName:RoleRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午8:57:27 <br/>       
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    @Query("select r from Role r inner join r.users u where u.id = ?")
    List<Role> findbyUid(Long id);
}
  
