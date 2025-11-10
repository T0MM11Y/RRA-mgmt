package com.twm.mgmt.model.account;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class PermissionVo implements Serializable {

	private Integer action;

	private Long roleId;

	private String roleName;

	private Long accountId;

	private List<List<String>> onPrograms;

	private List<List<String>> offPrograms;

}
