package com.twm.mgmt.model.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.twm.mgmt.persistence.dto.TransactionRecordDto;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class TransactionHistoryResultVo implements Serializable {

	private static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String DEFAULT_MONTH_PATTERN = "yyyy-MM";

	private Long id;

	private String transactionId;

	private String orderId;

	private String rewardName;

	private String rewardType;

	private String transactionType;

	private String transactionStatus;

	private String reportMonth;

	private String paymentMethod;

	private String catalogOwner;

	private String homeOpco;

	private String hoTime;

	private String coTime;

	private String transactionTime;

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

	public TransactionHistoryResultVo(TransactionRecordDto dto) {
		this.id = dto.getId();
		this.transactionId = dto.getTransactionId();
		this.orderId = dto.getOrderId();
		this.rewardName = dto.getRewardName();
		this.rewardType = dto.getRewardType();
		this.transactionType = dto.getTransactionType();
		this.transactionStatus = dto.getReportType();
		this.reportMonth = formatMonth(dto.getReportMonth());
		this.paymentMethod = dto.getPaymentMethod();
		this.catalogOwner = dto.getCatalogOwner();
		this.homeOpco = dto.getHomeOpco();
		this.hoTime = formatDate(dto.getHoTime());
		this.coTime = formatDate(dto.getCoTime());
		this.transactionTime = formatDate(dto.getTransactionTime());
		this.rewardQuantity = dto.getRewardQuantity();
		this.paymentPoint = dto.getPaymentPoint();
		this.paymentCash = dto.getPaymentCash();
		this.paymentCurrency = dto.getPaymentCurrency();
		this.pricePoint = dto.getPricePoint();
		this.priceCash = dto.getPriceCash();
		this.priceCurrency = dto.getPriceCurrency();
		this.defaultMarginPercentage = dto.getDefaultMarginPercentage();
		this.additionalMarginPercentage = dto.getAdditionalMarginPercentage();
		this.discountPercentage = dto.getDiscountPercentage();
		this.catalogOwnerCurrencyExchangeRate = dto.getCatalogOwnerCurrencyExchangeRate();
		this.catalogOwnerPointToCurrencyRatio = dto.getCatalogOwnerPointToCurrencyRatio();
		this.homeOpcoCurrencyExchangeRate = dto.getHomeOpcoCurrencyExchangeRate();
		this.homeOpcoPointToCurrencyRatio = dto.getHomeOpcoPointToCurrencyRatio();
		this.userId = dto.getUserId();
		this.rewardId = dto.getRewardId();
		this.externalRewardId = dto.getExternalRewardId();
		this.refundOriginalTransactionId = dto.getRefundOriginalTransactionId();
		this.refundPoint = dto.getRefundPoint();
		this.refundCash = dto.getRefundCash();
		this.refundCurrency = dto.getRefundCurrency();
		this.identityType = dto.getIdentityType();
		this.identityValue = dto.getIdentityValue();
		this.ban = dto.getBan();
		this.sourceFile = dto.getSourceFile();
		this.rowNo = dto.getRowNo();
		this.rowHash = dto.getRowHash();
	}

	private String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat(DEFAULT_DATE_TIME_PATTERN).format(date);
	}

	private String formatMonth(Date date) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat(DEFAULT_MONTH_PATTERN).format(date);
	}
}
