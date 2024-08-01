package com.sparta.mypet.common.exception.custom;

import com.sparta.mypet.common.entity.GlobalMessage;

public class ReportDuplicationException extends RuntimeException {
	public ReportDuplicationException(String message) {
		super(message);
	}

	public ReportDuplicationException(GlobalMessage errorMessage) {
		super(errorMessage.getMessage());
	}
}
