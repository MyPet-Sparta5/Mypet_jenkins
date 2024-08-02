package com.sparta.mypet.common.exception.custom;

import com.sparta.mypet.common.entity.GlobalMessage;

public class ReportStatusDuplicationException extends RuntimeException {
	public ReportStatusDuplicationException(String message) {
		super(message);
	}

	public ReportStatusDuplicationException(GlobalMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}