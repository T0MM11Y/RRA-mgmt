package com.twm.mgmt.persistence.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class TransactionRecordDto {

	private Long id;

	private String transactionId;

	private String orderId;

	private String rewardName;

	private String rewardType;

	private String transactionType;

	private String reportType;

	private Date reportMonth;

	private String paymentMethod;

	private String catalogOwner;

	private String homeOpco;

	private Date hoTime;

	private Date coTime;

	private Date transactionTime;

	private Integer rewardQuantity;

	private BigDecimal paymentPoint;

	private BigDecimal paymentCash;

	private String paymentCurrency;

	private BigDecimal pricePoint;

	private BigDecimal priceCash;

	private String priceCurrency;

	private BigDecimal defaultMarginPercentage;

	private BigDecimal additionalMarginPercentage;

	private BigDecimal discountPercentage;

	private BigDecimal catalogOwnerCurrencyExchangeRate;

	private BigDecimal catalogOwnerPointToCurrencyRatio;

	private BigDecimal homeOpcoCurrencyExchangeRate;

	private BigDecimal homeOpcoPointToCurrencyRatio;

	private String userId;

	private String rewardId;

	private String externalRewardId;

	private String refundOriginalTransactionId;

	private BigDecimal refundPoint;

	private BigDecimal refundCash;

	private String refundCurrency;

	private String identityType;

	private String identityValue;

	private String ban;

	private String sourceFile;

	private Integer rowNo;

	private String rowHash;
}
