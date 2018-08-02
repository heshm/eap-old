package org.eap.common.authority.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.eap.common.authority.dto.PermissionDTO;
import org.eap.common.authority.dto.RolePermissionDTO;
import org.eap.common.authority.dto.UserPermissionDTO;
import org.eap.common.authority.service.PermissionService;
import org.eap.framework.domain.Permission;
import org.eap.framework.web.endpoint.BaseEndpoint;

@RestController
@RequestMapping("/common/authority/perm")
public class PermissionEndpoint extends BaseEndpoint {
	
	@Autowired
	private PermissionService permissionService;
	
	@GetMapping("/tree_list")
	public List<Permission> treeList(){
		return permissionService.getNestedPermission();
	}
	
	@GetMapping("/list")
	public List<PermissionDTO> list(){
		return permissionService.getAllPermission();
	}
	
	@GetMapping("/role_perm/{roleId}")
	public RolePermissionDTO rolePerm(@PathVariable("roleId")String roleId){
		return permissionService.getPermissionByRole(roleId);
	}
	
	@PostMapping("/role_perm")
	public RolePermissionDTO rolePerm(@RequestBody RolePermissionDTO rolePerm){
		permissionService.updateRolePermission(rolePerm);
		return rolePerm;
	}
	
	@GetMapping("/user_perm/{userId}")
	public UserPermissionDTO userPerm(@PathVariable("userId")String userId){
		return permissionService.getSpecialPermissionByUser(userId);
	}
	
	@PostMapping("/user_perm")
	public String userPerm(@RequestBody UserPermissionDTO userPerm){
		permissionService.updateUserPermission(userPerm);
		return UPDATED;
	}
	
	@PostMapping("/create")
	public String create(@RequestBody PermissionDTO perm){
		permissionService.savePermission(perm);
		return CREATED;
	}

}
