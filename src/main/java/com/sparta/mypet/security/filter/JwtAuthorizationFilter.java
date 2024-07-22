package com.sparta.mypet.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.mypet.security.JwtService;
import com.sparta.mypet.security.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "필터 : JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsServiceImpl userDetailsService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		try {
			String accessToken = jwtService.substringAccessToken(jwtService.getAccessTokenFromRequest(request));

			if (accessToken != null) {
				if (Boolean.TRUE.equals(jwtService.validateToken(accessToken))) {
					log.info("Access Token 유효");
					setAuthenticationContext(accessToken);
				} else {
					log.info("Invalid Token: {}", accessToken);
					sendTokenExpiredResponse(response);
					return;
				}
			}
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token", e);
			sendTokenExpiredResponse(response);
			return;
		} catch (SecurityException | MalformedJwtException e) {
			log.error("Invalid JWT token", e);
			sendInvalidTokenResponse(response);
			return;
		} catch (Exception e) {
			log.error("Unexpected error during JWT processing", e);
			sendAuthenticationFailureResponse(response);
			return;
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
		return jwtService.isAuthorizationHeaderNotFound(request);
	}

	private void setAuthenticationContext(String accessToken) {
		String username = jwtService.extractEmail(accessToken);

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, "",
			userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private void sendTokenExpiredResponse(HttpServletResponse response) throws IOException {
		sendErrorResponse(response, "The access token has expired");
	}

	private void sendInvalidTokenResponse(HttpServletResponse response) throws IOException {
		sendErrorResponse(response, "The access token is invalid");
	}

	private void sendAuthenticationFailureResponse(HttpServletResponse response) throws IOException {
		sendErrorResponse(response, "Authentication failed");
	}

	private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
		int status = HttpServletResponse.SC_UNAUTHORIZED;

		response.setStatus(status);
		response.setContentType("application/json");

		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("status", status);
		responseBody.put("message", message);

		response.getWriter().write(objectMapper.writeValueAsString(responseBody));
	}
}
