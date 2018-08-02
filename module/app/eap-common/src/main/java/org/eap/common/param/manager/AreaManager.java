package org.eap.common.param.manager;


import org.eap.common.param.domain.Area;

public interface AreaManager {
	
	Area getOneArea(String id);
	
	String getFullAreaName(String[] codes);

}
