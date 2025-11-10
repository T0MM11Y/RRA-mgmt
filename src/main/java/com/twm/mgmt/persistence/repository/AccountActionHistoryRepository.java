package com.twm.mgmt.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.twm.mgmt.persistence.entity.AccountActionHistoryEntity;

public interface AccountActionHistoryRepository extends JpaRepository<AccountActionHistoryEntity, Long> {

	@Query(value = "SELECT aah.ACCOUNT_ID FROM public.ACCOUNT_ACTION_HISTORY aah WHERE aah.EXECUTE_CONTENT LIKE '%部門別:' || :departmentId || ';AP 帳號:' || :sourceId || ';%' ORDER BY aah.EXECUTE_DATE DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
	Long getAccountIdByExecuteContentAP(@Param("departmentId") Long departmentId, @Param("sourceId") String sourceId);

	@Query(value = "SELECT aah.REQUEST_ID, CAST(aah.EXECUTE_CONTENT AS VARCHAR), aA.ACCOUNT_ID AS ACCOUNT_ID_aA, aA.USER_NAME AS USER_NAME_aA, aE.ACCOUNT_ID AS ACCOUNT_ID_aE, aE.USER_NAME AS USER_NAME_aE, TO_CHAR(aah.EXECUTE_DATE,'YYYY-MM-DD HH24:MI:SS') FROM public.ACCOUNT_ACTION_HISTORY aah "
			+ "LEFT JOIN public.ACCOUNT aE ON aE.ACCOUNT_ID = aah.EXECUTE_ACCOUNT_ID "
			+ "LEFT JOIN public.ACCOUNT aA ON aA.ACCOUNT_ID = aah.ACCOUNT_ID "
			+ "WHERE (:executeAccountId IS NULL OR aah.EXECUTE_ACCOUNT_ID = :executeAccountId) "
			+ "AND (:accountId IS NULL OR aah.ACCOUNT_ID = :accountId) "
			+ "AND (:requestId IS NULL OR aah.REQUEST_ID LIKE :requestId) "
			+ "ORDER BY aah.EXECUTE_DATE DESC", nativeQuery = true)
	List<Object[]> findAccountActionHistoriesByCriteria(@Param("executeAccountId") Long executeAccountId,
			@Param("accountId") Long accountId, @Param("requestId") String requestId);

}