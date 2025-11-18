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
public class CustomerCareConditionVo extends PaginationVo {

    // User identification fields (one of these should be provided)
    private String msisdn;
    private String twmUid;
    private String subid;
    private String aesTwmUid;
    // Short-term: support searching by transaction_record.USER_ID
    private String userId;

    // Optional hint from UI about which identifier was used (e.g. msisdn, twmUid, subid, aesTwmUid, userId)
    private String identifierType;

    // Transaction filter fields
    private String transactionId;
    private String orderId;
    private String rewardName;
    private String transactionType;
    private String paymentMethod;
    private String catalogOwner;

    // (Time range filters removed)

    private ActionType action = ActionType.UNKNOWN;
}
