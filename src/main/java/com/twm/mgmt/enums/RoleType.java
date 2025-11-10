package com.twm.mgmt.enums;

import com.twm.mgmt.utils.StringUtilsEx;

/**
 * 功能
 */
public enum RoleType {

	/** 平台PM */
	PLATFORM_PM,

	UNKNOWN;

	public static RoleType find(String name) {
		if (StringUtilsEx.isNotBlank(name)) {
			for (RoleType type : values()) {
				if (StringUtilsEx.equalsIgnoreCase(name, type.name())) {

					return type;
				}
			}
		}

		return UNKNOWN;
	}

	public boolean isUnknown() {

		return equals(UNKNOWN);
	}

	public boolean isPlatformPM() {

		return equals(PLATFORM_PM);
	}

}
