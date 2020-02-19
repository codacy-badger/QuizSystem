package ru.dgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dgi.model.Quest;
import java.util.List;

@Transactional(readOnly = true)
public interface QuestRepository extends JpaRepository<Quest, Integer> {

    @Query("SELECT q " +
            "FROM Quest q LEFT JOIN Result r ON (q.id = r.quest.id AND r.username=:username) " +
            "WHERE q.isActive = TRUE OR r.id IS NOT NULL ORDER BY q.id")
    List<Quest> getAllActiveOrStarted(@Param("username") String username);

    @Query("SELECT q FROM Quest q WHERE q.isActive = TRUE AND q.id=:id")
    Quest getActive(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Quest q WHERE q.id=:id")
    int delete(@Param("id") int id);
}
