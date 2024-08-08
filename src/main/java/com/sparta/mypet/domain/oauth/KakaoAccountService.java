package com.sparta.mypet.domain.oauth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.UserEmailDuplicateException;
import com.sparta.mypet.common.exception.custom.UserStatusNotActiveException;
import com.sparta.mypet.domain.auth.UserService;
import com.sparta.mypet.domain.auth.dto.LoginResponseDto;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserStatus;
import com.sparta.mypet.domain.feign.KakaoAuthApi;
import com.sparta.mypet.domain.feign.KakaoUserApi;
import com.sparta.mypet.domain.oauth.dto.KakaoTokenResponseDto;
import com.sparta.mypet.domain.oauth.entity.SocialAccount;
import com.sparta.mypet.domain.oauth.entity.SocialAccountInfo;
import com.sparta.mypet.domain.oauth.entity.SocialType;
import com.sparta.mypet.security.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Kakao Login Service")
@Service
@RequiredArgsConstructor
public class KakaoAccountService {

	@Value("${kakao.client-id.key}")
	private String kakaoClientId;
	@Value("${kakao.client-secret.key}")
	private String kakaoClientSecret;
	@Value("${kakao.redirect.login.uri}")
	private String kakaoRedirectLoginUri;
	@Value("${kakao.redirect.link.uri}")
	private String kakaoRedirectLinkUri;
	@Value("${kakao.grant-type}")
	private String kakaoGrantType;
	@Value("${kakao.admin.key}")
	private String kakaoAdminKey;

	private static final int REDIS_CACHE_TTL = 30;
	private static final String TARGET_ID_TYPE = "user_id";

	private final KakaoAuthApi kakaoAuthApi;
	private final KakaoUserApi kakaoUserApi;
	private final ObjectMapper objectMapper;

	private final SocialAccountService socialAccountService;

	private final RedisTemplate<String, Object> redisTemplate;

	private final UserService userService;
	private final JwtService jwtService;

	@Transactional
	public LoginResponseDto processKakaoLogin(String authorizationCode) {

		KakaoTokenResponseDto kakaoTokenResponseDto = getKakaoAccessToken(authorizationCode, kakaoRedirectLoginUri);

		SocialAccountInfo socialAccountInfo = getKakaoUserInfo(kakaoTokenResponseDto.getAccessToken());

		return loginOrRegisterUser(socialAccountInfo);
	}

	@Transactional
	public void processKakaoLeave(String email) {

		User user = userService.findUserByEmail(email);

		// 카카오 계정 연동 확인
		SocialAccount socialAccount = socialAccountService.findBySocialTypeAndUser(SocialType.KAKAO, user)
			.orElseThrow(() -> new IllegalArgumentException(GlobalMessage.SOCIAL_NOT_LINKED_ERROR.getMessage()));

		// 카카오 API를 통해 연결 끊기
		disconnectFromKakao(socialAccount.getSocialId());

		socialAccountService.deleteSocialAccount(socialAccount);
	}

	@Transactional
	public void processKakaoLink(String email, String authorizationCode) {
		User user = userService.findUserByEmail(email);

		KakaoTokenResponseDto kakaoTokenResponseDto = getKakaoAccessToken(authorizationCode, kakaoRedirectLinkUri);

		SocialAccountInfo socialAccountInfo = getKakaoUserInfo(kakaoTokenResponseDto.getAccessToken());

		SocialAccount socialAccount = socialAccountService.createAndSaveSocialAccount(socialAccountInfo);

		socialAccount.updateUser(user);
	}

	/**
	 * 카카오 서버에 토큰 요청
	 * @param authorizationCode 인증 인가 코드
	 * @return 토큰 정보 반환
	 */
	private KakaoTokenResponseDto getKakaoAccessToken(String authorizationCode, String redirectUri) {
		ResponseEntity<String> response = kakaoAuthApi.getAccessToken(kakaoClientId, kakaoClientSecret, kakaoGrantType,
			redirectUri, authorizationCode);

		if (response.getStatusCode().is2xxSuccessful()) {
			try {
				return objectMapper.readValue(response.getBody(), KakaoTokenResponseDto.class);
			} catch (Exception e) {
				throw new IllegalArgumentException(GlobalMessage.JSON_PARSING_ERROR.getMessage());
			}
		} else {
			throw new IllegalArgumentException(
				GlobalMessage.KAKAO_SERVER_ERROR.getMessage() + response.getStatusCode());
		}
	}

	/**
	 * 카카오 유저 정보 요청
	 * @param accessToken Access Token 입력
	 * @return 소셜 계정 정보 반환
	 */
	private SocialAccountInfo getKakaoUserInfo(String accessToken) {
		Map<String, String> header = new HashMap<>();
		header.put(JwtService.HEADER, JwtService.TOKEN_PREFIX + accessToken);

		// 소셜 계정 정보 요청
		ResponseEntity<String> response = kakaoUserApi.getUserInfo(header);

		if (response.getStatusCode().is2xxSuccessful()) {
			try {
				JsonNode jsonNode = objectMapper.readTree(response.getBody());

				Long socialId = jsonNode.get("id").asLong();
				String nickname = jsonNode.at("/properties/nickname").asText();
				String email = jsonNode.at("/kakao_account/email").asText();

				return SocialAccountInfo.builder()
					.socialId(socialId)
					.socialType(SocialType.KAKAO)
					.email(email)
					.nickname(nickname)
					.build();
			} catch (JsonProcessingException e) {
				throw new IllegalArgumentException(
					GlobalMessage.JSON_PARSING_ERROR.getMessage() + " : " + e.getMessage());
			}
		} else {
			throw new IllegalArgumentException(
				GlobalMessage.KAKAO_SERVER_ERROR.getMessage() + response.getStatusCode());
		}
	}

	/**
	 * 카카오 연결 해제
	 * @param socialId 저장된 소셜 ID
	 */
	private void disconnectFromKakao(Long socialId) {
		Map<String, String> header = new HashMap<>();
		header.put("Authorization", "KakaoAK " + kakaoAdminKey);

		ResponseEntity<String> response = kakaoUserApi.unlinkUser(header, TARGET_ID_TYPE, socialId.toString());

		if (response.getStatusCode().is2xxSuccessful()) {
			log.info("카카오 계정 연결 해제 성공: {}", socialId);
		} else {
			throw new IllegalArgumentException("카카오 계정 연결 해제에 실패했습니다.");
		}
	}

	private LoginResponseDto loginOrRegisterUser(SocialAccountInfo socialAccountInfo) {

		Optional<SocialAccount> optionalSocialAccount = socialAccountService.findBySocialTypeAndSocialId(
			SocialType.KAKAO, socialAccountInfo.getSocialId());
		Optional<User> optionalUser = userService.findOptionalUserByEmail(socialAccountInfo.getEmail());

		// 소셜 계정이 있는 경우
		if (optionalSocialAccount.isPresent()) {
			SocialAccount socialAccount = optionalSocialAccount.get();
			return handleExistingUser(socialAccount.getUser(), socialAccountInfo);
		}

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			handleUserStatusError(user.getStatus());
		}

		return handleNewUser(socialAccountInfo);
	}

	private LoginResponseDto handleNewUser(SocialAccountInfo socialAccountInfo) {
		String registrationKey = saveTemporarySocialAccountInfo(socialAccountInfo);
		String registrationUrl = generateRegistrationUrl(registrationKey);

		return new LoginResponseDto(registrationUrl);
	}

	private LoginResponseDto handleExistingUser(User user, SocialAccountInfo socialAccountInfo) {
		Optional<SocialAccount> optionalSocialAccount = socialAccountService.findBySocialTypeAndSocialId(
			SocialType.KAKAO, socialAccountInfo.getSocialId());

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

	private String saveTemporarySocialAccountInfo(SocialAccountInfo socialAccountInfo) {
		String registrationKey = UUID.randomUUID().toString();
		redisTemplate.opsForValue().set(registrationKey, socialAccountInfo, REDIS_CACHE_TTL, TimeUnit.MINUTES);
		return registrationKey;
	}

	private String generateRegistrationUrl(String registrationKey) {
		return "http://localhost:3000/signup?key=" + registrationKey;
	}

	private void handleUserStatusError(UserStatus userStatus) {
		switch (userStatus) {
			case WITHDRAWAL ->
				throw new UserStatusNotActiveException(GlobalMessage.USER_STATUS_WITHDRAWAL.getMessage());
			case SUSPENSION -> throw new UserStatusNotActiveException(GlobalMessage.USER_STATUS_STOP.getMessage());
			case ACTIVE -> throw new UserEmailDuplicateException(GlobalMessage.EMAIL_ALREADY_USED.getMessage());
		}
	}
}
