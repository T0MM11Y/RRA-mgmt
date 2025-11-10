package com.twm.mgmt.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twm.mgmt.persistence.dao.DepartmentDao;
import com.twm.mgmt.persistence.entity.DepartmentEntity;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long>, DepartmentDao {

	/**
	 * 取得已啟用的部門
	 * 
	 * @return
	 */
	@Query("SELECT d FROM DepartmentEntity d WHERE d.enabled = 'Y' ORDER BY d.departmentId")
	List<DepartmentEntity> findEnabledDepartment();

	/**
	 * 更新部門狀態
	 * 
	 * @param departmentId
	 * @param enabled
	 * @return
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE DepartmentEntity d SET d.enabled = :enabled, d.updateDate = CURRENT_TIMESTAMP WHERE d.departmentId = :departmentId")
	int updateEnabledByDepartmentId(@Param("departmentId")Long departmentId, @Param("enabled")String enabled);

	
	DepartmentEntity findBydepartmentId(Long departmentId);
	
	List<DepartmentEntity> getBydepartmentId(Long departmentId);
	

	
}
