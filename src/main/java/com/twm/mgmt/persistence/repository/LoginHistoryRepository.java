package com.twm.mgmt.persistence.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.twm.mgmt.persistence.entity.LoginHistoryEntity;


@Transactional(rollbackFor = { Exception.class })
public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long> {
    @Query(value = "SELECT ranked.ACCOUNT_ID, ranked.login_date " +
            "FROM ( " +
            "    SELECT lh.ACCOUNT_ID, lh.LOGIN_DATE AS login_date, " +
            "           ROW_NUMBER() OVER (PARTITION BY lh.ACCOUNT_ID ORDER BY lh.LOGIN_DATE DESC) AS rn " +
            "    FROM public.LOGIN_HISTORY lh " +
            ") ranked " +
            "LEFT JOIN public.ACCOUNT a ON a.ACCOUNT_ID = ranked.ACCOUNT_ID " +
            "WHERE rn = 1 AND a.ENABLED = 'Y' " +
            "ORDER BY ACCOUNT_ID ASC", 
   nativeQuery = true)
List<Object[]> findLatestLoginForEachAccount();

}
