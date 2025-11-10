package com.twm.mgmt.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.twm.mgmt.config.MoDbConfig;

import lombok.Data;

@Data
@Table(name = "MENU", schema = MoDbConfig.ACCOUNT_SCHEMA)
@Entity
@SuppressWarnings("serial")
public class MenuEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menuSeq")
	@SequenceGenerator(name = "menuSeq", sequenceName = "MENU_SEQ", allocationSize = 1, schema = MoDbConfig.ACCOUNT_SCHEMA)
	@Column(name = "MENU_ID")
	private Long menuId;

	@Column(name = "MENU_NAME", length = 150)
	private String menuName;

	@Column(name = "ORDER_NO")
	private Integer orderNo;

	@Column(name = "CREATE_DATE")
	private Date createDate;

	@Column(name = "CREATE_ACCOUNT")
	private Long createAccount;

	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	@Column(name = "UPDATE_ACCOUNT")
	private Long updateAccount;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_ID", insertable = false, updatable = false)
	private List<ProgramEntity> programs;

}
