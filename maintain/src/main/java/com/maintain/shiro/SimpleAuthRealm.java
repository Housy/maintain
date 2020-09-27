package com.maintain.shiro;

import com.maintain.common.Constant;
import com.maintain.po.User;
import com.maintain.util.InterfaceContainer;
import com.maintain.util.WebUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 自定义的验证实现
 */
public class SimpleAuthRealm extends AuthorizingRealm{

	/**
	 * 授权
	 * 访问有权限保护的资源时候，会调用此方法进行角色或
	 * 权限的验证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		User user = WebUtil.getLoginedUser();
		//添加角色
		info.addRole(user.getRole());
		return info;
	}

	/**
	 * 认证
	 * 就是登录
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		String username = token.getUsername();
		User user = null;
		//查询用户信息
		try{
			user = InterfaceContainer.getInstance().getUserService().findByUsername(username);
		}catch(Exception e) {
			e.printStackTrace();
			throw new AuthenticationException(Constant.ERROR_MSG_DUBBO_ERROR);
		}
		if(user == null){
			throw new AuthenticationException("用户名或密码错误");
		}
		return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());

	}

}
