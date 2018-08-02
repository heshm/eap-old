package org.eap.common.param.mapper;

import org.eap.common.param.domain.Menu;
import java.util.List;

public interface MenuMapper {
    int deleteByPrimaryKey(String menuId);

    int insert(Menu record);

    Menu selectByPrimaryKey(String menuId);

    List<Menu> selectAll();

    int updateByPrimaryKey(Menu record);
    
    Menu selectByMenuId(String menuId);
}