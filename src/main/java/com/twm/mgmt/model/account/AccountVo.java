package com.twm.mgmt.model.account;

import java.io.Serializable;

import com.twm.mgmt.enums.ActionType;
import com.twm.mgmt.persistence.entity.AccountEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@SuppressWarnings("serial")
public class AccountVo implements Serializable {

	private Long accountId;

	private String userName;

	private String email;

	private String mobile;

	private Long roleId;

	private Long departmentId;

	private ActionType action = ActionType.ADD;

	private String approvable;
	
	private String requestId;

	public AccountVo(AccountEntity entity) {
		this.accountId = entity.getAccountId();
		this.userName = entity.getUserName();
		this.email = entity.getEmail();
		this.mobile = entity.getMobile();
		this.roleId = entity.getRoleId();
		this.departmentId = entity.getDepartmentId();
	}

}
