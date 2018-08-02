package org.eap.common.param.service;


import java.util.List;

import org.springframework.security.core.userdetails.User;

import org.eap.common.param.domain.Menu;
import org.eap.common.param.dto.MenuDTO;

public interface MenuService {
	
	/**
	 * 根据MenuId递归获取Menu树
	 * @param menuId
	 */
	Menu readAllMenuById(String menuId);
	
	/**
	 * 根据MenuId读取menu及子菜单
	 * @param menuId
	 */
	Menu readOneMenuById(String menuId);
	/**
	 * 根据MenuId读取父菜单
	 * @param menuId
	 */
	Menu readParentMenu(String menuId);
	List<String> readParentMenu(String[] menuIdArray);
	/**
	 * 根据MenuId读取menu
	 * @param menuId
	 */
	Menu readByPrimaryKey(String menuId);
	
	/**
	 * 根据MenuId读取menuList
	 * @param menuId
	 */
	List<Menu> readMenuList(String menuId);
	
	void savaMenu(Menu menu);
	/**
	 * 根据MenuId递归删除菜单
	 * @param menuId
	 */
	void deleteMenu(String menuId);
	/**
	 * 读取授权app
	 */
	List<Menu> readAuthApp(User user);
	/**
	 * 根据id读取菜单及父菜单
	 */
	MenuDTO readOneMenuWithParent(String id);

}
