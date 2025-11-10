package com.twm.mgmt.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.twm.mgmt.config.MoDbConfig;

import lombok.Data;

@Data
@Table(name = "OPCO", schema = MoDbConfig.ACCOUNT_SCHEMA)
@Entity
@SuppressWarnings("serial")
public class OpcoEntity implements Serializable {

	@Id
	@Column(name = "OPCO_ID")
	private String opcoId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "CREATE_ACCOUNT")
	private Long createAccount;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "UPDATE_ACCOUNT")
	private Long updateAccount;
}
