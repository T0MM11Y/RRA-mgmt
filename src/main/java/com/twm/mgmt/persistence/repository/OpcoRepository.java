package com.twm.mgmt.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twm.mgmt.persistence.entity.OpcoEntity;

public interface OpcoRepository extends JpaRepository<OpcoEntity, String> {

	List<OpcoEntity> findAllByOrderByNameAsc();
}
