package org.eap.framework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.eap.framework.domain.AdminRole;
import org.eap.framework.domain.AdminUser;
import org.eap.framework.domain.Permission;
import org.eap.framework.domain.RolePermissionXref;
import org.eap.framework.domain.UserPermissionXref;
import org.eap.framework.domain.UserRoleXref;
import org.eap.framework.mapper.AdminRoleMapper;
import org.eap.framework.mapper.AdminUserMapper;
import org.eap.framework.mapper.PermissionMapper;
import org.eap.framework.mapper.RolePermissionXrefMapper;
import org.eap.framework.mapper.UserPermissionXrefMapper;
import org.eap.framework.mapper.UserRoleXrefMapper;

@Service
@Transactional(readOnly=true)
public class EapSecServiceImpl implements EapSecService{

	@Autowired
	private PermissionMapper permissionMapper;
	
	@Autowired
	private AdminRoleMapper adminRoleMapper;

	@Autowired
	private RolePermissionXrefMapper rolePermissionXrefMapper;
	
	@Autowired
	private AdminUserMapper adminUserMapper;
	
	@Autowired
	private UserRoleXrefMapper userRoleXrefMapper;

	@Autowired
	private UserPermissionXrefMapper userPermissionXrefMapper;
	
	@Override
	public Permission readPermissionById(String id) {
		return permissionMapper.selectByPermissionId(id);
	}

	@Override
	public AdminRole readAdminRoleById(String roleId) {
		AdminRole role = adminRoleMapper.selectByPrimaryKey(roleId);
		if (null != role) {
			List<RolePermissionXref> rolePermissionList = rolePermissionXrefMapper.selectList(roleId, null);
			if (null != rolePermissionList) {
				for (RolePermissionXref item : rolePermissionList) {
					Permission perm = readPermissionById(item.getPermissionId());
					if (null != perm){
						role.getAllPermissions().add(perm);
					}
				}
			}
		}
		return role;
	}

	@Override
	public AdminUser readAdminUserById(String userId) {
		AdminUser adminUser = adminUserMapper.selectByPrimaryKey(userId);
		readOtherUserInfo(adminUser);
		return adminUser;
	}

	@Override
	public AdminUser readAdminUserByLoginName(String loginName) {
		AdminUser adminUser = adminUserMapper.selectByLoginName(loginName);
		readOtherUserInfo(adminUser);
		return adminUser;
	}
	
	@Override
	//@PreAuthorize("#oauth2.clientHasRole('ROLE_ADMIN')")
	
	public List<AdminUser> getAllUser() {
		// TODO Auto-generated method stub
		return adminUserMapper.selectAll();
	}

	
	private void readOtherUserInfo(AdminUser adminUser){
		if (null != adminUser) {
			String userId = adminUser.getUserId();
			List<UserRoleXref> userRoleList = userRoleXrefMapper.selectList(userId, null);
			if (null != userRoleList) {
				for (UserRoleXref item : userRoleList) {
					AdminRole role = readAdminRoleById(item.getRoleId());
					if (null != role){
						adminUser.getAllRoles().add(role);
					}
				}
			}
			List<UserPermissionXref> UserPermissionList = userPermissionXrefMapper.selectList(userId, null);
			if (null != UserPermissionList) {
				for (UserPermissionXref item : UserPermissionList) {
					Permission perm = this.readPermissionById(item.getPermissionId());
					if(null != perm){
					    adminUser.getAllPermissions().add(perm);
					}
				}
			}
		}
	}

}
