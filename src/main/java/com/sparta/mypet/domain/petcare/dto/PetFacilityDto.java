package com.sparta.mypet.domain.petcare.dto;

import com.sparta.mypet.domain.petcare.entity.PetFacility;

import lombok.Getter;

@Getter
public class PetFacilityDto {
	private final Long id;
	private final String placeName;
	private final String addressName;
	private final String roadAddressName;
	private final double latitude;
	private final double longitude;
	private final String category;
	private final String phone;
	private final String operatingHours;
	private final boolean isClosed;

	public PetFacilityDto(PetFacility petFacility) {
		this.id = petFacility.getId();
		this.placeName = petFacility.getPlaceName();
		this.addressName = petFacility.getAddressName();
		this.roadAddressName = petFacility.getRoadAddressName();
		this.latitude = petFacility.getLatitude();
		this.longitude = petFacility.getLongitude();
		this.category = petFacility.getCategory().name();
		this.phone = petFacility.getPhone();
		this.operatingHours = petFacility.getOperatingHours();
		this.isClosed = petFacility.isClosed();
	}
}
