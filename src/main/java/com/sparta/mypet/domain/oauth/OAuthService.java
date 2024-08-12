package com.sparta.mypet.domain.oauth;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.UserEmailDuplicateException;
import com.sparta.mypet.common.exception.custom.UserStatusNotActiveException;
import com.sparta.mypet.domain.auth.UserService;
import com.sparta.mypet.domain.auth.dto.LoginResponseDto;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserStatus;
import com.sparta.mypet.domain.oauth.dto.TokenResponseDto;
import com.sparta.mypet.domain.oauth.entity.SocialAccount;
import com.sparta.mypet.domain.oauth.entity.SocialAccountInfo;
import com.sparta.mypet.domain.oauth.entity.SocialType;
import com.sparta.mypet.security.JwtService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public abstract class OAuthService {

	protected static final int REDIS_CACHE_TTL = 30; // 소셜 임시 데이터 만료 시간(분)

	@Value("${oauth2.grant-type}")
	protected String grantType;

	protected final RedisTemplate<String, Object> redisTemplate;
	protected final SocialAccountService socialAccountService;
	protected final ObjectMapper objectMapper;
	protected final UserService userService;
	protected final JwtService jwtService;

	@Transactional
	public LoginResponseDto processLogin(String authorizationCode) {
		TokenResponseDto tokenResponseDto = getAccessToken(authorizationCode, getRedirectLoginUrl());
		SocialAccountInfo socialAccountInfo = getUserInfo(tokenResponseDto);
		return loginOrRegisterUser(socialAccountInfo, getSocialType());
	}

	@Transactional
	public void processLeave(String email) {
		User user = userService.findUserByEmail(email);
		SocialAccount socialAccount = socialAccountService.findBySocialTypeAndUser(getSocialType(), user)
			.orElseThrow(() -> new IllegalArgumentException(GlobalMessage.SOCIAL_NOT_LINKED_ERROR.getMessage()));
		revokeSocialAccess(socialAccount);
		socialAccountService.deleteSocialAccount(socialAccount);
	}

	@Transactional
	public void processLink(String email, String authorizationCode) {
		User user = userService.findUserByEmail(email);
		TokenResponseDto tokenResponseDto = getAccessToken(authorizationCode, getRedirectLinkUrl());
		SocialAccountInfo socialAccountInfo = getUserInfo(tokenResponseDto);
		SocialAccount socialAccount = socialAccountService.createAndSaveSocialAccount(socialAccountInfo);
		socialAccount.updateUser(user);
	}

	public abstract SocialType getSocialType();

	protected abstract String getRedirectLinkUrl();

	protected abstract String getRedirectLoginUrl();

	protected abstract TokenResponseDto getAccessToken(String authorizationCode, String redirectUri);

	protected abstract SocialAccountInfo getUserInfo(TokenResponseDto responseDto);

	protected abstract void revokeSocialAccess(SocialAccount socialAccount);

	protected LoginResponseDto loginOrRegisterUser(SocialAccountInfo socialAccountInfo, SocialType socialType) {
		Optional<SocialAccount> optionalSocialAccount = socialAccountService.findBySocialTypeAndSocialId(socialType,
			socialAccountInfo.getSocialId());
		Optional<User> optionalUser = userService.findOptionalUserByEmail(socialAccountInfo.getEmail());

		if (optionalSocialAccount.isPresent()) {
			SocialAccount socialAccount = optionalSocialAccount.get();
			return handleExistingUser(socialAccount.getUser(), socialAccountInfo, socialType);
		}

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			handleUserStatusError(user.getStatus());
		}

		return handleNewUser(socialAccountInfo);
	}

	protected LoginResponseDto handleExistingUser(User user, SocialAccountInfo socialAccountInfo,
		SocialType socialType) {

		Optional<SocialAccount> optionalSocialAccount = socialAccountService.findBySocialTypeAndSocialId(socialType,
			socialAccountInfo.getSocialId());

		if (optionalSocialAccount.isEmpty()) {
			throw new UserEmailDuplicateException(GlobalMessage.EMAIL_ALREADY_USED.getMessage());
		}

		String accessToken = jwtService.generateAccessToken(user.getRole(), user.getEmail());
		String refreshToken = jwtService.generateRefreshToken(user.getEmail());

		user.updateRefreshToken(refreshToken);

		jwtService.setRefreshTokenAtCookie(refreshToken);
		jwtService.setAccessTokenAtHeader(accessToken);

		return LoginResponseDto.builder().user(user).build();
	}

	protected LoginResponseDto handleNewUser(SocialAccountInfo socialAccountInfo) {
		String registrationKey = saveTemporarySocialAccountInfo(socialAccountInfo);
		String registrationUrl = generateRegistrationUrl(registrationKey);

		return new LoginResponseDto(registrationUrl);
	}

	protected String saveTemporarySocialAccountInfo(SocialAccountInfo socialAccountInfo) {
		String registrationKey = UUID.randomUUID().toString();
		redisTemplate.opsForValue().set(registrationKey, socialAccountInfo, REDIS_CACHE_TTL, TimeUnit.MINUTES);
		return registrationKey;
	}

	protected String generateRegistrationUrl(String registrationKey) {
		return "http://localhost:3000/signup?key=" + registrationKey;
	}

	protected void handleUserStatusError(UserStatus userStatus) {
		switch (userStatus) {
			case WITHDRAWAL ->
				throw new UserStatusNotActiveException(GlobalMessage.USER_STATUS_WITHDRAWAL.getMessage());
			case SUSPENSION -> throw new UserStatusNotActiveException(GlobalMessage.USER_STATUS_STOP.getMessage());
			case ACTIVE -> throw new UserEmailDuplicateException(GlobalMessage.EMAIL_ALREADY_USED.getMessage());
		}
	}
}
