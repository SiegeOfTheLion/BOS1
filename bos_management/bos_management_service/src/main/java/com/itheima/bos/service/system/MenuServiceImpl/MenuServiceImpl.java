package com.itheima.bos.service.system.MenuServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.system.MenuDAO.MenuRepository;
import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.User;
import com.itheima.bos.service.system.MenuService.MenuService;

/**  
 * ClassName:MenuServiceImpl <br/>  
 * Function:  <br/>  
 * Date:     2018年3月28日 下午4:26:18 <br/>       
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> findAll() {
          
        return menuRepository.findByParentMenuIsNull();
    }

    @Override
    public void save(Menu menu) {
        menuRepository.save(menu);
    }

    @Override
    public Page<Menu> findAll(Pageable pageable) {
          
        return menuRepository.findAll(pageable);
    }
    /**
     * 动态加载菜单栏
     * TODO 简单描述该方法的实现功能（可选）.  
     * @see com.itheima.bos.service.system.MenuService.MenuService#findbyUser(com.itheima.bos.domain.system.User)
     */
    @Override
    public List<Menu> findbyUser(User user) {
          
        if ("admain".equals(user.getUsername())) {
            return menuRepository.findAll();
        }
        return menuRepository.findbyUser(user.getId());
    }
    
}
  
