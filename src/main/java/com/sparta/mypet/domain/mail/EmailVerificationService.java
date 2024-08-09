package com.sparta.mypet.domain.mail;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

	private static final int EMAIL_RETRY_TIME = 600; // 같은 이메일인 경우 10분 뒤에 다시 요청 가능
	private static final int EMAIL_CACHE_TTL = 600 + 60; // 10 + 1분 동안 이메일 인증 살아있음 (캐시 만료는 좀 더 여유있게)

	private static final int RANDOM_CODE_RANGE = 1000000;

	private static final String COOL_DOWN_PREFIX = "cooldown-";
	private static final String VERIFICATION_CODE_PREFIX = "verificationCode-";

	private final RedisTemplate<String, Object> redisTemplate;

	private final EmailService emailService;

	public boolean sendVerificationEmail(String email) {
		String cooldownKey = COOL_DOWN_PREFIX + email;
		String verificationKey = VERIFICATION_CODE_PREFIX + email;

		if (Boolean.TRUE.equals(redisTemplate.hasKey(cooldownKey))) {
			return false;
		}

		String verificationCode = createVerificationCode();

		redisTemplate.opsForValue().set(verificationKey, verificationCode, EMAIL_CACHE_TTL, TimeUnit.SECONDS);
		redisTemplate.opsForValue().set(cooldownKey, "cooling", EMAIL_RETRY_TIME, TimeUnit.SECONDS);

		emailService.sendEmail(email, verificationCode);

		return true;
	}

	public boolean verifyCode(String email, String code) {
		String cooldownKey = COOL_DOWN_PREFIX + email;
		String verificationKey = VERIFICATION_CODE_PREFIX + email;
		String storedCode = (String)redisTemplate.opsForValue().get(verificationKey);
		if (storedCode != null && storedCode.equals(code)) {
			redisTemplate.delete(cooldownKey);
			redisTemplate.delete(verificationKey);
			return true;
		}
		return false;
	}

	private String createVerificationCode() {
		return String.format("%06d", ThreadLocalRandom.current().nextInt(RANDOM_CODE_RANGE));
	}
}
