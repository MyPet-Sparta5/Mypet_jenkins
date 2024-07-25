package com.sparta.mypet.domain.backoffice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.mypet.common.util.PaginationUtil;
import com.sparta.mypet.domain.auth.UserRepository;
import com.sparta.mypet.domain.auth.UserService;
import com.sparta.mypet.domain.auth.entity.User;
import com.sparta.mypet.domain.auth.entity.UserStatus;
import com.sparta.mypet.domain.backoffice.dto.UserListResponseDto;
import com.sparta.mypet.domain.backoffice.dto.UserStatusRequestDto;
import com.sparta.mypet.domain.backoffice.dto.UserStatusResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BackOfficeService {

	private final UserRepository userRepository;
	private final UserService userService;

	@Transactional(readOnly = true)
	public Page<UserListResponseDto> getUsers(int page, int pageSize, String sortBy) {
		Pageable pageable = PaginationUtil.createPageable(page, pageSize, sortBy);

		Page<User> userList = userRepository.findAll(pageable);

		return userList.map(UserListResponseDto::new);
	}

	@Transactional
	public UserStatusResponseDto updateUserStatus(UserStatusRequestDto requestDto, Long userId) {
		User updatedUser = userService.findUserById(userId);
		UserStatus userStatus = UserStatus.valueOf(requestDto.getStatus());

		updatedUser.updateUserStatus(userStatus);

		return new UserStatusResponseDto(updatedUser);
	}
}
