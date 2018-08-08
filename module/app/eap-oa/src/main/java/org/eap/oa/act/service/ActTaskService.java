package org.eap.oa.act.service;

import java.util.List;
import java.util.Map;

import org.eap.oa.act.dto.CreateProcessInstanceRepresentation;
import org.eap.oa.act.dto.TaskDTO;
import org.eap.oa.act.dto.TaskFormDTO;

public interface ActTaskService {
	
	void startProcessInstance(CreateProcessInstanceRepresentation startRequest);
	
	List<TaskDTO> getToDoTask(String userId);

	List<TaskDTO> getDoingTask(String userId);
	
	List<TaskDTO> getTasks(String userId);

	TaskDTO getOneTask(String taskId);
	
	List<TaskDTO> listTasks(String processInstanceId,String state);
	
	void completeTask(String taskId);
	
	void completeTask(String taskId,Map<String, Object> variables);
	
	void claimTask(String taskId);
	
	TaskFormDTO getTaskForm(String taskId);
}
