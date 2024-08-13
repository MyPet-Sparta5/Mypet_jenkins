package com.sparta.mypet.domain.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class LeaveCallbackRequestDto {
	@JsonProperty("user_id")
	private String userId;
}
