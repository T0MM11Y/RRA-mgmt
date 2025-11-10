package com.twm.mgmt.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.twm.mgmt.persistence.entity.AccountPermissionProgramEntity;

public interface AccountPermissionProgramRepository extends JpaRepository<AccountPermissionProgramEntity, Long> {

	List<AccountPermissionProgramEntity> findByAccountId(Long accountId);

	AccountPermissionProgramEntity findByAccountIdAndProgramId(Long accountId, Long programId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	long deleteByAccountId(Long accountId);
}
