package com.twm.mgmt.validator;

import java.text.MessageFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.twm.mgmt.constant.RraConstants;
import com.twm.mgmt.enums.RraErrorCode;
import com.twm.mgmt.model.common.ErrorVo;
import com.twm.mgmt.utils.DateUtilsEx;
import com.twm.mgmt.utils.StringUtilsEx;

public abstract class BaseValidator {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private List<ErrorVo> errors;

	public abstract List<ErrorVo> validate();

	protected void addError(String fieldId, String message) {
		if (errors == null) {
			errors = new ArrayList<>();
		}

		errors.add(new ErrorVo(fieldId, message));
	}

	protected void addError(String fieldId, RraErrorCode code) {
		addError(fieldId, code.getDesc());
	}

	protected List<ErrorVo> getResult() {
		if (errors == null) {
			errors = new ArrayList<>();
		}

		return errors.stream().filter(error -> error != null).collect(Collectors.toList());
	}

	/**
	 * 檢核日期
	 * 
	 * @param startFieldId
	 * @param endFieldId
	 * @param startDate
	 * @param endDate
	 * @param rangeOfMonth
	 */
	protected void validateDateRange(String startFieldId, String endFieldId, String startDate, String endDate, int rangeOfMonth) {
		String fieldId = "";

		Date start = null, end = null;

		try {
			if (StringUtilsEx.isNotBlank(startDate)) {
				fieldId = startFieldId;

				start = DateUtilsEx.startDate(startDate);
			}

			if (StringUtilsEx.isNotBlank(endDate)) {
				fieldId = endFieldId;

				end = DateUtilsEx.endDate(endDate);
			}

			if (start != null && end != null) {
				if (start.after(end)) {
					addError(startFieldId, RraErrorCode.START_DATE_GREATER_THAN_END_DATE_ERROR);
				}
			}
		} catch (Exception e) {
			addError(fieldId, RraErrorCode.DATE_FORMATTER_ERROR);
		}

		if (rangeOfMonth > 0) {
			long between = DateUtilsEx.between(start, end, ChronoUnit.MILLIS);

			if (between > (long) rangeOfMonth * RraConstants.HOURS * RraConstants.MINUTES * RraConstants.SECONDS * RraConstants.MILLISECONDS) {
				addError(startFieldId, MessageFormat.format(RraErrorCode.DATE_RANGE_GREATER_THAN_MONTH.getDesc(), rangeOfMonth));
			}
		}
	}

}
