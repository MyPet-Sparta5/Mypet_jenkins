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
import com.sparta.mypet.domain.feign.KakaoAuthApi;
import com.sparta.mypet.domain.feign.KakaoUserApi;
import com.sparta.mypet.domain.oauth.dto.TokenResponseDto;
import com.sparta.mypet.domain.oauth.entity.SocialAccount;
import com.sparta.mypet.domain.oauth.entity.SocialAccountInfo;
import com.sparta.mypet.domain.oauth.entity.SocialType;
import com.sparta.mypet.security.JwtService;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "Kakao Login Service")
@Service
public class KakaoAccountService extends OAuthService {

	@Value("${kakao.client-id.key}")
	private String clientId;
	@Value("${kakao.client-secret.key}")
	private String clientSecret;
	@Value("${kakao.redirect.login.uri}")
	private String redirectLoginUri;
	@Value("${kakao.redirect.link.uri}")
	private String redirectLinkUri;
	@Value("${kakao.admin.key}")
	private String adminKey;
	private static final String TARGET_ID_TYPE = "user_id";

	private final KakaoAuthApi kakaoAuthApi;
	private final KakaoUserApi kakaoUserApi;

	@Autowired
	public KakaoAccountService(ObjectMapper objectMapper, SocialAccountService socialAccountService,
		RedisTemplate<String, Object> redisTemplate, UserService userService, JwtService jwtService,
		KakaoAuthApi kakaoAuthApi, KakaoUserApi kakaoUserApi) {
		super(redisTemplate, socialAccountService, objectMapper, userService, jwtService);
		this.kakaoAuthApi = kakaoAuthApi;
		this.kakaoUserApi = kakaoUserApi;
	}

	@Override
	public SocialType getSocialType() {
		return SocialType.KAKAO;
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
	 * 카카오 서버에 토큰 요청
	 * @param authorizationCode 인증 인가 코드
	 * @return 토큰 정보 반환
	 */
	@Override
	protected TokenResponseDto getAccessToken(String authorizationCode, String redirectUri) {
		ResponseEntity<String> response = kakaoAuthApi.getAccessToken(clientId, clientSecret, grantType, redirectUri,
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
	 * 카카오 유저 정보 요청
	 * @param responseDto Token 정보
	 * @return 소셜 계정 정보 반환
	 */
	@Override
	protected SocialAccountInfo getUserInfo(TokenResponseDto responseDto) {
		Map<String, String> header = new HashMap<>();
		header.put(JwtService.HEADER, JwtService.TOKEN_PREFIX + responseDto.getAccessToken());

		// 소셜 계정 정보 요청
		ResponseEntity<String> response = kakaoUserApi.getUserInfo(header);

		if (response.getStatusCode().is2xxSuccessful()) {
			try {
				JsonNode jsonNode = objectMapper.readTree(response.getBody());

				String socialId = jsonNode.get("id").asText();
				String nickname = jsonNode.at("/properties/nickname").asText();
				String email = jsonNode.at("/kakao_account/email").asText();

				return SocialAccountInfo.builder()
					.socialId(socialId)
					.socialType(SocialType.KAKAO)
					.email(email)
					.nickname(nickname)
					.accessToken(responseDto.getAccessToken())
					.refreshToken(responseDto.getRefreshToken())
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

	@Override
	protected void revokeSocialAccess(SocialAccount socialAccount) {
		Map<String, String> header = new HashMap<>();
		header.put("Authorization", "KakaoAK " + adminKey);

		ResponseEntity<String> response = kakaoUserApi.unlinkUser(header, TARGET_ID_TYPE,
			socialAccount.getSocialId().toString());

		if (response.getStatusCode().is2xxSuccessful()) {
			log.info("카카오 계정 연결 해제 성공: {}", socialAccount.getSocialId());
		} else {
			throw new IllegalArgumentException("카카오 계정 연결 해제에 실패했습니다.");
		}
	}
}
