package com.twm.mgmt.model.common;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("serial")
public class QueryResultVo extends PaginationVo {

	/** 總筆數 */
	private Integer total;

	private List<?> result;

	public QueryResultVo(PaginationVo pagination) {
		setNumber(pagination.getNumber());

		setSize(pagination.getSize());

		setName(pagination.getName());

		setOrder(pagination.getOrder());
	}

}
