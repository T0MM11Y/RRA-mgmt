package com.twm.mgmt.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twm.mgmt.persistence.dao.TransactionRecordDao;
import com.twm.mgmt.persistence.entity.TransactionRecordEntity;

public interface TransactionRecordRepository
		extends JpaRepository<TransactionRecordEntity, Long>, TransactionRecordDao {

}
