package org.eap.oa.act.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.eap.framework.web.controller.BaseController;
import org.eap.oa.act.dto.ResultListDataRepresentation;
import org.eap.oa.act.service.ModelService;

@RestController
@RequestMapping(value = "/oa/act/app")
public class ModelHistoryController extends BaseController{
	
	@Autowired
	private ModelService modelService;
	
	
	@GetMapping("/rest/models/{modelId}/history")
	public ResultListDataRepresentation getModelHistoryCollection(@PathVariable String modelId, @RequestParam(value = "includeLatestVersion", required = false) Boolean includeLatestVersion) {
	    return modelService.getModelHistory(modelId, includeLatestVersion);
	}

}
