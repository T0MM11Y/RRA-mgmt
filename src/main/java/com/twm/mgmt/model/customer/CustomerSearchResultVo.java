package com.twm.mgmt.model.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.twm.mgmt.persistence.entity.UserProfileEntity;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class CustomerSearchResultVo implements Serializable {

    private String twmUid;
    private String aesTwmUid;
    private String subid;
    private String msisdn;
    private String tier;
    private BigDecimal totalPoint;
    private Date updateDate;

    public CustomerSearchResultVo() {}

    public CustomerSearchResultVo(UserProfileEntity e) {
        this.twmUid = e.getTwmUid();
        this.aesTwmUid = e.getAesTwmUid();
        this.subid = e.getSubid();
        this.msisdn = e.getMsisdn();
        this.tier = e.getTier();
        this.totalPoint = e.getTotalPoint();
        this.updateDate = e.getUpdateDate();
    }
}

