package com.twm.mgmt.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.twm.mgmt.config.MoDbConfig;

import lombok.Data;

@Data
@Table(name = "TRANSACTION_RECORD", schema = MoDbConfig.ACCOUNT_SCHEMA)
@Entity
@SuppressWarnings("serial")
public class TransactionRecordEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactionRecordSeq")
	@SequenceGenerator(name = "transactionRecordSeq", sequenceName = "TRANSACTION_RECORD_SEQ", allocationSize = 1, schema = MoDbConfig.ACCOUNT_SCHEMA)
	@Column(name = "ID")
	private Long id;

	@Column(name = "REPORT_TYPE")
	private String reportType;

	@Column(name = "REPORT_MONTH")
	private Date reportMonth;

	@Column(name = "SOURCE_FILE")
	private String sourceFile;

	@Column(name = "ROW_NO")
	private Integer rowNo;

	@Column(name = "ROW_HASH")
	private String rowHash;

	@Column(name = "TRANSACTION_ID")
	private String transactionId;

	@Column(name = "TRANSACTION_TIME")
	private Date transactionTime;

	@Column(name = "ORDER_ID")
	private String orderId;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "CATALOG_OWNER")
	private String catalogOwner;

	@Column(name = "HOME_OPCO")
	private String homeOpco;

	@Column(name = "REWARD_ID")
	private String rewardId;

	@Column(name = "EXTERNAL_REWARD_ID")
	private String externalRewardId;

	@Column(name = "REWARD_TYPE")
	private String rewardType;

	@Column(name = "REWARD_QUANTITY")
	private Integer rewardQuantity;

	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;

	@Column(name = "REWARD_NAME")
	private String rewardName;

	@Column(name = "DEFAULT_MARGIN_PERCENTAGE")
	private BigDecimal defaultMarginPercentage;

	@Column(name = "ADDITIONAL_MARGIN_PERCENTAGE")
	private BigDecimal additionalMarginPercentage;

	@Column(name = "DISCOUNT_PERCENTAGE")
	private BigDecimal discountPercentage;

	@Column(name = "PRICE_POINT")
	private BigDecimal pricePoint;

	@Column(name = "PRICE_CASH")
	private BigDecimal priceCash;

	@Column(name = "PRICE_CURRENCY")
	private String priceCurrency;

	@Column(name = "CATALOG_OWNER_CURRENCY_EXCHANGE_RATE")
	private BigDecimal catalogOwnerCurrencyExchangeRate;

	@Column(name = "CATALOG_OWNER_POINT_TO_CURRENCY_RATIO")
	private BigDecimal catalogOwnerPointToCurrencyRatio;

	@Column(name = "HOME_OPCO_CURRENCY_EXCHANGE_RATE")
	private BigDecimal homeOpcoCurrencyExchangeRate;

	@Column(name = "HOME_OPCO_POINT_TO_CURRENCY_RATIO")
	private BigDecimal homeOpcoPointToCurrencyRatio;

	@Column(name = "PAYMENT_METHOD")
	private String paymentMethod;

	@Column(name = "PAYMENT_POINT")
	private BigDecimal paymentPoint;

	@Column(name = "PAYMENT_CASH")
	private BigDecimal paymentCash;

	@Column(name = "PAYMENT_CURRENCY")
	private String paymentCurrency;

	@Column(name = "REFUND_ORIGINAL_TRANSACTION_ID")
	private String refundOriginalTransactionId;

	@Column(name = "REFUND_POINT")
	private BigDecimal refundPoint;

	@Column(name = "REFUND_CASH")
	private BigDecimal refundCash;

	@Column(name = "REFUND_CURRENCY")
	private String refundCurrency;

	@Column(name = "CO_TIME")
	private Date coTime;

	@Column(name = "HO_TIME")
	private Date hoTime;

	@Column(name = "BAN")
	private String ban;

	@Column(name = "IDENTITY_TYPE")
	private String identityType;

	@Column(name = "IDENTITY_VALUE")
	private String identityValue;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "CREATE_ACCOUNT")
	private Long createAccount;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "UPDATE_ACCOUNT")
	private Long updateAccount;
}
