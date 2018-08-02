package org.eap.developer.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.eap.common.param.dto.MenuDTO;
import org.eap.common.param.service.MenuService;
import org.eap.developer.service.DevMenuService;
import org.eap.framework.web.endpoint.BaseEndpoint;

@RestController
@RequestMapping(value = "/developer/menu")
public class DevMenuEndpoint extends BaseEndpoint{
	
	@Autowired
	private DevMenuService devMenuService;
	@Autowired
	private MenuService menuService;
	
	@GetMapping("/getChildMenuList/{id}")
	public List<MenuDTO> getChildMenuList(@PathVariable("id")String id){
		return devMenuService.readApplicationMenu(id);
	}
	
	@GetMapping("/delete/{id}")
	public String deleteMenu(@PathVariable("id")String id){
		menuService.deleteMenu(id);
		return DELETED;
	}
	
}
