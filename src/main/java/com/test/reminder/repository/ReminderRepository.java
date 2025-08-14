package com.test.reminder.repository;

import com.test.reminder.domain.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReminderRepository extends JpaRepository<ReminderEntity, Long>, ReminderCustomRepository {

    @Query(
            value = """
            SELECT *
            FROM reminder
            WHERE remind < NOW()
            ORDER BY remind
            LIMIT :count
            FOR UPDATE SKIP LOCKED
            """,
            nativeQuery = true
    )
    List<ReminderEntity> findRemindsBeforeNowForUpdateSkipLocked(int count);
}
