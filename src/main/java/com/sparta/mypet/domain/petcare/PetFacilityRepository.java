package com.sparta.mypet.domain.petcare;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sparta.mypet.domain.petcare.entity.FacilityCategory;
import com.sparta.mypet.domain.petcare.entity.PetFacility;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface PetFacilityRepository extends JpaRepository<PetFacility, Long> {

	@Query("SELECT f FROM PetFacility f WHERE " + "(:category IS NULL OR f.category = :category) AND " +
		//"(:query IS NULL OR f.placeName LIKE %:query% OR f.addressName LIKE %:query%) AND " + // 쿼리는 검색 기능에 추가
		"(:x IS NULL OR :y IS NULL OR :radius IS NULL OR "
		+ "(6371 * acos(cos(radians(:y)) * cos(radians(f.latitude)) * cos(radians(f.longitude) - radians(:x)) + sin(radians(:y)) * sin(radians(f.latitude)))) <= :radius) AND "
		+ "f.isClosed = false")
	Page<PetFacility> searchPetFacilities(@Param("category") FacilityCategory category, @Param("x") Double x,
		@Param("y") Double y, @Param("radius") Double radius, Pageable pageable);
}
