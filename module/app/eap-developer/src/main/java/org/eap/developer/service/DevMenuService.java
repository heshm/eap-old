package org.eap.developer.service;

import java.util.List;

import org.eap.common.param.dto.MenuDTO;


public interface DevMenuService {
	
	List<MenuDTO> readApplicationMenu(String appId);

}
