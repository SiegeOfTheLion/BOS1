package com.itheima.bos.service.system.MenuServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuDAO.RoleRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.MenuService.RoleService;

/**
 * ClassName:RoleService <br/>
 * Function: <br/>
 * Date: 2018年3月28日 下午8:54:10 <br/>
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<Role> findAll(Pageable pageable) {

        return roleRepository.findAll(pageable);
    }

    @Override
    public void save(Role role, String menuIds, Long[] permissionIds) {
        roleRepository.save(role);
        if (StringUtils.isNotEmpty(menuIds)) {
            String[] split = menuIds.split(",");
            for (String menuId : split) {
                Menu menu = new Menu();
                menu.setId(Long.parseLong(menuId));
                role.getMenus().add(menu);
            }
        }

        if (permissionIds != null && permissionIds.length > 0) {
            for (Long permissionId : permissionIds) {
                Permission permission = new Permission();
                permission.setId(permissionId);
                role.getPermissions().add(permission);
            }
        }

    }

}
