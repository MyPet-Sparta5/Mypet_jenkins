package com.sparta.mypet.security.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import com.sparta.mypet.domain.auth.entity.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public final class JwtProvider {

	// 인스턴스화 방지 기본 생성자
	private JwtProvider() {
	}

	/**
	 * JWT 토큰 생성
	 * @param authorizationKey 역할 정보가 저장될 클레임 키
	 * @param role 사용자 역할
	 * @param username 사용자 이름(여기서는 Email)
	 * @param expiration 토큰 만료 시간 (밀리초)
	 * @param secretKey 토큰 서명을 위한 비밀 키
	 * @param signatureAlgorithm 토큰 서명 알고리즘
	 * @return 생성된 JWT 토큰
	 */
	public static String generateToken(String authorizationKey, UserRole role, String username, long expiration,
		SecretKey secretKey, SignatureAlgorithm signatureAlgorithm) {

		return Jwts.builder()
			.claim(authorizationKey, role)
			.setSubject(username)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(secretKey, signatureAlgorithm)
			.compact();
	}

	/**
	 * JWT 서명에 사용할 비밀 키를 생성합니다.
	 * @param secretKey 비밀 키
	 * @return 생성된 SecretKey 객체
	 */
	public static SecretKey getSecretKey(String secretKey) {
		return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
	}

	/**
	 * 토큰에서 모든 클레임 추출
	 * @param token     JWT 토큰
	 * @param secretKey 토큰 검증을 위한 비밀 키
	 * @return 추출된 모든 클레임
	 */
	public static Claims extractAllClaims(String token, SecretKey secretKey) {
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
	}
}
