package com.twm.mgmt.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.twm.mgmt.config.MoDbConfig;

import lombok.Data;

@Data
@Table(name = "USER_PROFILE", schema = MoDbConfig.ACCOUNT_SCHEMA)
@Entity
@SuppressWarnings("serial")
public class UserProfileEntity implements Serializable {

    @Id
    @Column(name = "TWM_UID")
    private String twmUid;

    @Column(name = "AES_TWM_UID")
    private String aesTwmUid;

    @Column(name = "SUBID")
    private String subid;

    @Column(name = "MSISDN")
    private String msisdn;

    @Column(name = "POINT_TYPE")
    private String pointType;

    @Column(name = "POINT_UID")
    private String pointUid;

    @Column(name = "IDENTITY_TYPE")
    private String identityType;

    @Column(name = "TIER")
    private String tier;

    @Column(name = "TOTAL_POINT")
    private BigDecimal totalPoint;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;
}

