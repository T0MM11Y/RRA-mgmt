package com.twm.mgmt.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twm.mgmt.persistence.entity.UserProfileEntity;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, String> {

    List<UserProfileEntity> findByTwmUidIgnoreCaseOrderByUpdateDateDesc(String twmUid);

    List<UserProfileEntity> findByMsisdnOrderByUpdateDateDesc(String msisdn);

    List<UserProfileEntity> findBySubidOrderByUpdateDateDesc(String subid);

    List<UserProfileEntity> findByAesTwmUidOrderByUpdateDateDesc(String aesTwmUid);
}
