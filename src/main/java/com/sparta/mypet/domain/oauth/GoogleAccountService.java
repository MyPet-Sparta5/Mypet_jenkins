package com.sparta.mypet.domain.oauth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.domain.auth.UserService;
import com.sparta.mypet.domain.feign.GoogleAuthApi;
import com.sparta.mypet.domain.feign.GoogleUserApi;
import com.sparta.mypet.domain.oauth.dto.TokenResponseDto;
import com.sparta.mypet.domain.oauth.entity.SocialAccount;
import com.sparta.mypet.domain.oauth.entity.SocialAccountInfo;
import com.sparta.mypet.domain.oauth.entity.SocialType;
import com.sparta.mypet.security.JwtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Google Account Service")
@Service
public class GoogleAccountService extends OAuthService {

	@Value("${google.client-id.key}")
	private String clientId;
	@Value("${google.client-secret.key}")
	private String clientSecret;
	@Value("${google.redirect.login.uri}")
	private String redirectLoginUri;
	@Value("${google.redirect.link.uri}")
	private String redirectLinkUri;

	private final GoogleAuthApi googleAuthApi;
	private final GoogleUserApi googleUserApi;

	@Autowired
	public GoogleAccountService(ObjectMapper objectMapper, SocialAccountService socialAccountService,
		RedisTemplate<String, Object> redisTemplate, UserService userService, JwtService jwtService,
		GoogleAuthApi googleAuthApi, GoogleUserApi googleUserApi) {
		super(redisTemplate, socialAccountService, objectMapper, userService, jwtService);
		this.googleAuthApi = googleAuthApi;
		this.googleUserApi = googleUserApi;
	}

	@Override
	public SocialType getSocialType() {
		return SocialType.GOOGLE;
	}

	@Override
	protected String getRedirectLinkUrl() {
		return redirectLinkUri;
	}

	@Override
	protected String getRedirectLoginUrl() {
		return redirectLoginUri;
	}

	/**
	 * 구글 서버에 토큰 요청
	 * @param authorizationCode 인증 인가 코드
	 * @return 토큰 정보 반환
	 */
	protected TokenResponseDto getAccessToken(String authorizationCode, String redirectUri) {
		ResponseEntity<String> response = googleAuthApi.getAccessToken(clientId, clientSecret, grantType, redirectUri,
			authorizationCode);

		if (response.getStatusCode().is2xxSuccessful()) {
			try {
				return objectMapper.readValue(response.getBody(), TokenResponseDto.class);
			} catch (Exception e) {
				throw new IllegalArgumentException(GlobalMessage.JSON_PARSING_ERROR.getMessage());
			}
		} else {
			throw new IllegalArgumentException(
				GlobalMessage.KAKAO_SERVER_ERROR.getMessage() + response.getStatusCode());
		}
	}

	/**
	 * 구글 유저 정보 요청
	 * @param responseDto Token 정보
	 * @return 소셜 계정 정보 반환
	 */
	@Override
	protected SocialAccountInfo getUserInfo(TokenResponseDto responseDto) {
		Map<String, String> header = new HashMap<>();
		header.put(JwtService.HEADER, JwtService.TOKEN_PREFIX + responseDto.getAccessToken());

		ResponseEntity<String> response = googleUserApi.getUserInfo(header);

		if (response.getStatusCode().is2xxSuccessful()) {
			try {
				JsonNode jsonNode = objectMapper.readTree(response.getBody());

				String socialId = jsonNode.get("sub").asText(); // 'sub' : unique 한 id값
				String email = jsonNode.get("email").asText();
				String name = jsonNode.get("name").asText();

				return SocialAccountInfo.builder()
					.socialId(socialId)
					.socialType(SocialType.GOOGLE)
					.email(email)
					.nickname(name)
					.accessToken(responseDto.getAccessToken())
					.refreshToken(responseDto.getRefreshToken())
					.build();
			} catch (JsonProcessingException e) {
				throw new IllegalArgumentException(
					GlobalMessage.JSON_PARSING_ERROR.getMessage() + " : " + e.getMessage());
			}
		} else {
			throw new IllegalArgumentException(
				GlobalMessage.GOOGLE_SERVER_ERROR.getMessage() + response.getStatusCode());
		}
	}

	@Override
	protected void revokeSocialAccess(SocialAccount socialAccount) {
		String accessToken = socialAccount.getAccessToken();
		String refreshToken = socialAccount.getRefreshToken();

		try {
			ResponseEntity<String> tokenInfoResponse = googleAuthApi.getTokenInfo(accessToken);

			if (!tokenInfoResponse.getStatusCode().is2xxSuccessful()) {
				// 액세스 토큰이 만료되었거나 유효하지 않은 경우, 리프레시 토큰으로 갱신 시도
				TokenResponseDto newTokens = refreshAccessToken(refreshToken);
				accessToken = newTokens.getAccessToken();

				// 새로운 토큰으로 SocialAccount 업데이트
				socialAccount.updateAccessToken(accessToken);
				socialAccount.updateRefreshToken(newTokens.getRefreshToken());
			}

			ResponseEntity<String> response = googleAuthApi.revokeToken(accessToken);

			if (response.getStatusCode().is2xxSuccessful()) {
				log.info("Google 계정 연결 해제 성공");
			} else {
				throw new IllegalArgumentException("Google 계정 연결 해제에 실패했습니다.");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Google 연결 해제 중 오류가 발생했습니다. " + e.getMessage());
		}
	}

	private TokenResponseDto refreshAccessToken(String refreshToken) {
		ResponseEntity<String> response = googleAuthApi.refreshAccessToken(refreshToken, clientId, clientSecret,
			grantType);

		if (response.getStatusCode().is2xxSuccessful()) {
			try {
				return objectMapper.readValue(response.getBody(), TokenResponseDto.class);
			} catch (Exception e) {
				throw new IllegalArgumentException(GlobalMessage.JSON_PARSING_ERROR.getMessage());
			}
		} else {
			throw new IllegalArgumentException("Failed to refresh access token: " + response.getStatusCode());
		}
	}

}
