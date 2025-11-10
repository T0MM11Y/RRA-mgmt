package com.twm.mgmt.enums;

/**
 * 功能
 */
public enum ActionType {

	/** 新增 */
	ADD(1),

	/** 編輯 */
	EDIT(2),

	/** 刪除 */
	DELETE(-1),

	/** 查詢 */
	QUERY(10),

	UNKNOWN(-9);

	ActionType(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}

	public static ActionType find(Integer code) {
		if (code != null) {
			for (ActionType type : values()) {
				if (code.intValue() == type.getCode().intValue()) {

					return type;
				}
			}
		}

		return UNKNOWN;
	}

	public boolean isAdd() {

		return equals(ADD);
	}

	public boolean isEdit() {

		return equals(EDIT);
	}

	public boolean isDelete() {

		return equals(DELETE);
	}

	public boolean isQuery() {

		return equals(QUERY);
	}

	public boolean isUnknown() {

		return equals(UNKNOWN);
	}

}
