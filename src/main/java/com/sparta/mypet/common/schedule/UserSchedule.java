package com.sparta.mypet.common.schedule;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.UserNotFoundException;
import com.sparta.mypet.domain.auth.UserService;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Scheduler")
@Component
@RequiredArgsConstructor
public class UserSchedule {

	private final UserService userService;

	@Scheduled(cron = "0 0 0 * * *") //매일 자정에 실행
	//@Scheduled(cron = "*/60 * * * * *") 테스트용 1분짜리
	@Transactional
	public void updateUserActive() {
		log.info("유저 정상화 시작");
		List<User> userList = userService.findExpiredSuspendedUsers();

		for (User user : userList) {
			try {
				user.updateUserStatus(UserStatus.ACTIVE);
			} catch (UserNotFoundException e) {
				log.error(user.getId() + " : " + GlobalMessage.USER_REACTIVE_FAIL.getMessage());
			}
		}

	}
}
