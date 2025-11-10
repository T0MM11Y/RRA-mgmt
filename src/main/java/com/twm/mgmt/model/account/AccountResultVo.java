package com.twm.mgmt.model.account;

import java.io.Serializable;

import com.twm.mgmt.persistence.dto.AccountDto;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class AccountResultVo implements Serializable {

	private Long accountId;

	private String userName;

	private String email;

	private String unitNo;

	private String roleName;

	private String departmentName;

	private String enabled;

	public AccountResultVo(AccountDto dto) {
		this.accountId = dto.getAccountId();
		this.userName = dto.getUserName();
		this.email = dto.getEmail();
		this.unitNo = dto.getUnitNo();
		this.roleName = dto.getRoleName().replaceFirst("ROLE_", "");
		this.departmentName = dto.getDepartmentName();
		this.enabled = dto.getEnabled();
	}

}
