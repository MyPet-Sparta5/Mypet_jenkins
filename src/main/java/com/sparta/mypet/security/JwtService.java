package com.sparta.mypet.security;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sparta.mypet.security.util.JwtProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtService")
@Service
public class JwtService {

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER = "Authorization";
	public static final String AUTHORIZATION_KEY = "auth";
	public static final String REFRESH_TOKEN_COOKIE_NAME = "RefreshToken";
	public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Value("${access.token.expiration}")
	private Long accessTokenExpiration; // access token 만료 시간

	@Value("${refresh.token.expiration}")
	private Long refreshTokenExpiration; // refresh token 만료 시간

	/**
	 * JWT 토큰을 생성합니다.
	 * @param tokenType 토큰의 종류 (access, refresh)
	 * @param role 유저의 역할 ("ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER")
	 * @param username 유저의 식별자 (여기서는 Email)
	 * @return 생성된 JWT 토큰
	 */
	public String generateToken(TokenType tokenType, Object role, String username) {
		long expiration = (tokenType.equals(TokenType.ACCESS)) ? accessTokenExpiration : refreshTokenExpiration;

		return JwtProvider.generateToken(AUTHORIZATION_KEY, role, username, expiration,
			JwtProvider.getSecretKey(secretKey), SIGNATURE_ALGORITHM);
	}

	/**
	 * HTTP 응답 헤더에 Access 토큰을 저장합니다.
	 * @param accessToken 설정할 Access 토큰
	 */
	public void setHeaderWithAccessToken(String accessToken) {
		HttpServletResponse response = ((ServletRequestAttributes)Objects.requireNonNull(
			RequestContextHolder.getRequestAttributes())).getResponse();

		if (response != null) {
			response.setHeader(HEADER, TOKEN_PREFIX + accessToken);
		}
	}

	/**
	 * Refresh 토큰을 쿠키에 저장합니다.
	 * @param refreshToken 저장할 Refresh 토큰
	 */
	public void setRefreshTokenAtCookie(String refreshToken) {
		Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge((int)(refreshTokenExpiration / 1000));

		HttpServletResponse response = ((ServletRequestAttributes)Objects.requireNonNull(
			RequestContextHolder.getRequestAttributes())).getResponse();

		if (response != null) {
			response.addCookie(cookie);
		}
	}

	/**
	 *  헤더에 토큰이 있는지 확인합니다.
	 * @param request HttpServletRequest
	 * @return Authorization Header 존재 여부
	 */
	public Boolean isAuthorizationHeaderNotFound(HttpServletRequest request) {
		return request.getHeader(HEADER) == null;
	}

	/**
	 * Http 요청에서 Access Token 추출합니다.
	 * @param request Http 요청
	 * @return 추출된 Access Token
	 */
	public String getAccessTokenFromRequest(HttpServletRequest request) {

		return request.getHeader(HEADER);
	}

	/**
	 * "Authorization" 헤더 값 Token Substring
	 * @param tokenValue Authorization 헤더의 토큰 값
	 * @return "Bearer " 문구 자른 순수한 Token 값
	 */
	public String substringAccessToken(String tokenValue) {

		if (tokenValue.startsWith(TOKEN_PREFIX)) {
			return tokenValue.substring(7);
		}
		return null;
	}

	/**
	 * HTTP 요청의 쿠키에서 Refresh 토큰을 추출합니다.
	 * @param request HTTP 요청
	 * @return 추출된 Refresh 토큰
	 */
	public String getRefreshTokenFromRequest(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * JWT 토큰의 유효성을 검증합니다.
	 * @param token JWT 토큰
	 * @return 토큰 유효성 여부
	 */
	public Boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(JwtProvider.getSecretKey(secretKey)).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.error("Invalid JWT signature.");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token.");
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty.");
		}
		return false;
	}

	/**
	 * JWT 토큰에서 이메일을 추출합니다.
	 * @param token JWT 토큰
	 * @return 추출된 이메일
	 */
	public String extractEmail(String token) {
		return JwtProvider.extractAllClaims(token, JwtProvider.getSecretKey(secretKey)).getSubject();
	}

	/**
	 * JWT 토큰에서 권한을 추출합니다.
	 * @param token JWT 토큰
	 * @return 추출된 User Role
	 */
	public Object extractRole(String token) {
		return JwtProvider.extractAllClaims(token, JwtProvider.getSecretKey(secretKey)).get(AUTHORIZATION_KEY);
	}

	/**
	 * Refresh 토큰 쿠키를 삭제합니다.
	 */
	public void deleteRefreshTokenAtCookie() {
		// 덮어 쓰기
		Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setPath("/");
		cookie.setMaxAge(0); // 쿠키 시간 초기화
		HttpServletResponse response = ((ServletRequestAttributes)Objects.requireNonNull(
			RequestContextHolder.getRequestAttributes())).getResponse();

		if (response != null) {
			response.addCookie(cookie);
		}
	}
}
