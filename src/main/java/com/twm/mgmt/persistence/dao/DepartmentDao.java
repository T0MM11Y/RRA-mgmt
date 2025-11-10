package com.twm.mgmt.persistence.dao;

import java.util.List;

import com.twm.mgmt.model.account.DepartmentConditionVo;
import com.twm.mgmt.persistence.entity.DepartmentEntity;

public interface DepartmentDao {

	List<DepartmentEntity> findByCondition(DepartmentConditionVo condition);

	Integer countByCondition(DepartmentConditionVo condition);

}
