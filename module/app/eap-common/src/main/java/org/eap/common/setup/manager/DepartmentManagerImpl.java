package org.eap.common.setup.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.eap.common.setup.domain.Department;
import org.eap.common.setup.mapper.DepartmentMapper;

@Component
@Transactional(readOnly=true) 
public class DepartmentManagerImpl implements DepartmentManager {

	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Override
	public Department getOneDepartment(String id) {
		return departmentMapper.selectByPrimaryKey(id);
	}

}
