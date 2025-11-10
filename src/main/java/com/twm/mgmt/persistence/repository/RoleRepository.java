package com.twm.mgmt.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twm.mgmt.persistence.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

	RoleEntity getByRoleName(String roleName);

}
