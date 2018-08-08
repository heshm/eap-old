package org.eap.oa.act.service;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.SubProcess;
import org.flowable.bpmn.model.UserTask;
import org.flowable.dmn.model.DmnDefinition;
import org.flowable.dmn.xml.converter.DmnXMLConverter;
import org.flowable.editor.dmn.converter.DmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.eap.framework.web.service.exception.InternalServerErrorException;
import org.eap.oa.act.domain.ActModelXref;
import org.eap.oa.act.domain.Model;
import org.eap.oa.act.domain.ModelRelation;
import org.eap.oa.act.mapper.ActModelXrefMapper;
import org.eap.oa.act.mapper.ModelRelationMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(readOnly = true)
public class DeploymentServiceImpl implements DeploymentService {

	@Resource
	private ModelService modelService;
	@Autowired
	private ModelRelationMapper modelRelationMapper;
	@Autowired
	private ActModelXrefMapper actModelXrefMapper;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ObjectMapper objectMapper;
	
	private DmnJsonConverter dmnJsonConverter = new DmnJsonConverter();
	private DmnXMLConverter dmnXMLConverter = new DmnXMLConverter();

	@Override
	@Transactional
	public Deployment deployModel(String modelId) {
		Model model = modelService.getModel(modelId);
		Deployment deployment = null;
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().name(model.getName())
				.key(model.getModelKey());

		Map<String, Model> formMap = new HashMap<String, Model>();
		Map<String, Model> decisionTableMap = new HashMap<String, Model>();

		List<ModelRelation> relations = modelRelationMapper.selectByParentModelIdAndType(modelId, null);
		relations.forEach(relation -> {
			Model childModel = modelService.getModel(relation.getModelId());
			if (Model.MODEL_TYPE_FORM == childModel.getModelType()) {
				formMap.put(childModel.getId(), childModel);
			} else if (Model.MODEL_TYPE_DECISION_TABLE == childModel.getModelType()) {
				decisionTableMap.put(childModel.getId(), childModel);
			}
		});

		BpmnModel bpmnModel = modelService.getBpmnModel(model,formMap,decisionTableMap);
		Map<String, StartEvent> startEventMap = processNoneStartEvents(bpmnModel);
		for (Process process : bpmnModel.getProcesses()) {
			processUserTasks(process.getFlowElements(), process, startEventMap);
		}
		byte[] modelXML = modelService.getBpmnXML(bpmnModel);
		deploymentBuilder.addInputStream(model.getModelKey().replaceAll(" ", "") + ".bpmn",
				new ByteArrayInputStream(modelXML));

		if (formMap.size() > 0) {
			formMap.forEach((key, form) -> {
				deploymentBuilder.addString("form-" + form.getModelKey() + ".form", form.getModelEditorJson());
			});
		}

		if (decisionTableMap.size() > 0) {
			
			decisionTableMap.forEach((decisionTableId,decisionTableInfo) -> {
				try {
					JsonNode decisionTableNode = objectMapper.readTree(decisionTableInfo.getModelEditorJson());
					DmnDefinition dmnDefinition = dmnJsonConverter.convertToDmn(decisionTableNode,
							decisionTableInfo.getId(), decisionTableInfo.getVersion(),
							decisionTableInfo.getLastUpdated());
					byte[] dmnXMLBytes = dmnXMLConverter.convertToXML(dmnDefinition);
					deploymentBuilder.addBytes("dmn-" + decisionTableInfo.getModelKey() + ".dmn", dmnXMLBytes);

				} catch (Exception e) {
					throw new InternalServerErrorException(
							"Error converting decision table to XML " + decisionTableInfo.getName());
				}
			});
		}

		deployment = deploymentBuilder.deploy();
		
		//设置流程分类
		ActModelXref actModelXref = actModelXrefMapper.selectByModelId(model.getId());
		if(null != actModelXref) {
			repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list().forEach(processDefinition -> {
				repositoryService.setProcessDefinitionCategory(processDefinition.getId(), actModelXref.getAppId());
			});
		}
		return deployment;
	}

	private Map<String, StartEvent> processNoneStartEvents(BpmnModel bpmnModel) {
		Map<String, StartEvent> startEventMap = new HashMap<String, StartEvent>();
		for (Process process : bpmnModel.getProcesses()) {
			for (FlowElement flowElement : process.getFlowElements()) {
				if (flowElement instanceof StartEvent) {
					StartEvent startEvent = (StartEvent) flowElement;
					if (CollectionUtils.isEmpty(startEvent.getEventDefinitions())) {
						if (StringUtils.isEmpty(startEvent.getInitiator())) {
							startEvent.setInitiator("initiator");
						}
						startEventMap.put(process.getId(), startEvent);
						break;
					}
				}
			}
		}
		return startEventMap;
	}

	private void processUserTasks(Collection<FlowElement> flowElements, Process process,
			Map<String, StartEvent> startEventMap) {

		for (FlowElement flowElement : flowElements) {
			if (flowElement instanceof UserTask) {
				UserTask userTask = (UserTask) flowElement;
				if ("$INITIATOR".equals(userTask.getAssignee())) {
					if (startEventMap.get(process.getId()) != null) {
						userTask.setAssignee("${" + startEventMap.get(process.getId()).getInitiator() + "}");
					}
				}

			} else if (flowElement instanceof SubProcess) {
				processUserTasks(((SubProcess) flowElement).getFlowElements(), process, startEventMap);
			}
		}
	}

}
