package org.eap.oa.act.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import org.eap.framework.web.endpoint.BaseEndpoint;
import org.eap.framework.web.util.AuthenticationUtils;
import org.eap.oa.act.dto.CreateProcessInstanceRepresentation;
import org.eap.oa.act.dto.TaskDTO;
import org.eap.oa.act.dto.TaskFormDTO;
import org.eap.oa.act.service.ActTaskService;

@RestController
@RequestMapping(value = "/oa/act/task")
public class TaskEndpoint extends BaseEndpoint{
	
	@Autowired
	private ActTaskService actTaskService;
	
	@PostMapping("/process-instances")
	public String startNewProcessInstance(@RequestBody CreateProcessInstanceRepresentation startRequest) {
		actTaskService.startProcessInstance(startRequest);
		return UPDATED;
	}
	
	@GetMapping("/doing")
	public List<TaskDTO> getDoingTask(){
		String userId = AuthenticationUtils.getUserId();
		return actTaskService.getDoingTask(userId);
	}
	
	@GetMapping("/list-tasks")
	public List<TaskDTO> getTasks() {
		String userId = AuthenticationUtils.getUserId();
		return actTaskService.getTasks(userId);
	}

	@GetMapping("/{taskId}")
	public TaskDTO getOne(@PathVariable String taskId) {
		return actTaskService.getOneTask(taskId);
	}
	
	@GetMapping("/list-tasks/{processInstanceId}/{state}")
	public List<TaskDTO> listTasks(@PathVariable("processInstanceId") String processInstanceId,
			@PathVariable(name="state",required=false) String state){
		return actTaskService.listTasks(processInstanceId, state);
	}
	
	@PutMapping("/{taskId}/action/complete")
	public String completeTask(@PathVariable("taskId") String taskId) {
		actTaskService.completeTask(taskId);
		return UPDATED;
	}

	@PostMapping("/{taskId}/action/complete")
	public String completeTask(@PathVariable("taskId") String taskId,@RequestBody Map<String, Object> variables) {
		actTaskService.completeTask(taskId,variables);
		return UPDATED;
	}
	
	@PutMapping("/{taskId}/action/claim")
	public String claimTask(@PathVariable String taskId) {
		actTaskService.claimTask(taskId);
		return UPDATED;
    }
	
	@GetMapping("/task-form/{taskId}")
	public TaskFormDTO getFormData(@PathVariable String taskId) {
		return actTaskService.getTaskForm(taskId);
	}

}
