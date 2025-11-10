package com.twm.mgmt.model.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@SuppressWarnings("serial")
public class UserInfoVo implements Serializable {

	private Long accountId;

	private String userName;

	private Long roleId;

	private String roleName;

	private Long departmentId;

	private String departmentName;

	private String buTag;

	private String employeeId;

}
