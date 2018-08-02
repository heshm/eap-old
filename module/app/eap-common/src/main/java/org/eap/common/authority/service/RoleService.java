package org.eap.common.authority.service;

import java.util.List;

import org.eap.common.authority.dto.UserRoleDTO;
import org.eap.framework.domain.AdminRole;

public interface RoleService {
	
	List<AdminRole> getAllRole();
	
	void saveRole(AdminRole role);
	/**
	 * 根据角色ID删除相应的角色信息
	 * @param roleId
	 */
	void deleteRole(String roleId);
	/**
	 * 根据用户ID取得用户的角色
	 * @param userId
	 * @return UserRoleDTO
	 */
	UserRoleDTO getUserRole(String userId);
	/**
	 * 更新用户的角色
	 * @param userRole
	 * @return
	 */
	UserRoleDTO updateUserRole(UserRoleDTO userRole);
	
	List<AdminRole> getRoleByName(String nameFilter);

}
