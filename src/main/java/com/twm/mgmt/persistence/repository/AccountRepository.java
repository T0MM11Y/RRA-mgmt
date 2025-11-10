package com.twm.mgmt.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.twm.mgmt.persistence.dao.AccountDao;
import com.twm.mgmt.persistence.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>, AccountDao {

	List<AccountEntity> findByUserId(String userId);

	List<AccountEntity> findByEmail(String email);

	@Query("SELECT a FROM AccountEntity a WHERE a.enabled = 'Y'")
	List<AccountEntity> findEnabledAccount();

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	@Query("UPDATE AccountEntity a SET a.enabled = :status, a.updateAccount = :updateAccount, a.updateDate = CURRENT_TIMESTAMP WHERE a.accountId = :accountId")
	int updateEnabledByAccountId(@Param("accountId") Long accountId, @Param("status") String status,
			@Param("updateAccount") Long updateAccount);

	List<AccountEntity> findByRoleIdOrderByUserIdAsc(Long roleId);

	List<AccountEntity> findAllByOrderByUserIdAsc();

	List<AccountEntity> findByDepartmentId(Long departmentId);

	List<AccountEntity> findByRoleId(Long roleId);

	List<AccountEntity> findByEnabled(String enabled);
}
