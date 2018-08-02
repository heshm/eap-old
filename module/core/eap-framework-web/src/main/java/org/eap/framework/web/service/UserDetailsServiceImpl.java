package org.eap.framework.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.eap.framework.domain.AdminRole;
import org.eap.framework.domain.AdminUser;
import org.eap.framework.domain.Permission;
import org.eap.framework.service.EapSecService;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final Byte STATUS_INVALID = 0;
	
	@Autowired
	private EapSecService securityService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		AdminUser adminUser = securityService.readAdminUserByLoginName(username);
		if(adminUser == null || adminUser.getActiveStatusFlag() == STATUS_INVALID){
			throw new UsernameNotFoundException("该用户不存在!");
		}
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(AdminRole role : adminUser.getAllRoles()){
			addPermissions(authorities,role.getAllPermissions());
		}
		addPermissions(authorities,adminUser.getAllPermissions());
		
		if(logger.isDebugEnabled()){
			String s = "[";
			for (GrantedAuthority auth : authorities){
				s = s + auth.getAuthority() + ",";
			}
			s = s + "]";
			logger.debug("{}`s authorities is {} ", username, s);
		}
		/*UserDetails userDetails = User.withDefaultPasswordEncoder()
				.username(adminUser.getLoginName())
				.password(adminUser.getPassword())
				.authorities(authorities)
				.build();*/
		/*User.withUsername(adminUser.getLoginName())
			.password(adminUser.getPassword())
			.authorities(authorities)
			.build();*/
		//System.out.println(userDetails.getPassword());
		return new User(adminUser.getLoginName(),adminUser.getPassword(),authorities);
	}
	
	private void addPermissions(List<GrantedAuthority> authorities,Set<Permission> permissions){
		for(Permission permission : permissions){
			if(permission.getIsFriendly()) {
				for (Permission childPermission : permission.getChildren()) {
					authorities.add(new SimpleGrantedAuthority(childPermission.getPermissionName()));
				}
			} else {
				authorities.add(new SimpleGrantedAuthority(permission.getPermissionName()));
			}
		}
	}

}
