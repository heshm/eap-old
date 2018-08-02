package org.eap.developer.service;

import java.util.List;

import org.eap.developer.dto.ApplicationDTO;

public interface ApplicationService {
	
	ApplicationDTO readOneApplication(String id);
	
	List<ApplicationDTO> readAllApplication();
	

}
