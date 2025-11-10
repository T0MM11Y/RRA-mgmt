package com.twm.mgmt.persistence.dto;

import lombok.Data;

@Data
public class AccountActionHistoryDto {


    private String requestId;
    private String executeContent;
    private Long accountIdA;
    private String userNameA;
    private Long executeAccountId;
    private String executeUserName;
    private String executeDate;

    
    public AccountActionHistoryDto(String requestId, String executeContent, Long accountIdA, String userNameA, Long executeAccountId, String executeUserName, String executeDate) {
        this.requestId = requestId;
        this.executeContent = executeContent;
        this.accountIdA = accountIdA;
        this.userNameA = userNameA;
        this.executeAccountId = executeAccountId;
        this.executeUserName = executeUserName;
        this.executeDate = executeDate;
    }

}
