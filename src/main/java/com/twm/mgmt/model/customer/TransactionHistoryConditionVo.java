package com.twm.mgmt.model.customer;

import com.twm.mgmt.enums.ActionType;
import com.twm.mgmt.model.common.PaginationVo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("serial")
public class TransactionHistoryConditionVo extends PaginationVo {

	private String transactionId;

	private String orderId;

	private String rewardName;

	private String transactionType;

	private String paymentMethod;

	private String catalogOwner;

	// From Step 1 (Customer Care search). Used to join with
	// transaction_record.identity_value
	private String aesTwmUid;

	// (Time range filters removed)

	private ActionType action = ActionType.UNKNOWN;
}
