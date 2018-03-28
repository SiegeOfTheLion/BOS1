package com.itheima.bos.service.system.MenuServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuDAO.PermissionRepository;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.service.system.MenuService.PermissionService;

/**  
 * ClassName:PermissionServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午8:14:52 <br/>       
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Override
    public Page<Permission> findAll(Pageable pageable) {
          
        return permissionRepository.findAll(pageable);
    }

    @Override
    public void save(Permission permission) {
        permissionRepository.save(permission); 
    }

}
  
