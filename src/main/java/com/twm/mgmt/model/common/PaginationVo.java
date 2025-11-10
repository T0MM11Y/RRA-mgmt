package com.twm.mgmt.model.common;

import java.io.Serializable;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class PaginationVo implements Serializable {

	/** 目前頁數 */
	private Integer number = 1;

	/** 顯示幾筆 */
	private Integer size = 10;

	/** 欄位名稱 */
	private String name;

	/** 順序(asc or desc) */
	private String order;
	
	/** 搜尋欄位變數 */
	private String search;

}
