package com.sparta.mypet.domain.petcare.dto;

import com.sparta.mypet.domain.petcare.entity.FacilityCategory;

import lombok.Getter;

@Getter
public class FacilitySearchRequestDto {
	private String query;
	private FacilityCategory category;
	private Double x;
	private Double y;
	private Double radius;
	private int page;
	private int size;
	private String sort;
}
