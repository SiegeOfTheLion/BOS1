package com.itheima.bos.service.reamls;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itheima.bos.dao.system.UserRepository;
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

    // 授权的方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection arg0) {
        // SimpleAuthorizationInfo:简单的授权信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 给admain授权具备修改查看courierAction_pageQuery方法
        info.addStringPermission("courierAction_pageQuery");
        info.addRole("admain");
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
