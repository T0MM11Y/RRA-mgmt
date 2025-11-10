package com.twm.mgmt.service;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twm.mgmt.model.common.QueryResultVo;
import com.twm.mgmt.model.customer.CustomerCareConditionVo;
import com.twm.mgmt.model.customer.TransactionHistoryConditionVo;
import com.twm.mgmt.model.customer.TransactionHistoryResultVo;
import com.twm.mgmt.persistence.dto.TransactionRecordDto;
import com.twm.mgmt.persistence.entity.OpcoEntity;
import com.twm.mgmt.persistence.repository.OpcoRepository;
import com.twm.mgmt.persistence.repository.TransactionRecordRepository;
import com.twm.mgmt.utils.StringUtilsEx;

@Service
public class TransactionHistoryService extends BaseService {

	private static final String EXPORT_DATE_PATTERN = "yyyyMMdd_HHmmss";
	private static final String DEFAULT_HOME_OPCO = "TWM";
	private static final String[] EXPORT_HEADERS = { "Transaction ID", "Order ID", "Reward Name", "Reward Type",
			"Transaction Type", "Status", "Report Month", "Payment Method", "Catalog Owner", "Home OPCO", "HO Time",
			"CO Time", "Transaction Time", "Reward Quantity", "Payment Point", "Payment Cash", "Payment Currency",
			"Price Point", "Price Cash", "Price Currency", "Default Margin %", "Additional Margin %", "Discount %",
			"Catalog Owner FX Rate", "Catalog Owner Point/Currency Ratio", "Home OPCO FX Rate",
			"Home OPCO Point/Currency Ratio", "User ID", "Reward ID", "External Reward ID",
			"Refund Original Transaction ID", "Refund Point", "Refund Cash", "Refund Currency", "Identity Type",
			"Identity Value", "BAN", "Source File", "Row No", "Row Hash" };

	private final TransactionRecordRepository transactionRecordRepository;
	private final OpcoRepository opcoRepository;

	@Autowired
	public TransactionHistoryService(TransactionRecordRepository transactionRecordRepository,
			OpcoRepository opcoRepository) {
		this.transactionRecordRepository = transactionRecordRepository;
		this.opcoRepository = opcoRepository;
	}

	@Transactional(readOnly = true)
	public QueryResultVo findTransactionHistory(TransactionHistoryConditionVo condition) {
		normalizePagination(condition);
		QueryResultVo result = new QueryResultVo(condition);

		List<TransactionRecordDto> dtos = transactionRecordRepository.findByCondition(condition);
		Integer total = transactionRecordRepository.countByCondition(condition);

		List<TransactionHistoryResultVo> vos = dtos.stream().map(TransactionHistoryResultVo::new)
				.collect(Collectors.toList());
		result.setTotal(total);
		result.setResult(vos);
		return result;
	}

	@Transactional(readOnly = true)
	public QueryResultVo findTransactionHistoryByAesTwmUid(CustomerCareConditionVo condition) {
		normalizePaginationForCare(condition);
		QueryResultVo result = new QueryResultVo(condition);

		// Convert CustomerCareConditionVo to TransactionHistoryConditionVo
		TransactionHistoryConditionVo txCondition = new TransactionHistoryConditionVo();
		txCondition.setAesTwmUid(condition.getAesTwmUid());
		txCondition.setTransactionId(condition.getTransactionId());
		txCondition.setOrderId(condition.getOrderId());
		txCondition.setRewardName(condition.getRewardName());
		txCondition.setTransactionType(condition.getTransactionType());
		txCondition.setPaymentMethod(condition.getPaymentMethod());
		txCondition.setCatalogOwner(condition.getCatalogOwner());
		// Time range filters removed
		txCondition.setNumber(condition.getNumber());
		txCondition.setSize(condition.getSize());
		txCondition.setName(condition.getName());
		txCondition.setOrder(condition.getOrder());

		List<TransactionRecordDto> dtos = transactionRecordRepository.findByCondition(txCondition);
		Integer total = transactionRecordRepository.countByCondition(txCondition);

		List<TransactionHistoryResultVo> vos = dtos.stream().map(TransactionHistoryResultVo::new)
				.collect(Collectors.toList());
		result.setTotal(total);
		result.setResult(vos);
		return result;
	}

	@Transactional(readOnly = true)
	public List<String> findTransactionTypes() {
		return transactionRecordRepository.findDistinctTransactionTypes(DEFAULT_HOME_OPCO);
	}

	@Transactional(readOnly = true)
	public List<String> findPaymentMethods() {
		return transactionRecordRepository.findDistinctPaymentMethods(DEFAULT_HOME_OPCO);
	}

	@Transactional(readOnly = true)
	public List<OpcoEntity> findCatalogOwners() {
		return opcoRepository.findAllByOrderByNameAsc();
	}

	@Transactional(readOnly = true)
	public List<TransactionHistoryResultVo> findAllForExport(TransactionHistoryConditionVo condition) {
		List<TransactionRecordDto> dtos = transactionRecordRepository.findForExport(condition);
		return dtos.stream().map(TransactionHistoryResultVo::new).collect(Collectors.toList());
	}

	public void copyQueryCondition(TransactionHistoryConditionVo source, TransactionHistoryConditionVo target) {
		if (source == null || target == null) {
			return;
		}
		target.setTransactionId(source.getTransactionId());
		target.setOrderId(source.getOrderId());
		target.setRewardName(source.getRewardName());
		target.setTransactionType(source.getTransactionType());
		target.setPaymentMethod(source.getPaymentMethod());
		target.setCatalogOwner(source.getCatalogOwner());
		target.setAesTwmUid(source.getAesTwmUid());
		// Time range filters removed
		target.setName(source.getName());
		target.setOrder(source.getOrder());
		target.setSize(source.getSize());
	}

	public byte[] exportToCsv(List<TransactionHistoryResultVo> records) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.join(",", EXPORT_HEADERS));
		sb.append(System.lineSeparator());

		if (records != null && !records.isEmpty()) {
			for (TransactionHistoryResultVo vo : records) {
				Object[] row = buildExportRow(vo);
				for (Object value : row) {
					appendCsvValue(sb, value);
				}
				removeLastComma(sb);
				sb.append(System.lineSeparator());
			}
		}

		return sb.toString().getBytes(StandardCharsets.UTF_8);
	}

	public byte[] exportToExcel(List<TransactionHistoryResultVo> records) throws Exception {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("TWM Transactions");
			int rowIndex = 0;

			Row header = sheet.createRow(rowIndex++);
			for (int i = 0; i < EXPORT_HEADERS.length; i++) {
				Cell cell = header.createCell(i);
				cell.setCellValue(EXPORT_HEADERS[i]);
			}

			for (TransactionHistoryResultVo vo : records) {
				Row row = sheet.createRow(rowIndex++);
				Object[] rowData = buildExportRow(vo);
				for (int col = 0; col < rowData.length; col++) {
					row.createCell(col).setCellValue(defaultString(rowData[col]));
				}
			}

			for (int i = 0; i < EXPORT_HEADERS.length; i++) {
				sheet.autoSizeColumn(i);
			}

			try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
				workbook.write(out);
				return out.toByteArray();
			}
		}
	}

	public String buildExportFileName(String prefix, String extension) {
		String safePrefix = StringUtilsEx.isBlank(prefix) ? "transaction_history" : prefix;
		String timestamp = new SimpleDateFormat(EXPORT_DATE_PATTERN, Locale.getDefault()).format(new Date());
		return safePrefix + "_" + timestamp + "." + extension;
	}

	private void normalizePagination(TransactionHistoryConditionVo condition) {
		if (condition.getNumber() == null || condition.getNumber() <= 0) {
			condition.setNumber(1);
		}
		if (condition.getSize() == null || condition.getSize() <= 0) {
			condition.setSize(10);
		}
		if (StringUtilsEx.isBlank(condition.getOrder())) {
			condition.setOrder("desc");
		}
		if (StringUtilsEx.isBlank(condition.getName())) {
			condition.setName("hoTime");
		}
	}

	private void normalizePaginationForCare(CustomerCareConditionVo condition) {
		if (condition.getNumber() == null || condition.getNumber() <= 0) {
			condition.setNumber(1);
		}
		if (condition.getSize() == null || condition.getSize() <= 0) {
			condition.setSize(10);
		}
		if (StringUtilsEx.isBlank(condition.getOrder())) {
			condition.setOrder("desc");
		}
		if (StringUtilsEx.isBlank(condition.getName())) {
			condition.setName("hoTime");
		}
	}

	private Object[] buildExportRow(TransactionHistoryResultVo vo) {
		return new Object[] { vo.getTransactionId(), vo.getOrderId(), vo.getRewardName(), vo.getRewardType(),
				vo.getTransactionType(), vo.getTransactionStatus(), vo.getReportMonth(), vo.getPaymentMethod(),
				vo.getCatalogOwner(), vo.getHomeOpco(), vo.getHoTime(), vo.getCoTime(), vo.getTransactionTime(),
				vo.getRewardQuantity(), formatNumber(vo.getPaymentPoint()), formatNumber(vo.getPaymentCash()),
				vo.getPaymentCurrency(), formatNumber(vo.getPricePoint()), formatNumber(vo.getPriceCash()),
				vo.getPriceCurrency(), formatNumber(vo.getDefaultMarginPercentage()),
				formatNumber(vo.getAdditionalMarginPercentage()), formatNumber(vo.getDiscountPercentage()),
				formatNumber(vo.getCatalogOwnerCurrencyExchangeRate()),
				formatNumber(vo.getCatalogOwnerPointToCurrencyRatio()),
				formatNumber(vo.getHomeOpcoCurrencyExchangeRate()),
				formatNumber(vo.getHomeOpcoPointToCurrencyRatio()), vo.getUserId(), vo.getRewardId(),
				vo.getExternalRewardId(), vo.getRefundOriginalTransactionId(), formatNumber(vo.getRefundPoint()),
				formatNumber(vo.getRefundCash()), vo.getRefundCurrency(), vo.getIdentityType(), vo.getIdentityValue(),
				vo.getBan(), vo.getSourceFile(), vo.getRowNo(), vo.getRowHash() };
	}

	private void appendCsvValue(StringBuilder sb, Object value) {
		if (value == null) {
			sb.append(",");
			return;
		}
		String text = value.toString();
		boolean containsComma = text.contains(",");
		boolean containsQuote = text.contains("\"");
		boolean containsLineBreak = text.contains("\n") || text.contains("\r");
		if (containsQuote) {
			text = text.replace("\"", "\"\"");
		}
		if (containsComma || containsQuote || containsLineBreak) {
			sb.append("\"").append(text).append("\"").append(",");
		} else {
			sb.append(text).append(",");
		}
	}

	private void removeLastComma(StringBuilder sb) {
		if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
	}

	private String formatNumber(Number number) {
		if (number == null) {
			return "";
		}
		return String.format(Locale.US, "%s", number);
	}

	private String defaultString(Object obj) {
		return obj == null ? "" : obj.toString();
	}
}
