package com.twm.mgmt.persistence.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.twm.mgmt.config.MoDbConfig;
import com.twm.mgmt.model.customer.TransactionHistoryConditionVo;
import com.twm.mgmt.persistence.dao.TransactionRecordDao;
import com.twm.mgmt.persistence.dto.TransactionRecordDto;
import com.twm.mgmt.utils.StringUtilsEx;

@Repository
public class TransactionRecordDaoImpl implements TransactionRecordDao {

	private static final String HOME_OPCO_PARAM = "homeOpco";
	private static final String TRANSACTION_TYPE_PARAM = "transactionType";
	private static final String TRANSACTION_ID_PARAM = "transactionId";
	private static final String ORDER_ID_PARAM = "orderId";
	private static final String REWARD_NAME_PARAM = "rewardName";
	private static final String PAYMENT_METHOD_PARAM = "paymentMethod";
	private static final String CATALOG_OWNER_PARAM = "catalogOwner";
	// Time range filter params removed
	private static final String AES_TWM_UID_PARAM = "aesTwmUid";
	// Enabled flag constant removed (no longer used)
	private static final String DEFAULT_HOME_OPCO = "TWM";

	private static final Map<String, String> SORTABLE_COLUMNS;

	static {
		Map<String, String> columns = new HashMap<>();
		columns.put("transactionId", "tr.TRANSACTION_ID");
		columns.put("orderId", "tr.ORDER_ID");
		columns.put("rewardName", "tr.REWARD_NAME");
		columns.put("rewardType", "tr.REWARD_TYPE");
		columns.put("transactionType", "tr.TRANSACTION_TYPE");
		columns.put("transactionStatus", "tr.REPORT_TYPE");
		columns.put("reportMonth", "tr.REPORT_MONTH");
		columns.put("paymentMethod", "tr.PAYMENT_METHOD");
		columns.put("catalogOwner", "tr.CATALOG_OWNER");
		columns.put("hoTime", "tr.HO_TIME");
		columns.put("coTime", "tr.CO_TIME");
		columns.put("transactionTime", "tr.TRANSACTION_TIME");
		columns.put("pricePoint", "tr.PRICE_POINT");
		columns.put("priceCash", "tr.PRICE_CASH");
		columns.put("paymentPoint", "tr.PAYMENT_POINT");
		SORTABLE_COLUMNS = columns;
	}

	@PersistenceContext(unitName = MoDbConfig.PERSISTENCE_UNIT)
	private EntityManager entityManager;

	@Override
	public List<TransactionRecordDto> findByCondition(TransactionHistoryConditionVo condition) {
		String sql = composeSql(condition, false);
		Map<String, Object> params = composeParams(condition);
		Query query = entityManager.createNativeQuery(sql);

		for (Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		int pageNumber = condition.getNumber() == null ? 1 : condition.getNumber();
		int pageSize = condition.getSize() == null ? 10 : condition.getSize();
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);

		@SuppressWarnings("unchecked")
		NativeQuery<Object[]> nativeQuery = (NativeQuery<Object[]>) query.unwrap(NativeQuery.class);
		List<Object[]> rows = nativeQuery.getResultList();

		return rows.stream().map(this::mapRowToDto).collect(Collectors.toList());
	}

	@Override
	public Integer countByCondition(TransactionHistoryConditionVo condition) {
		String sql = composeSql(condition, true);
		Map<String, Object> params = composeParams(condition);

		Query query = entityManager.createNativeQuery(sql);
		for (Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		Object result = query.getSingleResult();
		if (result instanceof BigInteger) {
			return ((BigInteger) result).intValue();
		}
		return result == null ? 0 : ((Number) result).intValue();
	}

	@Override
	public String findLatestAesTwmUidByUserId(String userId) {
		if (StringUtilsEx.isBlank(userId)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT tr.IDENTITY_VALUE ");
		sb.append("FROM ").append(MoDbConfig.ACCOUNT_SCHEMA).append(".TRANSACTION_RECORD tr ");
		sb.append("WHERE tr.USER_ID = :userId ");
		sb.append("AND tr.IDENTITY_VALUE IS NOT NULL ");
		sb.append("ORDER BY tr.TRANSACTION_TIME DESC");
		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("userId", userId);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<Object> list = query.getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		Object v = list.get(0);
		return v == null ? null : v.toString();
	}

	@Override
	public String findLatestUserIdByAesTwmUid(String aesTwmUid) {
		if (StringUtilsEx.isBlank(aesTwmUid)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT tr.USER_ID ");
		sb.append("FROM ").append(MoDbConfig.ACCOUNT_SCHEMA).append(".TRANSACTION_RECORD tr ");
		sb.append("WHERE tr.IDENTITY_VALUE = :aesTwmUid ");
		sb.append("AND tr.USER_ID IS NOT NULL ");
		sb.append("ORDER BY tr.TRANSACTION_TIME DESC");
		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter("aesTwmUid", aesTwmUid);
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<Object> list = query.getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		Object v = list.get(0);
		return v == null ? null : v.toString();
	}

	@Override
	public List<String> findDistinctTransactionTypes(String homeOpco) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT COALESCE(tr.TRANSACTION_TYPE, '') ");
		sb.append("FROM ").append(MoDbConfig.ACCOUNT_SCHEMA).append(".TRANSACTION_RECORD tr ");
		sb.append("WHERE tr.HOME_OPCO = :").append(HOME_OPCO_PARAM);
		sb.append(" AND COALESCE(tr.TRANSACTION_TYPE, '') <> ''");
		sb.append(" ORDER BY 1");

		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter(HOME_OPCO_PARAM, StringUtilsEx.isBlank(homeOpco) ? DEFAULT_HOME_OPCO : homeOpco);

		@SuppressWarnings("unchecked")
		List<String> values = query.getResultList();
		return new ArrayList<>(new java.util.LinkedHashSet<>(values));
	}

	@Override
	public List<String> findDistinctPaymentMethods(String homeOpco) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT COALESCE(tr.PAYMENT_METHOD, '') ");
		sb.append("FROM ").append(MoDbConfig.ACCOUNT_SCHEMA).append(".TRANSACTION_RECORD tr ");
		sb.append("WHERE tr.HOME_OPCO = :").append(HOME_OPCO_PARAM);
		sb.append(" AND COALESCE(tr.PAYMENT_METHOD, '') <> ''");
		sb.append(" ORDER BY 1");

		Query query = entityManager.createNativeQuery(sb.toString());
		query.setParameter(HOME_OPCO_PARAM, StringUtilsEx.isBlank(homeOpco) ? DEFAULT_HOME_OPCO : homeOpco);

		@SuppressWarnings("unchecked")
		List<String> values = query.getResultList();
		return new ArrayList<>(new java.util.LinkedHashSet<>(values));
	}

	private String composeSql(TransactionHistoryConditionVo condition, boolean isCount) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");

		if (isCount) {
			sb.append("COUNT(1) ");
		} else {
			sb.append("tr.ID, ");
			sb.append("tr.TRANSACTION_ID, ");
			sb.append("tr.ORDER_ID, ");
			sb.append("tr.REWARD_NAME, ");
			sb.append("tr.REWARD_TYPE, ");
			sb.append("tr.TRANSACTION_TYPE, ");
			sb.append("tr.REPORT_TYPE, ");
			sb.append("tr.REPORT_MONTH, ");
			sb.append("tr.CATALOG_OWNER, ");
			sb.append("tr.HOME_OPCO, ");
			sb.append("tr.HO_TIME, ");
			sb.append("tr.CO_TIME, ");
			sb.append("tr.TRANSACTION_TIME, ");
			sb.append("tr.REWARD_QUANTITY, ");
			sb.append("tr.PAYMENT_METHOD, ");
			sb.append("tr.PAYMENT_POINT, ");
			sb.append("tr.PAYMENT_CASH, ");
			sb.append("tr.PAYMENT_CURRENCY, ");
			sb.append("tr.PRICE_POINT, ");
			sb.append("tr.PRICE_CASH, ");
			sb.append("tr.PRICE_CURRENCY, ");
			sb.append("tr.DEFAULT_MARGIN_PERCENTAGE, ");
			sb.append("tr.ADDITIONAL_MARGIN_PERCENTAGE, ");
			sb.append("tr.DISCOUNT_PERCENTAGE, ");
			sb.append("tr.CATALOG_OWNER_CURRENCY_EXCHANGE_RATE, ");
			sb.append("tr.CATALOG_OWNER_POINT_TO_CURRENCY_RATIO, ");
			sb.append("tr.HOME_OPCO_CURRENCY_EXCHANGE_RATE, ");
			sb.append("tr.HOME_OPCO_POINT_TO_CURRENCY_RATIO, ");
			sb.append("tr.USER_ID, ");
			sb.append("tr.REWARD_ID, ");
			sb.append("tr.EXTERNAL_REWARD_ID, ");
			sb.append("tr.REFUND_ORIGINAL_TRANSACTION_ID, ");
			sb.append("tr.REFUND_POINT, ");
			sb.append("tr.REFUND_CASH, ");
			sb.append("tr.REFUND_CURRENCY, ");
			sb.append("tr.IDENTITY_TYPE, ");
			sb.append("tr.IDENTITY_VALUE, ");
			sb.append("tr.BAN, ");
			sb.append("tr.SOURCE_FILE, ");
			sb.append("tr.ROW_NO, ");
			sb.append("tr.ROW_HASH ");
		}

		sb.append("FROM ").append(MoDbConfig.ACCOUNT_SCHEMA).append(".TRANSACTION_RECORD tr ");
		sb.append("WHERE 1 = 1 ");
		sb.append("AND tr.HOME_OPCO = :").append(HOME_OPCO_PARAM).append(" ");
		// Removed enabled flag filtering as TRANSACTION_RECORD.ENABLED column was
		// dropped

		// Customer Care Step 2: restrict to selected user if provided
		if (StringUtilsEx.isNotBlank(condition.getAesTwmUid())) {
			sb.append("AND tr.IDENTITY_VALUE = :").append(AES_TWM_UID_PARAM).append(" ");
		}

		if (StringUtilsEx.isNotBlank(condition.getTransactionId())) {
			sb.append("AND LOWER(tr.TRANSACTION_ID) LIKE :").append(TRANSACTION_ID_PARAM).append(" ");
		}
		if (StringUtilsEx.isNotBlank(condition.getOrderId())) {
			sb.append("AND LOWER(tr.ORDER_ID) LIKE :").append(ORDER_ID_PARAM).append(" ");
		}
		if (StringUtilsEx.isNotBlank(condition.getRewardName())) {
			sb.append("AND LOWER(tr.REWARD_NAME) LIKE :").append(REWARD_NAME_PARAM).append(" ");
		}
		if (StringUtilsEx.isNotBlank(condition.getTransactionType())) {
			sb.append("AND tr.TRANSACTION_TYPE = :").append(TRANSACTION_TYPE_PARAM).append(" ");
		}
		if (StringUtilsEx.isNotBlank(condition.getPaymentMethod())) {
			sb.append("AND tr.PAYMENT_METHOD = :").append(PAYMENT_METHOD_PARAM).append(" ");
		}
		if (StringUtilsEx.isNotBlank(condition.getCatalogOwner())) {
			sb.append("AND tr.CATALOG_OWNER = :").append(CATALOG_OWNER_PARAM).append(" ");
		}
		// Time range filters removed

		if (!isCount) {
			sb.append(composeOrderBy(condition));
		}

		return sb.toString();
	}

	private Map<String, Object> composeParams(TransactionHistoryConditionVo condition) {
		Map<String, Object> params = new HashMap<>();
		params.put(HOME_OPCO_PARAM, DEFAULT_HOME_OPCO);

		if (StringUtilsEx.isNotBlank(condition.getAesTwmUid())) {
			params.put(AES_TWM_UID_PARAM, condition.getAesTwmUid().trim());
		}

		if (StringUtilsEx.isNotBlank(condition.getTransactionId())) {
			params.put(TRANSACTION_ID_PARAM, likeValue(condition.getTransactionId()));
		}
		if (StringUtilsEx.isNotBlank(condition.getOrderId())) {
			params.put(ORDER_ID_PARAM, likeValue(condition.getOrderId()));
		}
		if (StringUtilsEx.isNotBlank(condition.getRewardName())) {
			params.put(REWARD_NAME_PARAM, likeValue(condition.getRewardName()));
		}
		if (StringUtilsEx.isNotBlank(condition.getTransactionType())) {
			params.put(TRANSACTION_TYPE_PARAM, condition.getTransactionType());
		}
		if (StringUtilsEx.isNotBlank(condition.getPaymentMethod())) {
			params.put(PAYMENT_METHOD_PARAM, condition.getPaymentMethod());
		}
		if (StringUtilsEx.isNotBlank(condition.getCatalogOwner())) {
			params.put(CATALOG_OWNER_PARAM, condition.getCatalogOwner());
		}
		// Time range filters removed

		return params;
	}

	private String composeOrderBy(TransactionHistoryConditionVo condition) {
		String sortName = condition.getName();
		String sortOrder = condition.getOrder();
		String column = SORTABLE_COLUMNS.getOrDefault(sortName, "tr.HO_TIME");
		String orderKeyword = "DESC";
		if ("asc".equalsIgnoreCase(sortOrder)) {
			orderKeyword = "ASC";
		} else if ("desc".equalsIgnoreCase(sortOrder)) {
			orderKeyword = "DESC";
		}

		return " ORDER BY " + column + " " + orderKeyword + ", tr.ID DESC";
	}

	private TransactionRecordDto mapRowToDto(Object[] row) {
		int i = 0;
		TransactionRecordDto dto = new TransactionRecordDto();
		dto.setId(asLong(row[i++]));
		dto.setTransactionId(asString(row[i++]));
		dto.setOrderId(asString(row[i++]));
		dto.setRewardName(asString(row[i++]));
		dto.setRewardType(asString(row[i++]));
		dto.setTransactionType(asString(row[i++]));
		dto.setReportType(asString(row[i++]));
		dto.setReportMonth(asDate(row[i++]));
		dto.setCatalogOwner(asString(row[i++]));
		dto.setHomeOpco(asString(row[i++]));
		dto.setHoTime(asDate(row[i++]));
		dto.setCoTime(asDate(row[i++]));
		dto.setTransactionTime(asDate(row[i++]));
		dto.setRewardQuantity(asInteger(row[i++]));
		dto.setPaymentMethod(asString(row[i++]));
		dto.setPaymentPoint(asBigDecimal(row[i++]));
		dto.setPaymentCash(asBigDecimal(row[i++]));
		dto.setPaymentCurrency(asString(row[i++]));
		dto.setPricePoint(asBigDecimal(row[i++]));
		dto.setPriceCash(asBigDecimal(row[i++]));
		dto.setPriceCurrency(asString(row[i++]));
		dto.setDefaultMarginPercentage(asBigDecimal(row[i++]));
		dto.setAdditionalMarginPercentage(asBigDecimal(row[i++]));
		dto.setDiscountPercentage(asBigDecimal(row[i++]));
		dto.setCatalogOwnerCurrencyExchangeRate(asBigDecimal(row[i++]));
		dto.setCatalogOwnerPointToCurrencyRatio(asBigDecimal(row[i++]));
		dto.setHomeOpcoCurrencyExchangeRate(asBigDecimal(row[i++]));
		dto.setHomeOpcoPointToCurrencyRatio(asBigDecimal(row[i++]));
		dto.setUserId(asString(row[i++]));
		dto.setRewardId(asString(row[i++]));
		dto.setExternalRewardId(asString(row[i++]));
		dto.setRefundOriginalTransactionId(asString(row[i++]));
		dto.setRefundPoint(asBigDecimal(row[i++]));
		dto.setRefundCash(asBigDecimal(row[i++]));
		dto.setRefundCurrency(asString(row[i++]));
		dto.setIdentityType(asString(row[i++]));
		dto.setIdentityValue(asString(row[i++]));
		dto.setBan(asString(row[i++]));
		dto.setSourceFile(asString(row[i++]));
		dto.setRowNo(asInteger(row[i++]));
		dto.setRowHash(asString(row[i++]));
		return dto;
	}

	// Time range helper methods removed

	private String likeValue(String value) {
		return "%" + value.trim().toLowerCase() + "%";
	}

	private Long asLong(Object value) {
		if (value instanceof BigInteger) {
			return ((BigInteger) value).longValue();
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		return value == null ? null : Long.valueOf(value.toString());
	}

	private String asString(Object value) {
		return value == null ? null : value.toString();
	}

	private static final DateTimeFormatter OFFSET_WITH_SPACE =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXX");
	private static final DateTimeFormatter OFFSET_WITH_SPACE_COLON =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssxxx");
	private static final DateTimeFormatter SIMPLE_LOCAL =
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private java.util.Date asDate(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof java.util.Date) {
			return (java.util.Date) value;
		}
		if (value instanceof OffsetDateTime) {
			return java.util.Date.from(((OffsetDateTime) value).toInstant());
		}
		if (value instanceof ZonedDateTime) {
			return java.util.Date.from(((ZonedDateTime) value).toInstant());
		}
		if (value instanceof LocalDateTime) {
			return java.util.Date
					.from(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant());
		}
		if (value instanceof Instant) {
			return java.util.Date.from((Instant) value);
		}
		if (value instanceof Number) {
			return new java.util.Date(((Number) value).longValue());
		}
		if (value instanceof CharSequence) {
			java.util.Date parsed = parseDateString(value.toString());
			if (parsed != null) {
				return parsed;
			}
		}
		return null;
	}

	private java.util.Date parseDateString(String raw) {
		if (raw == null) {
			return null;
		}
		String text = raw.trim();
		if (text.isEmpty()) {
			return null;
		}
		try {
			return java.util.Date.from(Instant.parse(text));
		} catch (DateTimeParseException ignored) {
			// fall through
		}
		try {
			return java.util.Date.from(OffsetDateTime.parse(text).toInstant());
		} catch (DateTimeParseException ignored) {
			// fall through
		}
		try {
			return java.util.Date.from(OffsetDateTime.parse(text, OFFSET_WITH_SPACE).toInstant());
		} catch (DateTimeParseException ignored) {
			// fall through
		}
		try {
			return java.util.Date
					.from(OffsetDateTime.parse(text, OFFSET_WITH_SPACE_COLON).toInstant());
		} catch (DateTimeParseException ignored) {
			// fall through
		}
		try {
			LocalDateTime local = LocalDateTime.parse(text, SIMPLE_LOCAL);
			return java.util.Date.from(local.atZone(ZoneId.systemDefault()).toInstant());
		} catch (DateTimeParseException ignored) {
			// fall through
		}
		try {
			long epoch = Long.parseLong(text);
			return new java.util.Date(epoch);
		} catch (NumberFormatException ignored) {
			return null;
		}
	}

	private BigDecimal asBigDecimal(Object value) {
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		if (value instanceof Number) {
			return BigDecimal.valueOf(((Number) value).doubleValue());
		}
		return null;
	}

	private Integer asInteger(Object value) {
		if (value instanceof BigInteger) {
			return ((BigInteger) value).intValue();
		}
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		return null;
	}
}
