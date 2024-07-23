package com.sparta.mypet.domain.auth;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.PasswordInvalidException;
import com.sparta.mypet.domain.auth.dto.LoginRequestDto;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	@Transactional
	public String login(LoginRequestDto requestDto) {

		User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
			() -> new UsernameNotFoundException(GlobalMessage.USER_EMAIL_NOT_FOUND.getMessage())
		);

		if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new PasswordInvalidException(GlobalMessage.PASSWORD_INVALID);
		}

		String accessTokenType = "access";
		String refreshTokenType = "refresh";

		String accessToken = jwtService.generateToken(accessTokenType, user.getRole(), user.getEmail());
		String refreshToken = jwtService.generateToken(refreshTokenType, user.getRole(), user.getEmail());

		user.updateRefreshToken(refreshToken);

		jwtService.setRefreshTokenAtCookie(refreshToken);
		jwtService.setHeaderWithAccessToken(accessToken);

		return accessToken;
	}
}
