package com.sparta.mypet.domain.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.PasswordInvalidException;
import com.sparta.mypet.common.exception.custom.UserEmailDuplicateException;
import com.sparta.mypet.domain.auth.dto.SignupRequestDto;
import com.sparta.mypet.domain.auth.dto.SignupResponseDto;
import com.sparta.mypet.domain.auth.dto.UserWithPostListResponseDto;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserRole;
import com.sparta.mypet.domain.auth.entity.UserStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SignupResponseDto signup(SignupRequestDto requestDto) {

		Optional<User> duplicateUser = userRepository.findByEmail(requestDto.getEmail());

		if (duplicateUser.isPresent()) {
			throw new UserEmailDuplicateException(GlobalMessage.USER_EMAIL_DUPLICATE);
		}

		if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
			throw new PasswordInvalidException(GlobalMessage.PASSWORD_INVALID);
		}

		String encodePassword = passwordEncoder.encode(requestDto.getPassword());

		User user = User.builder()
			.email(requestDto.getEmail())
			.password(encodePassword)
			.nickname(requestDto.getNickname())
			.penaltyCount(0)
			.role(UserRole.USER)
			.status(UserStatus.ACTIVE)
			.build();

		User saveUser = userRepository.save(user);

		return SignupResponseDto.builder()
			.user(saveUser)
			.build();
	}

	public UserWithPostListResponseDto getUser(String email) {

		User user = findUserByEmail(email);

		return UserWithPostListResponseDto.builder()
			.user(user)
			.postList(user.getPostList())
			.build();
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(
			() -> new UsernameNotFoundException(GlobalMessage.USER_EMAIL_NOT_FOUND.getMessage())
		);
	}
}
