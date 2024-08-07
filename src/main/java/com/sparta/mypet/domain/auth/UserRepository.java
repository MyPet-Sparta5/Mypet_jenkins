package com.sparta.mypet.domain.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sparta.mypet.domain.auth.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	@Query(value = "SELECT u.* " +
		"FROM users u " +
		"LEFT OUTER JOIN (" +
		"    SELECT user_id, MAX(suspension_end_datetime) AS end_date " +
		"    FROM suspensions " +
		"    GROUP BY user_id" +
		") s ON u.user_id = s.user_id " +
		"WHERE u.status = 'SUSPENSION' " +
		"AND s.end_date < NOW()", nativeQuery = true)
	List<User> findExpiredSuspendedUsers();
}
