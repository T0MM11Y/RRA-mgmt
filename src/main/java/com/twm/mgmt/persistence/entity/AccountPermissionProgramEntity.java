package com.twm.mgmt.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.twm.mgmt.config.MoDbConfig;

import lombok.Data;

@Data
@Table(name = "ACCOUNT_PERMISSION_PROGRAM", schema = MoDbConfig.ACCOUNT_SCHEMA)
@Entity
@SuppressWarnings("serial")
public class AccountPermissionProgramEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountPermissionProgramSeq")
	@SequenceGenerator(name = "accountPermissionProgramSeq", sequenceName = "ACCOUNT_PERMISSION_PROGRAM_SEQ", allocationSize = 1, schema = MoDbConfig.ACCOUNT_SCHEMA)
	@Column(name = "PERMISSION_ID")
	private Long permissionId;

	@Column(name = "ACCOUNT_ID")
	private Long accountId;

	@Column(name = "PROGRAM_ID")
	private Long programId;

	@Column(name = "ENABLED", length = 1)
	private String enabled = "N";

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "CREATE_ACCOUNT")
	private Long createAccount;

}
