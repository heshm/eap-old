package org.eap.common.authority.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.eap.common.authority.dto.AdminUserDTO;

public interface AdminUserService {
	
	AdminUserDTO getOneAdminUser(String id);
	
	Page<AdminUserDTO> getPageAdminUser(Pageable pageable,Map<String,Object> param);
	
	void createAdminUser(AdminUserDTO adminUser);
	
	void updateAdminUser(AdminUserDTO adminUser);
	
	List<AdminUserDTO> getUsers(String filter);

}
