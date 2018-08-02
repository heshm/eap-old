package org.eap.common.param.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.eap.common.param.dto.AreaDTO;
import org.eap.common.param.dto.AreaSelectDTO;
import org.eap.common.param.service.AreaService;
import org.eap.framework.web.endpoint.BaseEndpoint;

@RestController
@RequestMapping("/common/param/area")
public class AreaEndpoint extends BaseEndpoint {
	@Autowired
	private AreaService areaService;
	
	@GetMapping("/tree/{id}")
	public AreaDTO tree(@PathVariable("id") String id){
		return areaService.getOneNestedArea(id);
	}
	
	@GetMapping("/treeSelect/{id}")
	public List<AreaSelectDTO> treeSelect(@PathVariable("id") String id) {
		return areaService.getSelectedArea(id);
	}
	
	@GetMapping("/ajaxSelect/{id}")
	public List<AreaSelectDTO> ajaxSelect(@PathVariable("id") String id) {
		return areaService.getSelectedAreaWithoutChildren(id);
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") String id){
		areaService.delete(id);
		return DELETED;
	}
	
	@PostMapping("/create")
	public String create(@RequestBody AreaDTO area){
		areaService.create(area);
		return CREATED;
	}
	
	@PutMapping("/update")
	public String update(@RequestBody AreaDTO area){
		areaService.update(area);
		return UPDATED;
	}

}
