package com.test.reminder.repository;

import com.test.reminder.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsBySubId(String subId);

    UserEntity findBySubId(String subId);
}
