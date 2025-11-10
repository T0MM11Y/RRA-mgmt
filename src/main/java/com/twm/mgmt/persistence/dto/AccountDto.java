package com.twm.mgmt.persistence.dto;

import lombok.Data;

@Data
public class AccountDto {

	private Long accountId;

	private String userName;

	private String email;

	private String unitNo;

	private Long roleId;

	private String roleName;

	private Long departmentId;

	private String departmentName;

	private String enabled;

}
