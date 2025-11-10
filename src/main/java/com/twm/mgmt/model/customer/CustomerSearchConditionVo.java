package com.twm.mgmt.model.customer;

import java.io.Serializable;

import com.twm.mgmt.enums.ActionType;
import com.twm.mgmt.model.common.PaginationVo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("serial")
public class CustomerSearchConditionVo extends PaginationVo implements Serializable {

    private String msisdn;

    private String twmUid;

    private String subid;

    private ActionType action = ActionType.UNKNOWN;
}

