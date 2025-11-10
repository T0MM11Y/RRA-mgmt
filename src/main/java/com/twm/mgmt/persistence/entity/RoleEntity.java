package com.twm.mgmt.persistence.entity;

import java.io.Serializable;

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
@Table(name = "ROLE", schema = MoDbConfig.ACCOUNT_SCHEMA)
@Entity
@SuppressWarnings("serial")
public class RoleEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roleSeq")
	@SequenceGenerator(name = "roleSeq", sequenceName = "ROLE_SEQ", allocationSize = 1, schema = MoDbConfig.ACCOUNT_SCHEMA)
	@Column(name = "ROLE_ID")
	private Long roleId;

	@Column(name = "ROLE_NAME", length = 255)
	private String roleName;

}
