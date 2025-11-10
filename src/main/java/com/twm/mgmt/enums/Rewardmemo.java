package com.twm.mgmt.enums;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Rewardmemo {

	Rewardmemo1(1, "基本回饋"),

	Rewardmemo2(2, "滿額回饋"),

	Rewardmemo3(3, "加碼回饋(P2)"),

	Rewardmemo4(4, "欠費檢核不過"),

	Rewardmemo5(5, "黑名單檢核不過"),

	Rewardmemo6(6, "擇優不過"),

	Rewardmemo7(7, "客編有誤"),

	Rewardmemo8(8, "momo回饋"),

	Rewardmemo9(9, "門號檢核不過"),

	Rewardmemo10(10, "合約檢核不過"),
	
	Rewardmemo11(11, "舊資料Migration"),
	
	UNKNOWN(12, "其他"),
	;
	
	

	private Integer code;

	private String desc;

	Rewardmemo(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static Rewardmemo find(BigDecimal code) {
		if (code != null) {
			for (Rewardmemo type : values()) {
				if (code.intValue() == type.getCode().intValue()) {

					return type;
				}
			}
		}

		return UNKNOWN;
	}

	public static Map<Integer, String> getOptions() {

		return Stream.of(Rewardmemo1, Rewardmemo2, Rewardmemo3, Rewardmemo4, Rewardmemo5, Rewardmemo6, Rewardmemo7, Rewardmemo8, Rewardmemo9, Rewardmemo10, Rewardmemo11).collect(Collectors.toMap(Rewardmemo::getCode, Rewardmemo::getDesc));
	}

	public static Map<Integer, String> getOptions2() {
		return Stream.of(Rewardmemo1, Rewardmemo2, Rewardmemo3, Rewardmemo6, Rewardmemo7, Rewardmemo8, Rewardmemo11).collect(Collectors.toMap(Rewardmemo::getCode, Rewardmemo::getDesc));
	}

}
