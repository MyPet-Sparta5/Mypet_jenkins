package com.sparta.mypet.domain.petcare;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.mypet.domain.petcare.dto.PetFacilityDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PetFacilityController {

	private final PetFacilityService petFacilityService;

	@GetMapping("/facilities/search")
	public Page<PetFacilityDto> searchFacilities(@RequestParam String category, @RequestParam double x,
		@RequestParam double y, @RequestParam double radius, @RequestParam int page, @RequestParam int size,
		@RequestParam String sort) {
		return petFacilityService.searchFacilities(category, x, y, radius, page, size, sort);
	}

}
