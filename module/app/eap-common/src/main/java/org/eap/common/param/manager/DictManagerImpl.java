package org.eap.common.param.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.eap.common.param.domain.Dict;
import org.eap.common.param.mapper.DictMapper;

@Component
@Transactional(readOnly=true) 
public class DictManagerImpl implements DictManager {
	@Autowired
	private DictMapper dictMapper;

	@Override
	public Dict readOneDict(String dictType, String dictValue) {
		return dictMapper.selectByPrimaryKey(dictType, dictValue);
	}

}
