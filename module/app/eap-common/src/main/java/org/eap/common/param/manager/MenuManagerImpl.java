package org.eap.common.param.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.eap.common.param.domain.Menu;
import org.eap.common.param.mapper.MenuMapper;

@Component
@Transactional(readOnly=true) 
public class MenuManagerImpl implements MenuManager {
	
	@Autowired
	private MenuMapper menuMapper;

	@Override
	public Menu getOneMenuById(String menuId) {
		return menuMapper.selectByMenuId(menuId);
	}

}
