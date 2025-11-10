package com.twm.mgmt.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.twm.mgmt.persistence.entity.ProgramEntity;

public interface ProgramRepository extends JpaRepository<ProgramEntity, Long> {

	/**
	 * Find programs by menu ID
	 */
	List<ProgramEntity> findByMenuIdOrderByOrderNoAsc(Long menuId);

	/**
	 * Find program by URI pattern
	 */
	@Query(value = "SELECT * FROM public.program p WHERE :uri LIKE p.program_uri || '%' LIMIT 1", nativeQuery = true)
	Optional<ProgramEntity> findByUriPattern(@Param("uri") String uri);

	/**
	 * Find program by exact program URI
	 */
	ProgramEntity findByProgramUri(String programUri);

	/**
	 * Find programs by a list of program IDs
	 */
	@Query("from ProgramEntity p where p.programId in :programIds")
	List<ProgramEntity> findPrograms(@Param("programIds") List<Long> programIds);
}