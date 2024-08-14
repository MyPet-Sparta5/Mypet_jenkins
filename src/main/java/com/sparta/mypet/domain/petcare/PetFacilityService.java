package com.sparta.mypet.domain.petcare;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.util.PaginationUtil;
import com.sparta.mypet.domain.petcare.dto.PetFacilityDto;
import com.sparta.mypet.domain.petcare.entity.FacilityCategory;
import com.sparta.mypet.domain.petcare.entity.PetFacility;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PetFacilityService {

	private final PetFacilityRepository petFacilityRepository;

	@Transactional(readOnly = true)
	public Page<PetFacilityDto> searchFacilities(String category, double x, double y, double radius, int page, int size,
		String sort) {

		Pageable pageable = PaginationUtil.createPageable(page, size, sort);

		Double radiusInKm = radius / 1000.0; // m 단위로 입력받았다면 km 단위로 변환
		FacilityCategory facilityCategory = FacilityCategory.fromString(category);

		Page<PetFacility> facilityPage = petFacilityRepository.searchPetFacilities(facilityCategory, x, y, radiusInKm,
			pageable);

		return facilityPage.map(PetFacilityDto::new);
	}
}
