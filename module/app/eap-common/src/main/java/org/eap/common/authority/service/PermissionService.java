package org.eap.common.authority.service;

import java.util.List;

import org.eap.common.authority.dto.PermissionDTO;
import org.eap.common.authority.dto.RolePermissionDTO;
import org.eap.common.authority.dto.UserPermissionDTO;
import org.eap.framework.domain.Permission;

public interface PermissionService {
	
	Permission getOnePermission(String permId);
	
	List<PermissionDTO> getAllPermission();
	
	List<Permission> getNestedPermission();
	
	void savePermission(PermissionDTO perm);
	
	RolePermissionDTO getPermissionByRole(String roleId);
	
	RolePermissionDTO updateRolePermission(RolePermissionDTO rolePerm);
	
 	UserPermissionDTO getSpecialPermissionByUser(String userId);
 	
 	void updateUserPermission(UserPermissionDTO userPerm);

}
