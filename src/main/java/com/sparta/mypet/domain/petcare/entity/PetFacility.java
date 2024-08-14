package com.sparta.mypet.domain.petcare.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Table(name = "pet_facilities")
@Entity
@Getter
public class PetFacility {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FacilityCategory category; // 카테고리

	@Column(nullable = false, unique = true)
	private String placeName; // 가게 이름
	@Column(nullable = false)
	private Double longitude; // x, 경도
	@Column(nullable = false)
	private Double latitude; // y, 위도
	@Column(nullable = false)
	private String addressName; // 일반 주소
	@Column(nullable = false)
	private String roadAddressName; // 도로명
	@Column
	private String phone; // 전화 번호
	@Column
	private String operatingHours; // 영업 시간
	@Column(nullable = false)
	private Boolean isClosed; // 폐업을 했는지?

}
