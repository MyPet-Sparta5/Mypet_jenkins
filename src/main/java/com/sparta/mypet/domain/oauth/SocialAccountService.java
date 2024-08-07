package com.sparta.mypet.domain.oauth;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.domain.oauth.dto.SocialAccountResponse;
import com.sparta.mypet.domain.oauth.entity.SocialAccount;
import com.sparta.mypet.domain.oauth.entity.SocialAccountInfo;
import com.sparta.mypet.domain.oauth.entity.SocialType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "Social")
@RequiredArgsConstructor
public class SocialAccountService {

	private final RedisTemplate<String, Object> redisTemplate;

	private final SocialAccountRepository socialAccountRepository;

	@Transactional(readOnly = true)
	public SocialAccountResponse getSocialAccountInfo(String key) {

		log.info("key = " + key);
		SocialAccountInfo socialAccountInfo = (SocialAccountInfo)redisTemplate.opsForValue().get(key);

		if (socialAccountInfo == null)
			throw new IllegalArgumentException(GlobalMessage.REDIS_VALUE_ERROR.getMessage());
		log.info(socialAccountInfo.getEmail());

		return SocialAccountResponse.builder()
			.email(socialAccountInfo.getEmail())
			.nickname(socialAccountInfo.getNickname())
			.build();
	}

	@Transactional
	public SocialAccount createAndSaveSocialAccount(SocialAccountInfo socialAccountInfo) {

		SocialAccount socialAccount = SocialAccount.builder()
			.socialId(socialAccountInfo.getSocialId())
			.socialType(socialAccountInfo.getSocialType())
			.email(socialAccountInfo.getEmail())
			.build();

		return socialAccountRepository.save(socialAccount);
	}

	@Transactional(readOnly = true)
	public Optional<SocialAccount> findBySocialTypeAndEmail(SocialType socialType, String email) {
		return socialAccountRepository.findBySocialTypeAndEmail(socialType, email);
	}

	public Optional<SocialAccount> findBySocialTypeAndSocialId(SocialType socialType, Long socialId) {
		return socialAccountRepository.findBySocialTypeAndSocialId(socialType, socialId);
	}

	@Transactional
	public void deleteSocialAccount(SocialAccount socialAccount) {
		socialAccountRepository.delete(socialAccount);
	}

	public boolean hasSocialAccount(String email) {
		return socialAccountRepository.existsSocialAccountByEmail(email);
	}

}
