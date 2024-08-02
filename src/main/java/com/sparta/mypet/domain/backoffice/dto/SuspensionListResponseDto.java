package com.sparta.mypet.domain.backoffice.dto;

import java.time.LocalDateTime;

import com.sparta.mypet.domain.suspension.entity.Suspension;

import lombok.Getter;

@Getter
public class SuspensionListResponseDto {
	private final Long id;
	private final Long suspensionHandleUserId;
	private final String suspensionIssue;
	private final LocalDateTime suspensionStartDatetime;
	private final LocalDateTime suspensionEndDatetime;

	public SuspensionListResponseDto(Suspension suspension) {
		this.id = suspension.getId();
		this.suspensionHandleUserId = suspension.getSuspensionHandleUserId();
		this.suspensionIssue = suspension.getSuspensionIssue();
		this.suspensionStartDatetime = suspension.getSuspensionStartDatetime();
		this.suspensionEndDatetime = suspension.getSuspensionEndDatetime();
	}
}
