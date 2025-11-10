package com.twm.mgmt.persistence.dao;

import java.util.List;

import com.twm.mgmt.model.customer.TransactionHistoryConditionVo;
import com.twm.mgmt.persistence.dto.TransactionRecordDto;

public interface TransactionRecordDao {

	List<TransactionRecordDto> findByCondition(TransactionHistoryConditionVo condition);

	Integer countByCondition(TransactionHistoryConditionVo condition);

	List<TransactionRecordDto> findForExport(TransactionHistoryConditionVo condition);

	List<String> findDistinctTransactionTypes(String homeOpco);

	List<String> findDistinctPaymentMethods(String homeOpco);
}
