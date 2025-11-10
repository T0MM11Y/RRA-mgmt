package com.twm.mgmt.model.account;

import java.io.Serializable;

import com.twm.mgmt.persistence.entity.DepartmentEntity;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class DepartmentResultVo implements Serializable {

	private Long departmentId;

	private String departmentName;

	private String enabled;

	public DepartmentResultVo(DepartmentEntity entity) {
		this.departmentId = entity.getDepartmentId();
		this.departmentName = entity.getDepartmentName();
		this.enabled = entity.getEnabled();
	}

}
