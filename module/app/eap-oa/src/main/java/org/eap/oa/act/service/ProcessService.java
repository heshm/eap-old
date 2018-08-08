package org.eap.oa.act.service;


import javax.servlet.http.HttpServletResponse;

import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.eap.oa.act.dto.ProcessQueryDTO;

public interface ProcessService {
	
	Page<ProcessQueryDTO> getPageProcessDefinitionList(Pageable pageable,String category);
	
	public void getResource(String processDefinitionId,String resourceType, HttpServletResponse response);
	
	public void deleteDeployment(String deploymentId);
	
	Page<ProcessInstance> getRunningInstance(Pageable pageable);
	
	boolean processDefinitionHasStartForm(String processDefinitionId);
	
	ProcessQueryDTO getOneProcessDefinition(String processDefinitionId);

}
