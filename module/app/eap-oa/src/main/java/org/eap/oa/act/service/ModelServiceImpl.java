package org.eap.oa.act.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.editor.language.json.converter.util.JsonConverterUtil;
import org.flowable.form.model.FormModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import org.eap.framework.util.IDUtils;
import org.eap.framework.web.service.exception.InternalServerErrorException;
import org.eap.framework.web.util.AuthenticationUtils;
import org.eap.oa.act.domain.AbstractModel;
import org.eap.oa.act.domain.ActModelXref;
import org.eap.oa.act.domain.Model;
import org.eap.oa.act.domain.ModelHistory;
import org.eap.oa.act.domain.ModelRelation;
import org.eap.oa.act.dto.ModelRepresentation;
import org.eap.oa.act.dto.ResultListDataRepresentation;
import org.eap.oa.act.editor.service.ModelImageService;
import org.eap.oa.act.mapper.ActModelXrefMapper;
import org.eap.oa.act.mapper.ModelHistoryMapper;
import org.eap.oa.act.mapper.ModelMapper;
import org.eap.oa.act.mapper.ModelRelationMapper;
import org.eap.oa.flowable.editor.dto.AppDefinition;
import org.eap.oa.flowable.editor.dto.DecisionTableDefinitionRepresentation;
import org.eap.oa.util.OAConst;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
@Transactional(readOnly = true)
public class ModelServiceImpl implements ModelService {

	private final Logger logger = LoggerFactory.getLogger(ModelServiceImpl.class);

	private static final String SORT_NAME_ASC = "nameAsc";
	private static final String SORT_NAME_DESC = "nameDesc";
	private static final String SORT_MODIFIED_ASC = "modifiedAsc";

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ModelHistoryMapper modelHistoryMapper;
	@Autowired
	private ModelRelationMapper modelRelationMapper;
	@Autowired
	private ActModelXrefMapper actModelXrefMapper;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ModelImageService modelImageService;

	private BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();

	private BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();

	@Override
	public Model getModel(String modelId) {
		return modelMapper.selectByPrimaryKey(modelId);
	}

	@Override
	public ResultListDataRepresentation getModel(String filter, String sort, Integer modelType,
			HttpServletRequest request) {
		String filterText = request.getParameter("filterText");
		List<ModelRepresentation> resultList = new ArrayList<ModelRepresentation>();
		List<Model> models = modelMapper.selectModelList(AuthenticationUtils.getUserId(), modelType, filterText,
				getSort(sort, false));
		if (models != null) {
			models.forEach(model -> {
				resultList.add(new ModelRepresentation(model));		
			});
		}

		return new ResultListDataRepresentation(resultList);
	}

	@Override
	public ResultListDataRepresentation getModelHistory(String modelId, boolean includeLatestVersion) {
		Model model = getModel(modelId);
		List<ModelHistory> history = modelHistoryMapper.selectByModelId(modelId);
		ResultListDataRepresentation result = new ResultListDataRepresentation();

		List<ModelRepresentation> representations = new ArrayList<ModelRepresentation>();

		// Also include the latest version of the model
		if (includeLatestVersion) {
			representations.add(new ModelRepresentation(model));
		}
		if (history.size() > 0) {
			for (ModelHistory modelHistory : history) {
				representations.add(new ModelRepresentation(modelHistory));
			}
			result.setData(representations);
		}

		// Set size and total
		result.setSize(representations.size());
		result.setTotal(Long.valueOf(representations.size()));
		result.setStart(0);
		return result;
	}

	@Override
	public List<ModelRepresentation> getModel(String appId, String filter, Integer modelType) {
		List<Model> models;
		if (modelType == null) {
			models = modelMapper.selectModelList(null, AbstractModel.MODEL_TYPE_BPMN, filter, null);
		} else {
			models = modelMapper.selectModelList(null, modelType, filter, null);
		}
		List<ModelRepresentation> list = new ArrayList<>();
		Optional.ofNullable(models).get().forEach(model -> {
			ActModelXref xref = actModelXrefMapper.selectByModelId(model.getId());
			if(null == xref) {
				list.add(new ModelRepresentation(model));
			}else {
				list.add(new ModelRepresentation(model,xref.getAppId()));
			}
		});
		return list;
	}

	@Override
	public boolean validateModelKey(String modelKey) {
		return modelMapper.selectByModelKey(modelKey) != null;
	}

	@Override
	@Transactional
	public Model createModel(ModelRepresentation model, String editorJson) {
		Model newModel = new Model();
		newModel.setVersion(1);
		newModel.setName(model.getName());
		newModel.setModelKey(model.getKey());
		newModel.setModelType(model.getModelType());
		newModel.setCreated(Calendar.getInstance().getTime());
		newModel.setCreatedBy(AuthenticationUtils.getPrincipal().getUsername());
		newModel.setDescription(model.getDescription());
		newModel.setModelEditorJson(editorJson);
		newModel.setLastUpdated(Calendar.getInstance().getTime());
		newModel.setLastUpdatedBy(AuthenticationUtils.getPrincipal().getUsername());
		newModel.setId(IDUtils.uuid());
		persistModel(newModel);
		return newModel;
	}

	@Override
	@Transactional
	public Model saveModel(Model modelObject) {
		return persistModel(modelObject);
	}

	@Override
	@Transactional
	public Model saveModel(Model modelObject, String editorJson, byte[] imageBytes, boolean newVersion,
			String newVersionComment, UserDetails updatedBy) {
		return internalSave(modelObject.getName(), modelObject.getModelKey(), modelObject.getDescription(), editorJson,
				newVersion, newVersionComment, imageBytes, updatedBy, modelObject);
	}

	@Override
	@Transactional
	public Model saveModel(String modelId, String name, String key, String description, String editorJson,
			boolean newVersion, String newVersionComment, UserDetails updatedBy) {
		Model modelObject = modelMapper.selectByPrimaryKey(modelId);
		return internalSave(name, key, description, editorJson, newVersion, newVersionComment, null, updatedBy,
				modelObject);
	}

	private Model internalSave(String name, String key, String description, String editorJson, boolean newVersion,
			String newVersionComment, byte[] imageBytes, UserDetails updatedBy, Model modelObject) {
		if (!newVersion) {
			modelObject.setLastUpdated(new Date());
			modelObject.setLastUpdatedBy(updatedBy.getUsername());
			modelObject.setName(name);
			modelObject.setModelKey(key);
			modelObject.setDescription(description);
			modelObject.setModelEditorJson(editorJson);

			if (imageBytes != null) {
				modelObject.setThumbnail(imageBytes);
			}

		} else {
			ModelHistory historyModel = createNewModelhistory(modelObject);
			persistModelHistory(historyModel);

			modelObject.setVersion(modelObject.getVersion() + 1);
			modelObject.setLastUpdated(new Date());
			modelObject.setLastUpdatedBy(updatedBy.getUsername());
			modelObject.setName(name);
			modelObject.setModelKey(key);
			modelObject.setDescription(description);
			modelObject.setModelEditorJson(editorJson);
			modelObject.setModelComment(newVersionComment);
			if (imageBytes != null) {
				modelObject.setThumbnail(imageBytes);
			}
		}

		return persistModel(modelObject);
	}

	private Model persistModel(Model model) {
		if (modelMapper.selectByPrimaryKey(model.getId()) == null) {
			modelMapper.insert(model);
		}
		if (!StringUtils.isEmpty(model.getModelEditorJson())) {
			ObjectNode jsonNode = null;
			try {
				jsonNode = (ObjectNode) objectMapper.readTree(model.getModelEditorJson());
			} catch (IOException e) {
				logger.warn("Could not deserialize json model");
			}

			if ((model.getModelType() == null || model.getModelType().intValue() == Model.MODEL_TYPE_BPMN)) {
				byte[] thumbnail = modelImageService.generateThumbnailImage(model, jsonNode);
                if (thumbnail != null) {
                    model.setThumbnail(thumbnail);
                }
				handleBpmnProcessFormModelRelations(model, jsonNode);
			    handleBpmnProcessDecisionTaskModelRelations(model, jsonNode);

			} else if (model.getModelType().intValue() == Model.MODEL_TYPE_CMMN) {
				byte[] thumbnail = modelImageService.generateCmmnThumbnailImage(model, jsonNode);
                if (thumbnail != null) {
                    model.setThumbnail(thumbnail);
                }
                // The cmmn need to be updated.
                
			} else if (model.getModelType().intValue() == Model.MODEL_TYPE_FORM
					|| model.getModelType().intValue() == Model.MODEL_TYPE_DECISION_TABLE) {
				jsonNode.put("name", model.getName());
				jsonNode.put("key", model.getModelKey());
			}
		}
		modelMapper.updateByPrimaryKey(model);
		return model;
	}

	private ModelHistory persistModelHistory(ModelHistory modelHistory) {
		String id = IDUtils.uuid();
		modelHistory.setId(id);
		modelHistoryMapper.insert(modelHistory);
		return modelHistory;
	}

	@Override
	public BpmnModel getBpmnModel(AbstractModel model) {
		BpmnModel bpmnModel = null;
		try {
			/*
			 * Map<String, Model> formMap = new HashMap<String, Model>(); Map<String, Model>
			 * decisionTableMap = new HashMap<String, Model>();
			 * 
			 * List<Model> referencedModels =
			 * modelRepository.findModelsByParentModelId(model.getId()); for (Model
			 * childModel : referencedModels) { if (Model.MODEL_TYPE_FORM ==
			 * childModel.getModelType()) { formMap.put(childModel.getId(), childModel);
			 * 
			 * } else if (Model.MODEL_TYPE_DECISION_TABLE == childModel.getModelType()) {
			 * decisionTableMap.put(childModel.getId(), childModel); } }
			 */

			bpmnModel = getBpmnModel(model, null, null);

		} catch (Exception e) {
			logger.error("Could not generate BPMN 2.0 model for " + model.getId(), e);
			throw new InternalServerErrorException("Could not generate BPMN 2.0 model");
		}

		return bpmnModel;
	}

	@Override
	public BpmnModel getBpmnModel(AbstractModel model, Map<String, Model> formMap,
			Map<String, Model> decisionTableMap) {
		try {
			ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(model.getModelEditorJson());
			Map<String, String> formKeyMap = new HashMap<String, String>();
			if (formMap != null) {
				for (Model formModel : formMap.values()) {
					formKeyMap.put(formModel.getId(), formModel.getModelKey());
				}
			}
			Map<String, String> decisionTableKeyMap = new HashMap<String, String>();
			if (decisionTableMap != null) {
				for (Model decisionTableModel : decisionTableMap.values()) {
					decisionTableKeyMap.put(decisionTableModel.getId(), decisionTableModel.getModelKey());
				}
			}
			return bpmnJsonConverter.convertToBpmnModel(editorJsonNode, formKeyMap, decisionTableKeyMap);
		} catch (Exception e) {
			logger.error("Could not generate BPMN 2.0 model for " + model.getId(), e);
			throw new InternalServerErrorException("Could not generate BPMN 2.0 model");
		}
	}

	@Override
	public byte[] getBpmnXML(BpmnModel bpmnModel) {
		for (org.flowable.bpmn.model.Process process : bpmnModel.getProcesses()) {
			if (!StringUtils.isEmpty(process.getId())) {
				char firstCharacter = process.getId().charAt(0);
				if (Character.isDigit(firstCharacter)) {
					process.setId("a" + process.getId());
				}
			}
		}
		byte[] xmlBytes = bpmnXMLConverter.convertToXML(bpmnModel);
		return xmlBytes;
	}
	
	@Override
	@Transactional
	public void updateCategory(String modelId, String appId) {
		ActModelXref actModelXref = actModelXrefMapper.selectByModelId(modelId);
		if(null != actModelXref) {
			if(actModelXref.getAppId().equals(appId))
				return;
			actModelXref.setAppId(appId);
			actModelXrefMapper.updateByPrimaryKey(actModelXref);
		}else {
			actModelXrefMapper.insert(new ActModelXref(appId,modelId));
		}
	}


	private void handleBpmnProcessFormModelRelations(AbstractModel bpmnProcessModel, ObjectNode editorJsonNode) {
		List<JsonNode> formReferenceNodes = JsonConverterUtil
				.filterOutJsonNodes(JsonConverterUtil.getBpmnProcessModelFormReferences(editorJsonNode));
		Set<String> formIds = JsonConverterUtil.gatherStringPropertyFromJsonNodes(formReferenceNodes, "id");

		handleModelRelations(bpmnProcessModel, formIds, OAConst.TYPE_FORM_MODEL_CHILD);
	}

	private void handleBpmnProcessDecisionTaskModelRelations(AbstractModel bpmnProcessModel,
			ObjectNode editorJsonNode) {
		List<JsonNode> decisionTableNodes = JsonConverterUtil
				.filterOutJsonNodes(JsonConverterUtil.getBpmnProcessModelDecisionTableReferences(editorJsonNode));
		Set<String> decisionTableIds = JsonConverterUtil.gatherStringPropertyFromJsonNodes(decisionTableNodes, "id");

		handleModelRelations(bpmnProcessModel, decisionTableIds, OAConst.TYPE_DECISION_TABLE_MODEL_CHILD);
	}

	private void handleModelRelations(AbstractModel bpmnProcessModel, Set<String> idsReferencedInJson,
			String relationshipType) {
		modelRelationMapper.deleteByParentModelIdAndType(bpmnProcessModel.getId(), relationshipType);
		idsReferencedInJson.forEach(id -> {
			ModelRelation relation = new ModelRelation();
			relation.setId(IDUtils.uuid());
			relation.setParentModelId(bpmnProcessModel.getId());
			relation.setModelId(id);
			relation.setRelationType(relationshipType);
			modelRelationMapper.insert(relation);
		});
	}

	private ModelHistory createNewModelhistory(Model model) {
		ModelHistory historyModel = new ModelHistory();
		historyModel.setName(model.getName());
		historyModel.setModelKey(model.getModelKey());
		historyModel.setDescription(model.getDescription());
		historyModel.setCreated(model.getCreated());
		historyModel.setLastUpdated(model.getLastUpdated());
		historyModel.setCreatedBy(model.getCreatedBy());
		historyModel.setLastUpdatedBy(model.getLastUpdatedBy());
		historyModel.setModelEditorJson(model.getModelEditorJson());
		historyModel.setModelType(model.getModelType());
		historyModel.setVersion(model.getVersion());
		historyModel.setModelId(model.getId());
		historyModel.setModelComment(model.getModelComment());

		return historyModel;
	}

	private Sort getSort(String sort, boolean prefixWithProcessModel) {
		String propName;
		Direction direction;
		if (SORT_NAME_ASC.equals(sort)) {
			if (prefixWithProcessModel) {
				propName = "model.name";
			} else {
				propName = "name";
			}
			direction = Direction.ASC;
		} else if (SORT_NAME_DESC.equals(sort)) {
			if (prefixWithProcessModel) {
				propName = "model.name";
			} else {
				propName = "name";
			}
			direction = Direction.DESC;
		} else if (SORT_MODIFIED_ASC.equals(sort)) {
			if (prefixWithProcessModel) {
				propName = "model.lastUpdated";
			} else {
				propName = "lastUpdated";
			}
			direction = Direction.ASC;
		} else {
			// Default sorting
			if (prefixWithProcessModel) {
				propName = "model.lastUpdated";
			} else {
				propName = "lastUpdated";
			}
			direction = Direction.DESC;
		}
		return new Sort(direction, propName);
	}

	@Override
	public String createModelJson(ModelRepresentation model) {
		String json = null;
        if (Integer.valueOf(AbstractModel.MODEL_TYPE_FORM).equals(model.getModelType())) {
            try {
                json = objectMapper.writeValueAsString(new FormModel());
            } catch (Exception e) {
                logger.error("Error creating form model", e);
                throw new InternalServerErrorException("Error creating form");
            }

        } else if (Integer.valueOf(AbstractModel.MODEL_TYPE_DECISION_TABLE).equals(model.getModelType())) {
            try {
                DecisionTableDefinitionRepresentation decisionTableDefinition = new DecisionTableDefinitionRepresentation();

                String decisionTableDefinitionKey = model.getName().replaceAll(" ", "");
                decisionTableDefinition.setKey(decisionTableDefinitionKey);

                json = objectMapper.writeValueAsString(decisionTableDefinition);
            } catch (Exception e) {
                logger.error("Error creating decision table model", e);
                throw new InternalServerErrorException("Error creating decision table");
            }

        } else if (Integer.valueOf(AbstractModel.MODEL_TYPE_APP).equals(model.getModelType())) {
            try {
                json = objectMapper.writeValueAsString(new AppDefinition());
            } catch (Exception e) {
            	logger.error("Error creating app definition", e);
                throw new InternalServerErrorException("Error creating app definition");
            }

        } else if (Integer.valueOf(AbstractModel.MODEL_TYPE_CMMN).equals(model.getModelType())) {
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/cmmn1.1#");
            editorNode.set("stencilset", stencilSetNode);
            ObjectNode propertiesNode = objectMapper.createObjectNode();
            propertiesNode.put("case_id", model.getKey());
            propertiesNode.put("name", model.getName());
            if (!StringUtils.isEmpty(model.getDescription())) {
                propertiesNode.put("documentation", model.getDescription());
            }
            editorNode.set("properties", propertiesNode);

            ArrayNode childShapeArray = objectMapper.createArrayNode();
            editorNode.set("childShapes", childShapeArray);
            ObjectNode childNode = objectMapper.createObjectNode();
            childShapeArray.add(childNode);
            ObjectNode boundsNode = objectMapper.createObjectNode();
            childNode.set("bounds", boundsNode);
            ObjectNode lowerRightNode = objectMapper.createObjectNode();
            boundsNode.set("lowerRight", lowerRightNode);
            lowerRightNode.put("x", 758);
            lowerRightNode.put("y", 754);
            ObjectNode upperLeftNode = objectMapper.createObjectNode();
            boundsNode.set("upperLeft", upperLeftNode);
            upperLeftNode.put("x", 40);
            upperLeftNode.put("y", 40);
            childNode.set("childShapes", objectMapper.createArrayNode());
            childNode.set("dockers", objectMapper.createArrayNode());
            childNode.set("outgoing", objectMapper.createArrayNode());
            childNode.put("resourceId", "casePlanModel");
            ObjectNode stencilNode = objectMapper.createObjectNode();
            childNode.set("stencil", stencilNode);
            stencilNode.put("id", "CasePlanModel");
            json = editorNode.toString();

        } else {
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.set("stencilset", stencilSetNode);
            ObjectNode propertiesNode = objectMapper.createObjectNode();
            propertiesNode.put("process_id", model.getKey());
            propertiesNode.put("name", model.getName());
            if (!StringUtils.isEmpty(model.getDescription())) {
                propertiesNode.put("documentation", model.getDescription());
            }
            editorNode.set("properties", propertiesNode);

            ArrayNode childShapeArray = objectMapper.createArrayNode();
            editorNode.set("childShapes", childShapeArray);
            ObjectNode childNode = objectMapper.createObjectNode();
            childShapeArray.add(childNode);
            ObjectNode boundsNode = objectMapper.createObjectNode();
            childNode.set("bounds", boundsNode);
            ObjectNode lowerRightNode = objectMapper.createObjectNode();
            boundsNode.set("lowerRight", lowerRightNode);
            lowerRightNode.put("x", 130);
            lowerRightNode.put("y", 193);
            ObjectNode upperLeftNode = objectMapper.createObjectNode();
            boundsNode.set("upperLeft", upperLeftNode);
            upperLeftNode.put("x", 100);
            upperLeftNode.put("y", 163);
            childNode.set("childShapes", objectMapper.createArrayNode());
            childNode.set("dockers", objectMapper.createArrayNode());
            childNode.set("outgoing", objectMapper.createArrayNode());
            childNode.put("resourceId", "startEvent1");
            ObjectNode stencilNode = objectMapper.createObjectNode();
            childNode.set("stencil", stencilNode);
            stencilNode.put("id", "StartNoneEvent");
            json = editorNode.toString();
        }

        return json;
	}

	
}
