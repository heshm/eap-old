package org.eap.framework.service;

import java.util.List;

import org.eap.framework.domain.AdminRole;
import org.eap.framework.domain.AdminUser;
import org.eap.framework.domain.Permission;

public interface EapSecService {
	
	Permission readPermissionById(String id);
	AdminRole readAdminRoleById(String roleId);
	AdminUser readAdminUserById(String userId);
	AdminUser readAdminUserByLoginName(String loginName);
	
	List<AdminUser> getAllUser();

}
