package com.twm.mgmt.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.twm.mgmt.persistence.entity.RolePermissionProgramEntity;

public interface RolePermissionProgramRepository extends JpaRepository<RolePermissionProgramEntity, Long> {

	List<RolePermissionProgramEntity> findByRoleId(Long roleId);

	List<RolePermissionProgramEntity> findByProgramId(Long programId);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	long deleteByRoleId(Long roleId);
}
