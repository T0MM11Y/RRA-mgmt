package com.twm.mgmt.model.account;

import com.twm.mgmt.enums.ActionType;
import com.twm.mgmt.model.common.PaginationVo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("serial")
public class AccountConditionVo extends PaginationVo {

	private String roleId;

	private String departmentId;

	private ActionType action = ActionType.UNKNOWN;

}
