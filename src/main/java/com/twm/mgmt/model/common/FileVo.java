package com.twm.mgmt.model.common;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@SuppressWarnings("serial")
public class FileVo implements Serializable {

	/** guid */
	private BigDecimal fileId;

	/** 檔案名稱 */
	private String fileName;

	/** 檔案內容 */
	private byte[] fileContent;

	/** 檔案類型 */
	private String contentType;

	/** 檔案大小 */
	private String fileSize;

}
