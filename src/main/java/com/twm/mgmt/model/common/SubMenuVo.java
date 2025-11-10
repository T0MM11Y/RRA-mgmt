package com.twm.mgmt.model.common;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@SuppressWarnings("serial")
public class SubMenuVo implements Serializable {

	private Boolean permission;

	private Long programId;

	private String name;

	private String url;

	private String statusName;

	public SubMenuVo(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public SubMenuVo(Long programId, String name) {
		this.programId = programId;
		this.name = name;
	}

}
