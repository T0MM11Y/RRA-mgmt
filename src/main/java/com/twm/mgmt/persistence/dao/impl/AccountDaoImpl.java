package com.twm.mgmt.persistence.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.stream.Collectors;

import java.math.BigInteger;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.twm.mgmt.config.MoDbConfig;
import com.twm.mgmt.model.account.AccountConditionVo;
import com.twm.mgmt.persistence.dao.AccountDao;
import com.twm.mgmt.persistence.dto.AccountDto;
import com.twm.mgmt.utils.StringUtilsEx;

@Repository
public class AccountDaoImpl implements AccountDao {

	@PersistenceContext(unitName = MoDbConfig.PERSISTENCE_UNIT)
	private EntityManager manager;

	@SuppressWarnings("unchecked")
	@Override
	public List<AccountDto> findByCondition(AccountConditionVo condition) {
		String sql = composeSql(condition, false);

		Map<String, Object> params = composeParams(condition);

		Query q = manager.createNativeQuery(sql);
		// Map parameters
		for (Entry<String, Object> entry : params.entrySet()) {
			q.setParameter(entry.getKey(), entry.getValue());
		}

		q.setFirstResult((condition.getNumber() - 1) * condition.getSize());
		q.setMaxResults(condition.getSize());

		// Hibernate 6: unwrap to NativeQuery to ensure proper result handling
		NativeQuery<Object[]> nq = (NativeQuery<Object[]>) q.unwrap(NativeQuery.class);
		List<Object[]> rows = nq.getResultList();

		return rows.stream().map(r -> {
			AccountDto dto = new AccountDto();
			int i = 0;
			Object v;
			v = r[i++]; // accountId
			if (v instanceof BigInteger)
				dto.setAccountId(((BigInteger) v).longValue());
			else if (v instanceof Number)
				dto.setAccountId(((Number) v).longValue());
			v = r[i++]; // userName
			dto.setUserName(v == null ? null : v.toString());
			v = r[i++]; // email
			dto.setEmail(v == null ? null : v.toString());
			v = r[i++]; // roleId
			if (v instanceof BigInteger)
				dto.setRoleId(((BigInteger) v).longValue());
			else if (v instanceof Number)
				dto.setRoleId(((Number) v).longValue());
			v = r[i++]; // roleName
			dto.setRoleName(v == null ? null : v.toString());
			v = r[i++]; // departmentId
			if (v instanceof BigInteger)
				dto.setDepartmentId(((BigInteger) v).longValue());
			else if (v instanceof Number)
				dto.setDepartmentId(((Number) v).longValue());
			v = r[i++]; // departmentName
			dto.setDepartmentName(v == null ? null : v.toString());
			v = r[i++]; // enabled
			dto.setEnabled(v == null ? null : v.toString());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public Integer countByCondition(AccountConditionVo condition) {
		String sql = composeSql(condition, true);

		Map<String, Object> params = composeParams(condition);

		Query query = manager.createNativeQuery(sql);

		for (Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		return ((Number) query.getSingleResult()).intValue();
	}

	private String composeSql(AccountConditionVo condition, boolean isCount) {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT");

		if (isCount) {
			sb.append(" COUNT(a.ACCOUNT_ID)");
		} else {
			sb.append(" a.ACCOUNT_ID as accountId, a.USER_NAME as userName, a.EMAIL as email,");

			sb.append(" a.ROLE_ID as roleId, b.ROLE_NAME as roleName,");

			sb.append(" a.DEPARTMENT_ID as departmentId, c.DEPARTMENT_NAME as departmentName,");

			sb.append(" a.ENABLED as enabled ");
		}

		sb.append(" FROM ").append(MoDbConfig.ACCOUNT_SCHEMA).append(".ACCOUNT a");

		sb.append(" LEFT JOIN ").append(MoDbConfig.ACCOUNT_SCHEMA).append(".ROLE b on a.ROLE_ID = b.ROLE_ID");

		sb.append(" LEFT JOIN ").append(MoDbConfig.ACCOUNT_SCHEMA)
				.append(".DEPARTMENT c on a.DEPARTMENT_ID = c.DEPARTMENT_ID");

		sb.append(" WHERE 1 = 1");

		if (StringUtilsEx.isNotBlank(condition.getDepartmentId())) {
			sb.append(" AND a.DEPARTMENT_ID = :departmentId ");
		}

		if (StringUtilsEx.isNotBlank(condition.getRoleId())) {
			sb.append(" AND a.ROLE_ID = :roleId ");
		}

		return sb.toString();
	}

	private Map<String, Object> composeParams(AccountConditionVo condition) {
		Map<String, Object> params = new HashMap<>();

		if (StringUtilsEx.isNotBlank(condition.getDepartmentId())) {
			params.put("departmentId", Long.valueOf(condition.getDepartmentId()));
		}

		if (StringUtilsEx.isNotBlank(condition.getRoleId())) {
			params.put("roleId", Long.valueOf(condition.getRoleId()));
		}

		return params;
	}

}
