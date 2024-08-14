package com.sparta.mypet.domain.oauth;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.UserEmailDuplicateException;
import com.sparta.mypet.domain.auth.entity.User;
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

		Optional<SocialAccount> optionalSocialAccount = socialAccountRepository.findBySocialTypeAndSocialId(
			socialAccountInfo.getSocialType(), socialAccountInfo.getSocialId());

		if (optionalSocialAccount.isPresent()) {
			throw new UserEmailDuplicateException(GlobalMessage.SOCIAL_ALREADY_LINKED.getMessage());
		}

		SocialAccount socialAccount = SocialAccount.builder()
			.socialId(socialAccountInfo.getSocialId())
			.socialType(socialAccountInfo.getSocialType())
			.email(socialAccountInfo.getEmail())
			.build();

		socialAccount.updateAccessToken(socialAccountInfo.getAccessToken());
		socialAccount.updateRefreshToken(socialAccount.getRefreshToken());

		return socialAccountRepository.save(socialAccount);
	}

	@Transactional(readOnly = true)
	public Optional<SocialAccount> findBySocialTypeAndEmail(SocialType socialType, String email) {
		return socialAccountRepository.findBySocialTypeAndEmail(socialType, email);
	}

	@Transactional(readOnly = true)
	public Optional<SocialAccount> findBySocialTypeAndSocialId(SocialType socialType, String socialId) {
		return socialAccountRepository.findBySocialTypeAndSocialId(socialType, socialId);
	}

	@Transactional(readOnly = true)
	public Optional<SocialAccount> findBySocialTypeAndUser(SocialType socialType, User user) {
		return socialAccountRepository.findBySocialTypeAndUser(socialType, user);
	}

	@Transactional
	public void deleteSocialAccount(SocialAccount socialAccount) {
		// 엔티티 삭제
		socialAccountRepository.delete(socialAccount);
	}

	public boolean hasSocialAccount(String email) {
		return socialAccountRepository.existsSocialAccountByEmail(email);
	}

}
