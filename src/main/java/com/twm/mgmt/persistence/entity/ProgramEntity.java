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
@Table(name = "PROGRAM", schema = MoDbConfig.ACCOUNT_SCHEMA)
@Entity
@SuppressWarnings("serial")
public class ProgramEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "programSeq")
	@SequenceGenerator(name = "programSeq", sequenceName = "PROGRAM_SEQ", allocationSize = 1, schema = MoDbConfig.ACCOUNT_SCHEMA)
	@Column(name = "PROGRAM_ID")
	private Long programId;

	@Column(name = "MENU_ID")
	private Long menuId;

	@Column(name = "ORDER_NO")
	private Integer orderNo;

	@Column(name = "PROGRAM_NAME", length = 150)
	private String programName;

	@Column(name = "PROGRAM_URI", length = 255)
	private String programUri;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "CREATE_ACCOUNT")
	private Long createAccount;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "UPDATE_ACCOUNT")
	private Long updateAccount;

}
