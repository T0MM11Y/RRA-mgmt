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
import com.twm.mgmt.utils.StringUtilsEx;

@Service
public class CustomerCareService extends BaseService {

    private final UserProfileRepository userProfileRepository;
    private final TransactionHistoryService transactionHistoryService;

    @Autowired
    public CustomerCareService(UserProfileRepository userProfileRepository,
            TransactionHistoryService transactionHistoryService) {
        this.userProfileRepository = userProfileRepository;
        this.transactionHistoryService = transactionHistoryService;
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

    private String resolveAesTwmUid(CustomerCareConditionVo condition) {
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
        target.setTransactionId(source.getTransactionId());
        target.setOrderId(source.getOrderId());
        target.setRewardName(source.getRewardName());
        target.setTransactionType(source.getTransactionType());
        target.setPaymentMethod(source.getPaymentMethod());
        target.setCatalogOwner(source.getCatalogOwner());
        // Time range filters removed
    }
}
