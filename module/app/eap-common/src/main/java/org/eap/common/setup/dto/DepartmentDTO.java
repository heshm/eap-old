package org.eap.common.setup.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;

import org.eap.common.setup.domain.Department;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class DepartmentDTO{
	
	private String id;

    private String parentId;

    private String name;

    private String[] areaCode;

    private String type;

    private String code;

    private String grade;

    private String primaryPerson;

    private String addr;

    private String telNo;

    private String faxNo;

    private String email;

    private String remark;
    
    private Boolean delFlg;
	
	private String typeName;
	
	private String primaryPersonName;
	
	private String areaName;
	
	@JsonProperty("children")
	private List<DepartmentDTO> children;
	
	public DepartmentDTO(){
		this.children = new ArrayList<>();
	}
	
	public DepartmentDTO(Department depart){
		BeanUtils.copyProperties(depart, this);
		areaCode = depart.getAreaCode().split(",");
		this.children = new ArrayList<>();
	}
	
	public DepartmentDTO(Department depart,String areaName){
		this(depart);
		this.areaName = areaName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String[] areaCode) {
		this.areaCode = areaCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getPrimaryPerson() {
		return primaryPerson;
	}

	public void setPrimaryPerson(String primaryPerson) {
		this.primaryPerson = primaryPerson;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(Boolean delFlg) {
		this.delFlg = delFlg;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getPrimaryPersonName() {
		return primaryPersonName;
	}

	public void setPrimaryPersonName(String primaryPersonName) {
		this.primaryPersonName = primaryPersonName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public List<DepartmentDTO> getChildren() {
		return children;
	}

	public void setChildren(List<DepartmentDTO> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DepartmentDTO [id=");
		builder.append(id);
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", areaCode=");
		builder.append(Arrays.toString(areaCode));
		builder.append(", type=");
		builder.append(type);
		builder.append(", code=");
		builder.append(code);
		builder.append(", grade=");
		builder.append(grade);
		builder.append(", primaryPerson=");
		builder.append(primaryPerson);
		builder.append(", addr=");
		builder.append(addr);
		builder.append(", telNo=");
		builder.append(telNo);
		builder.append(", faxNo=");
		builder.append(faxNo);
		builder.append(", email=");
		builder.append(email);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", typeName=");
		builder.append(typeName);
		builder.append(", primaryPersonName=");
		builder.append(primaryPersonName);
		builder.append(", areaName=");
		builder.append(areaName);
		builder.append(", children=");
		builder.append(children);
		builder.append("]");
		return builder.toString();
	}


}
