package com.itheima.bos.dao.system.MenuDAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itheima.bos.domain.system.Permission;

/**  
 * ClassName:PermissionRepository <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午8:17:07 <br/>       
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    // SELECT
    // *
    // FROM
    // t_permission p
    // INNER JOIN t_role_permission rp ON p.C_ID = rp.C_PERMISSION_ID
    // INNER JOIN t_role r ON r.C_ID = rp.C_ROLE_ID
    // INNER JOIN t_user_role ur ON ur.C_ROLE_ID = r.C_ID
    // INNER JOIN t_user u ON u.C_ID = ur.C_USER_ID
    // WHERE
    // u.C_ID = 200;
    
    @Query("select p from Permission p inner join p.roles r inner join r.users u where u.id = ?")
    List<Permission> findbyUid(Long id);
}
  
