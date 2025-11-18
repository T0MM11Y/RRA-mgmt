package com.twm.mgmt.persistence.dao;

import java.util.List;

import com.twm.mgmt.model.customer.TransactionHistoryConditionVo;
import com.twm.mgmt.persistence.dto.TransactionRecordDto;

public interface TransactionRecordDao {

	List<TransactionRecordDto> findByCondition(TransactionHistoryConditionVo condition);

	Integer countByCondition(TransactionHistoryConditionVo condition);

	List<String> findDistinctTransactionTypes(String homeOpco);

	List<String> findDistinctPaymentMethods(String homeOpco);

	/**
	 * Resolve AES TWM UID from transaction records by USER_ID. Returns the latest
	 * non-null identity_value ordered by transaction_time desc, or null if none.
	 */
	String findLatestAesTwmUidByUserId(String userId);

	/**
	 * Resolve USER_ID from transaction records by AES TWM UID (identity_value).
	 * Returns the latest non-null user_id ordered by transaction_time desc, or null
	 * if none.
	 */
	String findLatestUserIdByAesTwmUid(String aesTwmUid);
}
