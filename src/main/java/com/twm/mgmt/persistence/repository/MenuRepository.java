package com.twm.mgmt.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.twm.mgmt.persistence.entity.MenuEntity;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

	@Query("SELECT m FROM MenuEntity m WHERE m.menuId IN (:menuIds) ORDER BY m.orderNo")
	List<MenuEntity> findMenus(@Param("menuIds")List<Long> menuIds);

}
