package com.twm.mgmt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twm.mgmt.model.common.QueryResultVo;
import com.twm.mgmt.model.customer.CustomerCareConditionVo;
import com.twm.mgmt.model.customer.TransactionHistoryConditionVo;
import com.twm.mgmt.model.customer.TransactionHistoryResultVo;
import com.twm.mgmt.persistence.dto.TransactionRecordDto;
import com.twm.mgmt.persistence.entity.OpcoEntity;
import com.twm.mgmt.persistence.repository.OpcoRepository;
import com.twm.mgmt.persistence.repository.TransactionRecordRepository;
import com.twm.mgmt.utils.StringUtilsEx;

@Service
public class TransactionHistoryService extends BaseService {

	private static final String DEFAULT_HOME_OPCO = "TWM";

	private final TransactionRecordRepository transactionRecordRepository;
	private final OpcoRepository opcoRepository;

	@Autowired
	public TransactionHistoryService(TransactionRecordRepository transactionRecordRepository,
			OpcoRepository opcoRepository) {
		this.transactionRecordRepository = transactionRecordRepository;
		this.opcoRepository = opcoRepository;
	}

	@Transactional(readOnly = true)
	public QueryResultVo findTransactionHistory(TransactionHistoryConditionVo condition) {
		normalizePagination(condition);
		QueryResultVo result = new QueryResultVo(condition);

		List<TransactionRecordDto> dtos = transactionRecordRepository.findByCondition(condition);
		Integer total = transactionRecordRepository.countByCondition(condition);

		List<TransactionHistoryResultVo> vos = dtos.stream().map(TransactionHistoryResultVo::new)
				.collect(Collectors.toList());
		result.setTotal(total);
		result.setResult(vos);
		return result;
	}

	@Transactional(readOnly = true)
	public QueryResultVo findTransactionHistoryByAesTwmUid(CustomerCareConditionVo condition) {
		normalizePaginationForCare(condition);
		QueryResultVo result = new QueryResultVo(condition);

		// Convert CustomerCareConditionVo to TransactionHistoryConditionVo
		TransactionHistoryConditionVo txCondition = new TransactionHistoryConditionVo();
		txCondition.setAesTwmUid(condition.getAesTwmUid());
		txCondition.setTransactionId(condition.getTransactionId());
		txCondition.setOrderId(condition.getOrderId());
		txCondition.setRewardName(condition.getRewardName());
		txCondition.setTransactionType(condition.getTransactionType());
		txCondition.setPaymentMethod(condition.getPaymentMethod());
		txCondition.setCatalogOwner(condition.getCatalogOwner());
		// Time range filters removed
		txCondition.setNumber(condition.getNumber());
		txCondition.setSize(condition.getSize());
		txCondition.setName(condition.getName());
		txCondition.setOrder(condition.getOrder());

		List<TransactionRecordDto> dtos = transactionRecordRepository.findByCondition(txCondition);
		Integer total = transactionRecordRepository.countByCondition(txCondition);

		List<TransactionHistoryResultVo> vos = dtos.stream().map(TransactionHistoryResultVo::new)
				.collect(Collectors.toList());
		result.setTotal(total);
		result.setResult(vos);
		return result;
	}

	@Transactional(readOnly = true)
	public List<String> findTransactionTypes() {
		return transactionRecordRepository.findDistinctTransactionTypes(DEFAULT_HOME_OPCO);
	}

	@Transactional(readOnly = true)
	public List<String> findPaymentMethods() {
		return transactionRecordRepository.findDistinctPaymentMethods(DEFAULT_HOME_OPCO);
	}

	@Transactional(readOnly = true)
	public List<OpcoEntity> findCatalogOwners() {
		return opcoRepository.findAllByOrderByNameAsc();
	}

	public void copyQueryCondition(TransactionHistoryConditionVo source, TransactionHistoryConditionVo target) {
		if (source == null || target == null) {
			return;
		}
		target.setTransactionId(source.getTransactionId());
		target.setOrderId(source.getOrderId());
		target.setRewardName(source.getRewardName());
		target.setTransactionType(source.getTransactionType());
		target.setPaymentMethod(source.getPaymentMethod());
		target.setCatalogOwner(source.getCatalogOwner());
		target.setAesTwmUid(source.getAesTwmUid());
		// Time range filters removed
		target.setName(source.getName());
		target.setOrder(source.getOrder());
		target.setSize(source.getSize());
	}
	private void normalizePagination(TransactionHistoryConditionVo condition) {
		if (condition.getNumber() == null || condition.getNumber() <= 0) {
			condition.setNumber(1);
		}
		if (condition.getSize() == null || condition.getSize() <= 0) {
			condition.setSize(10);
		}
		if (StringUtilsEx.isBlank(condition.getOrder())) {
			condition.setOrder("desc");
		}
		if (StringUtilsEx.isBlank(condition.getName())) {
			condition.setName("hoTime");
		}
	}

	private void normalizePaginationForCare(CustomerCareConditionVo condition) {
		if (condition.getNumber() == null || condition.getNumber() <= 0) {
			condition.setNumber(1);
		}
		if (condition.getSize() == null || condition.getSize() <= 0) {
			condition.setSize(10);
		}
		if (StringUtilsEx.isBlank(condition.getOrder())) {
			condition.setOrder("desc");
		}
		if (StringUtilsEx.isBlank(condition.getName())) {
			condition.setName("hoTime");
		}
	}
}
