package com.twm.mgmt.persistence.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.springframework.stereotype.Repository;

import com.twm.mgmt.config.MoDbConfig;
import com.twm.mgmt.model.account.DepartmentConditionVo;
import com.twm.mgmt.persistence.dao.DepartmentDao;
import com.twm.mgmt.persistence.entity.DepartmentEntity;
import com.twm.mgmt.utils.StringUtilsEx;

@Repository
public class DepartmentDaoImpl implements DepartmentDao {

	@PersistenceContext(unitName = MoDbConfig.PERSISTENCE_UNIT)
	private EntityManager manager;

	@SuppressWarnings("unchecked")
	@Override
	public List<DepartmentEntity> findByCondition(DepartmentConditionVo condition) {
		String sql = composeSql(condition, false);

		Map<String, Object> params = composeParams(condition);

		Query query = manager.createQuery(sql, DepartmentEntity.class);

		for (Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		query.setFirstResult((condition.getNumber() - 1) * condition.getSize());

		query.setMaxResults(condition.getSize());

		return query.getResultList();

	}

	@Override
	public Integer countByCondition(DepartmentConditionVo condition) {
		String sql = composeSql(condition, true);

		Map<String, Object> params = composeParams(condition);

		Query query = manager.createQuery(sql);

		for (Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		return ((Long) query.getSingleResult()).intValue();
	}

	private String composeSql(DepartmentConditionVo condition, boolean isCount) {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT");

		if (isCount) {
			sb.append(" COUNT(d.departmentId)");
		} else {
			sb.append(" d");
		}

		sb.append(" FROM DepartmentEntity d");

		sb.append(" WHERE 1 = 1");

		if (StringUtilsEx.isNotBlank(condition.getDepartmentName())) {
			sb.append(" AND LOWER(d.departmentName) LIKE :departmentName");
		}

		return sb.toString();
	}

	private Map<String, Object> composeParams(DepartmentConditionVo condition) {
		Map<String, Object> params = new HashMap<>();

		if (StringUtilsEx.isNotBlank(condition.getDepartmentName())) {
			params.put("departmentName", "%" + condition.getDepartmentName().toLowerCase() + "%");
		}

		return params;
	}

}
