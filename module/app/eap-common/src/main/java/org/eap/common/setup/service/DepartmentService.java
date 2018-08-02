package org.eap.common.setup.service;

import org.eap.common.setup.dto.DepartmentDTO;

public interface DepartmentService {
	
	DepartmentDTO getOneDepartment(String id);
	
	DepartmentDTO getNestedDepartment(String id);
	
	String create(DepartmentDTO dept);
	
	void create(DepartmentDTO dept,String[] params);
	
	void update(DepartmentDTO dept);
}
