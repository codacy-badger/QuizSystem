package ru.dgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dgi.model.Result;
import java.util.List;

@Transactional(readOnly = true)
public interface ResultRepository extends JpaRepository<Result, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Result r WHERE r.id=:id AND r.quest.id=:questId")
    int delete(@Param("id") int id, @Param("questId") int questId);

    @Query("SELECT r FROM Result r WHERE r.quest.id=:questId ORDER BY r.username, r.id ASC")
    List<Result> getAll(@Param("questId") int questId);

    @Query("SELECT COUNT(r) FROM Result r WHERE r.quest.id=:questId")
    long getCount(@Param("questId") int questId);

    @Query("SELECT r FROM Result r WHERE r.quest.id=:questId AND r.username=:username ORDER BY r.username, r.id ASC")
    Result getResultByQuestIdAndUsername(@Param("questId") int questId, @Param("username") String username);
}
