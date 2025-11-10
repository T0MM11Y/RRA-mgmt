package com.twm.mgmt.persistence.dao;

import java.util.List;

import com.twm.mgmt.model.account.AccountConditionVo;
import com.twm.mgmt.persistence.dto.AccountDto;

public interface AccountDao {

	List<AccountDto> findByCondition(AccountConditionVo condition);

	Integer countByCondition(AccountConditionVo condition);

}
