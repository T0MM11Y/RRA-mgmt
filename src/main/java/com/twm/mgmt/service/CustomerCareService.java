package com.twm.mgmt.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twm.mgmt.model.common.QueryResultVo;
import com.twm.mgmt.model.customer.CustomerCareConditionVo;
import com.twm.mgmt.model.customer.CustomerSearchConditionVo;
import com.twm.mgmt.model.customer.CustomerSearchResultVo;
import com.twm.mgmt.persistence.entity.UserProfileEntity;
import com.twm.mgmt.persistence.repository.UserProfileRepository;
import com.twm.mgmt.persistence.repository.TransactionRecordRepository;
import com.twm.mgmt.utils.StringUtilsEx;

@Service
public class CustomerCareService extends BaseService {

    private final UserProfileRepository userProfileRepository;
    private final TransactionHistoryService transactionHistoryService;
    private final TransactionRecordRepository transactionRecordRepository;

    @Autowired
    public CustomerCareService(UserProfileRepository userProfileRepository,
            TransactionHistoryService transactionHistoryService,
            TransactionRecordRepository transactionRecordRepository) {
        this.userProfileRepository = userProfileRepository;
        this.transactionHistoryService = transactionHistoryService;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    @Transactional(readOnly = true)
    public QueryResultVo findUsers(CustomerSearchConditionVo condition) {
        normalizePagination(condition);
        QueryResultVo result = new QueryResultVo(condition);

        List<UserProfileEntity> entities;
        if (StringUtilsEx.isNotBlank(condition.getTwmUid())) {
            entities = userProfileRepository.findByTwmUidIgnoreCaseOrderByUpdateDateDesc(condition.getTwmUid());
        } else if (StringUtilsEx.isNotBlank(condition.getMsisdn())) {
            entities = userProfileRepository.findByMsisdnOrderByUpdateDateDesc(condition.getMsisdn());
        } else if (StringUtilsEx.isNotBlank(condition.getSubid())) {
            entities = userProfileRepository.findBySubidOrderByUpdateDateDesc(condition.getSubid());
        } else {
            entities = Collections.emptyList();
        }

        List<CustomerSearchResultVo> vos = entities.stream()
                .map(CustomerSearchResultVo::new)
                .collect(Collectors.toList());

        result.setTotal(vos.size());
        result.setResult(vos);
        return result;
    }

    private void normalizePagination(CustomerSearchConditionVo condition) {
        if (condition.getNumber() == null || condition.getNumber() <= 0) {
            condition.setNumber(1);
        }
        if (condition.getSize() == null || condition.getSize() <= 0) {
            condition.setSize(10);
        }
        if (com.twm.mgmt.utils.StringUtilsEx.isBlank(condition.getOrder())) {
            condition.setOrder("desc");
        }
        if (com.twm.mgmt.utils.StringUtilsEx.isBlank(condition.getName())) {
            condition.setName("updateDate");
        }
    }

    @Transactional(readOnly = true)
    public QueryResultVo findTransactionsByIdentifier(CustomerCareConditionVo condition) {
        normalizePaginationForCare(condition);

        // First, resolve the aesTwmUid from the provided identifier if not already set
        if (StringUtilsEx.isBlank(condition.getAesTwmUid())) {
            String aesTwmUid = resolveAesTwmUid(condition);
            if (StringUtilsEx.isBlank(aesTwmUid)) {
                // No user found, return empty result
                QueryResultVo result = new QueryResultVo(condition);
                result.setTotal(0);
                result.setResult(Collections.emptyList());
                return result;
            }
            condition.setAesTwmUid(aesTwmUid);
        }

        // Delegate to TransactionHistoryService using the resolved aesTwmUid
        return transactionHistoryService.findTransactionHistoryByAesTwmUid(condition);
    }

    @Transactional(readOnly = true)
    public java.util.Map<String, Object> getUserIdentifiers(CustomerCareConditionVo condition) {
        List<UserProfileEntity> profiles;
        if (StringUtilsEx.isNotBlank(condition.getAesTwmUid())) {
            profiles = userProfileRepository.findByAesTwmUidOrderByUpdateDateDesc(condition.getAesTwmUid());
        } else if (StringUtilsEx.isNotBlank(condition.getTwmUid())) {
            profiles = userProfileRepository.findByTwmUidIgnoreCaseOrderByUpdateDateDesc(condition.getTwmUid());
        } else if (StringUtilsEx.isNotBlank(condition.getMsisdn())) {
            profiles = userProfileRepository.findByMsisdnOrderByUpdateDateDesc(condition.getMsisdn());
        } else if (StringUtilsEx.isNotBlank(condition.getSubid())) {
            profiles = userProfileRepository.findBySubidOrderByUpdateDateDesc(condition.getSubid());
        } else {
            profiles = Collections.emptyList();
        }

        java.util.Map<String, Object> meta = new java.util.LinkedHashMap<>();

        if (!profiles.isEmpty()) {
            UserProfileEntity p = profiles.get(0);
            if (StringUtilsEx.isNotBlank(p.getMsisdn())) meta.put("msisdn", p.getMsisdn());
            if (StringUtilsEx.isNotBlank(p.getTwmUid())) meta.put("twmUid", p.getTwmUid());
            if (StringUtilsEx.isNotBlank(p.getSubid())) meta.put("subid", p.getSubid());
            if (StringUtilsEx.isNotBlank(p.getAesTwmUid())) meta.put("aesTwmUid", p.getAesTwmUid());
        } else {
            // Fallback to echo back whatever identifier was provided, to avoid clearing UI
            if (StringUtilsEx.isNotBlank(condition.getMsisdn())) meta.put("msisdn", condition.getMsisdn());
            if (StringUtilsEx.isNotBlank(condition.getTwmUid())) meta.put("twmUid", condition.getTwmUid());
            if (StringUtilsEx.isNotBlank(condition.getSubid())) meta.put("subid", condition.getSubid());
            if (StringUtilsEx.isNotBlank(condition.getAesTwmUid())) meta.put("aesTwmUid", condition.getAesTwmUid());
        }

        // Populate userId: prefer request value; else resolve from transaction_record by AES TWM UID
        if (StringUtilsEx.isNotBlank(condition.getUserId())) {
            meta.put("userId", condition.getUserId());
        } else if (StringUtilsEx.isNotBlank(condition.getAesTwmUid())) {
            String userId = transactionRecordRepository.findLatestUserIdByAesTwmUid(condition.getAesTwmUid());
            if (StringUtilsEx.isNotBlank(userId)) {
                meta.put("userId", userId);
            }
        }

        return meta.isEmpty() ? null : meta;
    }

    private String resolveAesTwmUid(CustomerCareConditionVo condition) {
        List<UserProfileEntity> entities;
        if (StringUtilsEx.isNotBlank(condition.getTwmUid())) {
            entities = userProfileRepository.findByTwmUidIgnoreCaseOrderByUpdateDateDesc(condition.getTwmUid());
        } else if (StringUtilsEx.isNotBlank(condition.getMsisdn())) {
            entities = userProfileRepository.findByMsisdnOrderByUpdateDateDesc(condition.getMsisdn());
        } else if (StringUtilsEx.isNotBlank(condition.getSubid())) {
            entities = userProfileRepository.findBySubidOrderByUpdateDateDesc(condition.getSubid());
        } else if (StringUtilsEx.isNotBlank(condition.getUserId())) {
            // Short-term approach: resolve AES TWM UID via transaction_record by USER_ID
            String aes = transactionRecordRepository.findLatestAesTwmUidByUserId(condition.getUserId());
            return StringUtilsEx.isNotBlank(aes) ? aes : null;
        } else {
            entities = Collections.emptyList();
        }

        if (entities.isEmpty()) {
            return null;
        }
        // Take the first (most recent) match
        return entities.get(0).getAesTwmUid();
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

    public void copyQueryCondition(CustomerCareConditionVo source, CustomerCareConditionVo target) {
        if (source == null || target == null) {
            return;
        }
        target.setMsisdn(source.getMsisdn());
        target.setTwmUid(source.getTwmUid());
        target.setSubid(source.getSubid());
        target.setAesTwmUid(source.getAesTwmUid());
        target.setUserId(source.getUserId());
        target.setIdentifierType(source.getIdentifierType());
        target.setTransactionId(source.getTransactionId());
        target.setOrderId(source.getOrderId());
        target.setRewardName(source.getRewardName());
        target.setTransactionType(source.getTransactionType());
        target.setPaymentMethod(source.getPaymentMethod());
        target.setCatalogOwner(source.getCatalogOwner());
        // Time range filters removed
    }
}
