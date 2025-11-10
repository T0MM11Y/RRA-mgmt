package com.twm.mgmt.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.twm.mgmt.config.MoDbConfig;

import lombok.Data;

@Data
@Table(name = "ACCOUNT", schema = MoDbConfig.ACCOUNT_SCHEMA)
@Entity
@SuppressWarnings("serial")
public class AccountEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountSeq")
	@SequenceGenerator(name = "accountSeq", sequenceName = "ACCOUNT_SEQ", allocationSize = 1, schema = MoDbConfig.ACCOUNT_SCHEMA)
	@Column(name = "ACCOUNT_ID")
	private Long accountId;

	@Column(name = "USER_ID", length = 120)
	private String userId;

	@Column(name = "USER_NAME", length = 120)
	private String userName;

	@Column(name = "EMAIL", length = 255)
	private String email;

	@Column(name = "MOBILE", length = 10)
	private String mobile;

	@Column(name = "ROLE_ID")
	private Long roleId;

	@Column(name = "DEPARTMENT_ID")
	private Long departmentId;

	@Column(name = "APPROVABLE", length = 1)
	private String approvable = "N";

	@Column(name = "ENABLED", length = 1)
	private String enabled = "Y";

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "CREATE_ACCOUNT")
	private Long createAccount;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "UPDATE_ACCOUNT")
	private Long updateAccount;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
	private RoleEntity role;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "DEPARTMENT_ID", insertable = false, updatable = false)
	private DepartmentEntity department;

}
