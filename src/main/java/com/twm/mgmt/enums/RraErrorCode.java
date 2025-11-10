package com.twm.mgmt.enums;

public enum RraErrorCode {

	ACCOUNT_NOT_FOUND("001", "用戶不存在，請聯絡資訊人員"),

	PERMISSION_DENY_ERROR("002", "您無使用該功能的權限"),

	IS_REQUIRED("003", "必填欄位，請輸入值"),

	INPUT_TEXT_OVER_MAXLENGTH("004", "超過長度限制，請重新輸入"),

	INPUT_TEXT_FORMAT_ERROR("005", "格式錯誤，請重新輸入"),

	ROLE_IS_EXIST("006", "角色已存在，請重新輸入"),

	PERMISSION_EMPTY_ERROR("007", "請勾選權限類型"),

	INPUT_TEXT_UNIQUE_SYMBOL_ERROR("008", "不可以含有特殊字元"),

	APPROVAL_LEVEL1_THE_SAME_ERROR("009", "第一關簽核人員不可相同"),

	APPROVAL_LEVEL2_THE_SAME_ERROR("010", "第二關簽核人員不可相同"),

	EMAIL_IS_EXIST("011", "E-Mail已存在"),

	DEPARTMENT_ID_IS_EXIST("012", "部門ID已存在"),

	SOURCE_ID_IS_EXIST("013", "SOURCE ID已存在"),

	EXPIRE_DATE_IS_DUPLICATE("014", "到期日不可重複"),

	QUERY_DATE_ARE_REQUIRED("015", "請輸入查詢起訖日"),

	DATE_FORMATTER_ERROR("016", "日期格式不符"),

	START_DATE_GREATER_THAN_END_DATE_ERROR("017", "起日不可大於訖日"),

	DATE_RANGE_GREATER_THAN_MONTH("018", "查詢區間不可超過{0}天"),

	APPROVAL_ACCOUNT_NOT_FOUND("019", "非系統單號{0}之簽核人員"),

	APPROVAL_DENY("020", "此單號已簽核"),
	
	INCOMPLETE_DATA_ENTRY("021","資料輸入不完全"),
	
	NUMBER_INPUT_CHARACTERS("022","輸入字數超過限制"),
	
	DATA_ERROR("023","資料錯誤"),
	
	DATA_INPUT_ERROR("024","資料輸入錯誤"),
	
	FILE_FORMAT_ERROR("025","檔案格式有誤"),
	
	TOTAL_REWARD_AMOUNT_FAIL("026","序號總金額不足匹配失敗"),
	
	MATCHTODB_FAIL("027","序號短少匹配失敗"),
	
	SERIAL_NUMBER_FILE_NAMED_INCORRECTLY("028","序號檔案命名錯誤"),

	VALIDATE_ERROR("998", "檢核失敗"),

	SYSTEM_ERROR("999", "系統錯誤"),
	
	SYSTEM_ERROR2("999", "系統發生錯誤，請聯繫系統管理員Yvette Yang。");

	private String code;

	private String desc;

	RraErrorCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

}
