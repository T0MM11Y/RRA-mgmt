package com.twm.mgmt.model.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@SuppressWarnings("serial")
public class ReceiveInfoVo implements Serializable {

	/** 簽核種類 */
	private String type;

	/** 單號 */
	private String id;

	/** 層級(L1 or L2) */
	private Integer level;

}
