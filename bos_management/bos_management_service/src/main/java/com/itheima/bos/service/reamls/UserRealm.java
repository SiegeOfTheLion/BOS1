package com.itheima.bos.service.reamls;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.system.UserRepository;
import com.itheima.bos.dao.system.MenuDAO.PermissionRepository;
import com.itheima.bos.dao.system.MenuDAO.RoleRepository;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.domain.system.User;

/**
 * ClassName:UserRealm <br/>
 * Function: <br/>
 * Date: 2018年3月26日 下午4:50:20 <br/>
 */
@Component  //注给spring管理
public class UserRealm extends AuthorizingRealm {
    // AuthorizingRealm:授权领域
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    // 授权的方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection arg0) {
        // SimpleAuthorizationInfo:简单的授权信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 根据当前用户查询权限和角色
        // 怎么获取当前用户呢?shiro框架提供了SecurityUtils.getSubject()方法用于获取subject即是当前对象
        Subject subject = SecurityUtils.getSubject();
        // 当前对象
        User user = (User) subject.getPrincipal();
        // 如果当前用户名是超级管理员的话,授予所有权限和角色
        if ("admain".equals(user.getUsername())) {
            // 去数据库查询所有角色和权限
            // 获取权限
            List<Permission> permissions = permissionRepository.findAll();
            for (Permission permission : permissions) {
                // 把权限添加到超级管理员身上
                info.addStringPermission(permission.getKeyword());
            }
            // 获取角色
            List<Role> roles = roleRepository.findAll();
            for (Role role : roles) {
                // 把权限添加到超级管理员身上
                info.addStringPermission(role.getKeyword());
            }
        }else {
            // 普通用户(这个uid不是permission的id,也不是role的id,而是用户名的id)
            // 获取权限
            List<Permission> permissions = permissionRepository.findbyUid(user.getId());
            for (Permission permission : permissions) {
                // 把权限添加到超级管理员身上
                info.addStringPermission(permission.getKeyword());
            }
            // 获取角色
            List<Role> roles = roleRepository.findbyUid(user.getId());
            for (Role role : roles) {
                // 把权限添加到超级管理员身上
                info.addStringPermission(role.getKeyword());
            }
        }
        return info;
    }

    // 认证的方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        // 获取
        UsernamePasswordToken usernamePasswordToken =
                (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        // 根据用户名查询用户
        User user = userRepository.findByUsername(username);
        // 根据用户名查找用户
        if (user != null) {
            /**
             * @param principal 当事人,主体.通常是从数据库中查询到的用户
             * @param credentials 凭证,密码.是从数据库中查询出来的密码
             * @param realmName
             */
            // 找到 -> 比较密码
            AuthenticationInfo info = new SimpleAuthenticationInfo(user,
                    user.getPassword(), getName());
            // 找不到 -> 抛出异常
            // 比较成功 -> 执行后续的逻辑
            return info;
        }

        // 比较失败 -> 抛出异常
        return null;
    }

}
