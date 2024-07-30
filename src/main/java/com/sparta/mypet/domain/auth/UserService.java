package com.sparta.mypet.domain.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.entity.GlobalMessage;
import com.sparta.mypet.common.exception.custom.PasswordInvalidException;
import com.sparta.mypet.common.exception.custom.UserEmailDuplicateException;
import com.sparta.mypet.common.exception.custom.UserNotFoundException;
import com.sparta.mypet.domain.auth.dto.SignupRequestDto;
import com.sparta.mypet.domain.auth.dto.SignupResponseDto;
import com.sparta.mypet.domain.auth.dto.UserWithPostListResponseDto;
import com.sparta.mypet.domain.auth.dto.UserWithdrawResponseDto;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserRole;
import com.sparta.mypet.domain.auth.entity.UserStatus;
import com.sparta.mypet.domain.post.entity.Category;
import com.sparta.mypet.domain.post.entity.Post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EntityManager entityManager;

	@Transactional
	public SignupResponseDto signup(SignupRequestDto requestDto) {

		Optional<User> duplicateUser = userRepository.findByEmail(requestDto.getEmail());

		if (duplicateUser.isPresent()) {
			throw new UserEmailDuplicateException(GlobalMessage.USER_EMAIL_DUPLICATE);
		}

		if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
			throw new PasswordInvalidException(GlobalMessage.PASSWORD_INVALID);
		}

		String encodePassword = passwordEncoder.encode(requestDto.getPassword());

		User user = User.builder()
			.email(requestDto.getEmail())
			.password(encodePassword)
			.nickname(requestDto.getNickname())
			.penaltyCount(0)
			.role(UserRole.USER)
			.status(UserStatus.ACTIVE)
			.build();

		User saveUser = userRepository.save(user);

		return SignupResponseDto.builder().user(saveUser).build();
	}

	@Transactional(readOnly = true)
	public UserWithPostListResponseDto getUser(String email) {

		User user = findUserByEmail(email);

		List<Post> freedomList = getPostsByCategory(user, Category.FREEDOM);
		List<Post> boastList = getPostsByCategory(user, Category.BOAST);

		return UserWithPostListResponseDto.builder().user(user).freedomList(freedomList).boastList(boastList).build();
	}

	@Transactional(readOnly = true)
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	// 유저를 탈퇴 처리 후, 로그아웃 API 호출을 통해 token 초기화!
	@Transactional
	public UserWithdrawResponseDto withdrawUser(String email) {

		User user = findUserByEmail(email);

		user.updateUserStatus(UserStatus.WITHDRAWAL);

		return UserWithdrawResponseDto.builder().user(user).build();
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(GlobalMessage.USER_EMAIL_NOT_FOUND.getMessage()));
	}

	public User findUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(GlobalMessage.USER_NOT_FOUND.getMessage()));
	}

	private List<Post> getPostsByCategory(User user, Category category) {
		String jpql = "SELECT p FROM Post p " + "WHERE p.user = :user " + "AND p.category = :category "
			+ "ORDER BY p.createdAt DESC";

		TypedQuery<Post> query = entityManager.createQuery(jpql, Post.class);
		query.setParameter("user", user);
		query.setParameter("category", category);
		query.setMaxResults(5);

		return query.getResultList();
	}
}
