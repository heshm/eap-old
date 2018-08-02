package org.eap.common.param.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.eap.common.param.domain.Dict;
import org.eap.common.param.domain.DictType;

public interface DictService {
	
	List<Dict> listAllDict();
	
	Page<Dict> listPageDict(String dictType,Pageable pageable);
	
	List<DictType> listAllDictType();
	
	void createDictType(DictType dictType);
	
	void updateDictType(DictType dictType);

}
