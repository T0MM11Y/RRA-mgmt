package com.twm.mgmt.model.account;

import java.io.Serializable;

import com.twm.mgmt.persistence.entity.RoleEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@SuppressWarnings("serial")
public class RoleVo implements Serializable {

	private Long roleId;

	private String roleName;

	public RoleVo(RoleEntity entity) {
		this.roleId = entity.getRoleId();
		this.roleName = entity.getRoleName().replaceFirst("ROLE_", "");
	}

}
