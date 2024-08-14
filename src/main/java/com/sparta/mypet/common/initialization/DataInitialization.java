package com.sparta.mypet.common.initialization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sparta.mypet.domain.auth.UserRepository;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserRole;
import com.sparta.mypet.domain.auth.entity.UserStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Initialization")
@Component
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${admin.user.email}")
	private String adminEmail;

	@Value("${admin.user.password}")
	private String adminPassword;

	@Override
	public void run(String... args) {
		if (userRepository.count() == 0) {

			String encodePassword = passwordEncoder.encode(adminPassword);
			String nickname = "MyPetAdmin";

			User user = User.builder()
				.email(adminEmail)
				.password(encodePassword)
				.nickname(nickname)
				.suspensionCount(0)
				.role(UserRole.ADMIN)
				.status(UserStatus.ACTIVE)
				.build();

			userRepository.save(user);
			log.info("관리자 계정 생성!");
		}
	}
}
