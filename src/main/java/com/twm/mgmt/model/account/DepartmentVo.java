package com.twm.mgmt.model.account;

import java.io.Serializable;

import com.twm.mgmt.enums.ActionType;
import com.twm.mgmt.persistence.entity.DepartmentEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@SuppressWarnings("serial")
public class DepartmentVo implements Serializable {

	private Long departmentId;

	private String departmentName;

	private ActionType action;

	public DepartmentVo(DepartmentEntity entity) {
		this.departmentId = entity.getDepartmentId();
		this.departmentName = entity.getDepartmentName();
	}

}
