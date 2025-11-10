package com.twm.mgmt.model.common;

import java.io.Serializable;
import java.util.List;

import com.twm.mgmt.persistence.entity.MenuEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@SuppressWarnings("serial")
public class MenuVo implements Serializable {

	private Long menuId;

	private String name;

	private List<SubMenuVo> subVos;
	
	private MenuEntity menuEntity;

	public MenuVo(String name, List<SubMenuVo> subVos,MenuEntity menu) {
		this.name = name;
		this.subVos = subVos;
		this.menuEntity = menu;
	}

}
