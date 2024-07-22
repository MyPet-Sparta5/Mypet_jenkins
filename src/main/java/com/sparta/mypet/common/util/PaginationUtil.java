package com.sparta.mypet.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtil {

	private PaginationUtil() {
	}

	public static Pageable createPageable(int page, int size, String sortBy) {
		validatePaginationParams(page, size);
		Sort sort = createSort(sortBy);
		return PageRequest.of(page - 1, size, sort);
	}

	private static void validatePaginationParams(int page, int size) {
		if (page < 1) {
			throw new IllegalArgumentException("페이지 번호는 1보다 작을 수 없습니다.");
		}
		if (size < 1 || size > 100) {
			throw new IllegalArgumentException("페이지 크기는 1에서 100 사이여야 합니다.");
		}
	}

	private static Sort createSort(String sortBy) {
		String[] parts = sortBy.split(",");
		if (parts.length != 2) {
			throw new IllegalArgumentException("잘못된 정렬 파라미터입니다. 'field,direction' 형식을 사용하세요.");
		}
		String property = parts[0].trim();
		Sort.Direction direction = parts[1].trim().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		return Sort.by(direction, property);
	}

}
