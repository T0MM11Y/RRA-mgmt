package com.twm.mgmt.persistence.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransactionRecordDaoImplTest {

    private TransactionRecordDaoImpl dao;
    private Method asDate;

    @BeforeEach
    void setUp() throws Exception {
        dao = new TransactionRecordDaoImpl();
        asDate = TransactionRecordDaoImpl.class.getDeclaredMethod("asDate", Object.class);
        asDate.setAccessible(true);
    }

    @Test
    void asDateParsesOffsetWithoutTSeparator() throws Exception {
        Date result = (Date) asDate.invoke(dao, "2025-08-15 13:00:00+00");
        assertThat(result).isNotNull();
        assertThat(result.toInstant()).isEqualTo(Instant.parse("2025-08-15T13:00:00Z"));
    }

    @Test
    void asDateHandlesLocalDateTimeInstances() throws Exception {
        LocalDateTime local = LocalDateTime.of(2025, 8, 15, 13, 0, 0);
        Date result = (Date) asDate.invoke(dao, local);
        assertThat(result).isNotNull();
        ZoneId zoneId = ZoneId.systemDefault();
        assertThat(result.toInstant()).isEqualTo(local.atZone(zoneId).toInstant());
    }

    @Test
    void asDateHandlesEpochNumbers() throws Exception {
        long epochMillis = Instant.parse("2025-08-15T13:00:00Z").toEpochMilli();
        Date result = (Date) asDate.invoke(dao, epochMillis);
        assertThat(result).isNotNull();
        assertThat(result.toInstant()).isEqualTo(Instant.ofEpochMilli(epochMillis));
    }
}
