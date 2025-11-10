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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "LOGIN_HISTORY", schema = MoDbConfig.CAMPAIGN_SCHEMA)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
public class LoginHistoryEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loginHistorySeq")
	@SequenceGenerator(name = "loginHistorySeq", sequenceName = "LOGIN_HISTORY_SEQ", allocationSize = 1, schema = MoDbConfig.CAMPAIGN_SCHEMA)
    @Column(name = "LOGIN_HISTORY_ID")
    private Long loginHistoryId;

    @Column(name = "LOGIN_DATE")
    private Date loginDate;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;
}
