package org.eap.oa.act.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.eap.common.authority.service.RoleService;
import org.eap.framework.domain.AdminRole;
import org.eap.framework.web.controller.BaseController;
import org.eap.oa.act.dto.ResultListDataRepresentation;
import org.eap.oa.flowable.editor.dto.GroupRepresentation;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/oa/act/app")
public class EditorGroupsController extends BaseController {

	@Autowired
	private RoleService roleService; 
	
	@GetMapping("/rest/editor-groups")
	public ResultListDataRepresentation getGroups(@RequestParam(required = false, value = "filter") String filter) {
		List<AdminRole> roles = roleService.getRoleByName(filter);
		List<GroupRepresentation> result = null;
		if(roles != null) {
			result = new ArrayList<>(roles.size());
			for(AdminRole role : roles) {
				result.add(new GroupRepresentation(role));
			}
		}
		return new ResultListDataRepresentation(result);
	}

}
