package com.sparta.mypet.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Configuration
public class JwtConfig {

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Value("${access.token.expiration}")
	private Long accessTokenExpiration; // access token 만료 시간

	@Value("${refresh.token.expiration}")
	private Long refreshTokenExpiration; // refresh token 만료 시간

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER = "Authorization";
	public static final String AUTHORIZATION_KEY = "auth";
	public static final String REFRESH_TOKEN_COOKIE_NAME = "RefreshToken";
	public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

	public static String staticSecretKey;
	public static Long staticAccessTokenExpiration;
	public static Long staticRefreshTokenExpiration;
	public static int staticRefreshTokenExpirationSecond;

	@PostConstruct
	public void init() {
		staticSecretKey = secretKey;
		staticAccessTokenExpiration = accessTokenExpiration; // 30분
		staticRefreshTokenExpiration = refreshTokenExpiration; // 2주
		staticRefreshTokenExpirationSecond = (int)(refreshTokenExpiration / 1000);
	}
}
