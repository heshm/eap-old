package org.eap.common.authority.service;

import java.util.ArrayList;
import java.util.List;

/*import org.flowable.engine.IdentityService;
import org.flowable.idm.api.Group;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import org.eap.common.authority.dto.UserRoleDTO;
import org.eap.framework.domain.AdminRole;
import org.eap.framework.domain.UserRoleXref;
import org.eap.framework.mapper.AdminRoleMapper;
import org.eap.framework.mapper.UserRoleXrefMapper;

@Service
@Transactional(readOnly=true) 
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private AdminRoleMapper adminRoleMapper;
	@Autowired
	private UserRoleXrefMapper userRoleXrefMapper;
	/*@Autowired
	private IdentityService identityService;*/

	@Override
	public List<AdminRole> getAllRole() {
		List<AdminRole> result = adminRoleMapper.selectAll();
		return result;
	}

	@Override
	@Transactional
	public void saveRole(AdminRole role){
		String roleId = role.getRoleId();
		AdminRole dbRole = adminRoleMapper.selectByPrimaryKey(roleId);
		if(dbRole != null){
			throw new IllegalArgumentException("角色已经存在!");
		}
		adminRoleMapper.insert(role);
		//同步更新Activiti组信息
		/*Group group = identityService.newGroup(role.getRoleId());
		group.setName(role.getRoleName());
		group.setType(role.getRoleType());
		identityService.saveGroup(group);*/
	}

	@Override
	@Transactional
	public void deleteRole(String roleId) {
		adminRoleMapper.deleteByPrimaryKey(roleId);
		//identityService.deleteGroup(roleId);
	}

	@Override
	public UserRoleDTO getUserRole(String userId) {
		UserRoleDTO result = new UserRoleDTO(userId);
		if(StringUtils.isEmpty(userId)){
			return result;
		}
		List<String> roleList = new ArrayList<>(100);
		List<UserRoleXref> userRoleList = userRoleXrefMapper.selectList(userId, null);
		//roleList.addAll(userRoleList.stream().map(UserRoleXref::getRoleId).collect(Collectors.toList()));
		/*for(UserRoleXref userRole : userRoleList){
			roleList.add(userRole.getRoleId());
		}*/
		userRoleList.forEach(userRole -> {
			roleList.add(userRole.getRoleId());
		});
		result.setRoleList(roleList);
		return result;
	}

	@Override
	@Transactional
	public UserRoleDTO updateUserRole(UserRoleDTO userRole) {
		String userId = userRole.getUserId();
		if(StringUtils.isEmpty(userId)){
			throw new IllegalArgumentException("用户ID不能为空!");
		}
		List<UserRoleXref> parmList = new ArrayList<>(20);
		userRoleXrefMapper.deleteByUser(userId);

		/*identityService.createGroupQuery().groupMember(userId).list().forEach(member -> {
			identityService.deleteMembership(userId,member.getId());
		});*/

		userRole.getRoleList().forEach(role -> {
			parmList.add(new UserRoleXref(userId,role));
		});

		if(!parmList.isEmpty()){
			userRoleXrefMapper.insertList(parmList);
			/*parmList.forEach(parm ->{
				identityService.createMembership(userId,parm.getRoleId());
			});*/
		}
		return userRole;
	}

	@Override
	public List<AdminRole> getRoleByName(String nameFilter) {
		
		return adminRoleMapper.selectByName(nameFilter);
	}

}
