package org.eap.common.param.dto;

import java.util.List;

import org.springframework.beans.BeanUtils;

import org.eap.common.param.domain.Area;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class AreaDTO extends Area{
	
	private String typeName;
	
	@JsonInclude(Include.NON_NULL)
	private List<AreaDTO> children;
	
	public AreaDTO(){
	}
	
	public AreaDTO(Area area){
		BeanUtils.copyProperties(area, this);
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public List<AreaDTO> getChildren() {
		return children;
	}

	public void setChildren(List<AreaDTO> children) {
		this.children = children;
	}
	
}
