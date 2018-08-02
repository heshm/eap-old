package org.eap.common.param.manager;

import org.eap.common.param.domain.Dict;

public interface DictManager {
	
	Dict readOneDict(String dictType,String dictValue);

}
