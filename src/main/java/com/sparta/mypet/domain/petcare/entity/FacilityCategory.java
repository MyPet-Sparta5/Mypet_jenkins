package com.sparta.mypet.domain.petcare.entity;

public enum FacilityCategory {
	PET_HOSPITAL, // 동물 병원
	PET_HOTEL, // 예정: 동물 호텔
	PET_SALON, // 예정: 동물 미용실
	PET_CAFE; // 예정: 동물 카페

	public static FacilityCategory fromString(String categoryStr) {
		for (FacilityCategory category : values()) {
			if (category.name().equals(categoryStr)) {
				return category;
			}
		}
		return null;
	}
}
