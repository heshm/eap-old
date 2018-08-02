package org.eap.common.authority.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*import org.flowable.engine.IdentityService;
import org.flowable.idm.api.User;*/
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.eap.common.authority.dto.AdminUserDTO;
import org.eap.common.authority.mapper.AdminUserDTOMapper;
import org.eap.common.setup.domain.DepartmentUserXref;
import org.eap.common.setup.mapper.DepartmentUserXrefMapper;
import org.eap.framework.domain.AdminUser;
import org.eap.framework.mapper.AdminUserMapper;
import org.eap.framework.web.util.AuthenticationUtils;

@Service
@Transactional(readOnly=true) 
public class AdminUserServiceImpl implements AdminUserService{
	
	@Autowired
	private AdminUserDTOMapper adminUserDTOMapper;
	
	@Autowired
	private AdminUserMapper adminUserMapper;
	
	@Autowired
	private DepartmentUserXrefMapper departmentUserXrefMapper;
	
	/*@Autowired
	private IdentityService identityService;*/
	
	@Value("${eap.defaultPassword}")
	private String password;

	@Override
	public AdminUserDTO getOneAdminUser(String id) {
		AdminUserDTO adminUser = adminUserDTOMapper.selectOne(id);
		if(adminUser == null) 
			return null;
		List<DepartmentUserXref> list = departmentUserXrefMapper.selectList(null, id);
		if(list != null) {
			String[] deptIds = new String[list.size()];
			for(int i = 0; i < list.size(); i++) {
				deptIds[i] = list.get(i).getDepartmentId();
			}
			adminUser.setDepartmentIds(deptIds);
		}
		return adminUser;
	}

	@Override
	public Page<AdminUserDTO> getPageAdminUser(Pageable pageable, Map<String, Object> param) {
		param.put("pageable", pageable);
		List<AdminUserDTO> list = adminUserDTOMapper.selectList(param);
		int total = adminUserDTOMapper.countByExample(param);
		return new PageImpl<>(list,pageable,total);
	}

	@Override
	@Transactional
	public void createAdminUser(AdminUserDTO adminUser) {
		AdminUser dbUser = adminUserMapper.selectByLoginName(adminUser.getLoginName());
		if(dbUser != null) {
			throw new IllegalArgumentException("用户信息已经存在!");
		}
		Date currentDate = new Date();
		AdminUser user = new AdminUser();
		BeanUtils.copyProperties(adminUser, user);
		user.setUserId(adminUser.getLoginName());
		user.setCreateDate(currentDate);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(password));
		adminUserMapper.insert(user);
		
		saveDeptUserXref(adminUser);
		
		//saveActUser(user);
		
	}
	
	@Override
	@Transactional
	public void updateAdminUser(AdminUserDTO adminUser) {
		AdminUser user = adminUserMapper.selectByLoginName(adminUser.getLoginName());
		user.setActiveStatusFlag(adminUser.getActiveStatusFlag());
		user.setUserName(adminUser.getUserName());
		user.setPhoneNumber(adminUser.getPhoneNumber());
		user.setEmail(adminUser.getEmail());
		user.setRoleName(adminUser.getRoleName());
		adminUserMapper.updateByPrimaryKey(user);
		saveDeptUserXref(adminUser);
		//System.out.println(identityService.createUserQuery());
		//User actUser = identityService.createUserQuery().userId(user.getLoginName()).singleResult();
		//if(actUser == null){
		//	saveActUser(user);
		//}
		
	}
	
	private void saveDeptUserXref(AdminUserDTO adminUser) {
		departmentUserXrefMapper.deleteByUserId(adminUser.getLoginName());
		String currentUserId = AuthenticationUtils.getUserId();
		Date currentDate = new Date();
		DepartmentUserXref deptUser = new DepartmentUserXref();
		deptUser.setUserId(adminUser.getLoginName());
		deptUser.setCreateTime(currentDate);
		deptUser.setUpdateTime(currentDate);
		deptUser.setCreateBy(currentUserId);
		deptUser.setUpdateBy(currentUserId);
		Arrays.stream(adminUser.getDepartmentIds()).forEach(deptId -> {
			deptUser.setDepartmentId(deptId);
			departmentUserXrefMapper.insert(deptUser);
		});
		
	}

	@Override
	public List<AdminUserDTO> getUsers(String filter) {
		Map<String,Object> filterMap = new HashMap<>();
		filterMap.put("userName", filter);
		List<AdminUserDTO> list = adminUserDTOMapper.selectList(filterMap);
		return list;
	}

	
	/*private void saveActUser(AdminUser user) {
		//identityService.
		User actUser = identityService.newUser(user.getUserId());
		actUser.setPassword(user.getPassword());
		actUser.setEmail(user.getEmail());
		actUser.setFirstName("Flowable");
		identityService.saveUser(actUser);
	}*/

	

}
