package ru.dgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dgi.model.Question;
import java.util.List;

@Transactional(readOnly = true)
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Question q WHERE q.id=:id AND q.quest.id=:questId")
    int delete(@Param("id") int id, @Param("questId") int questId);

    @Query("SELECT q FROM Question q WHERE q.quest.id=:questId ORDER BY q.number ASC")
    List<Question> getAll(@Param("questId") int questId);

    @Query("SELECT q FROM Question q, Quest qst WHERE qst.id = q.quest.id " +
            "AND qst.id=:questId AND qst.isActive = TRUE ORDER BY q.number ASC")
    List<Question> getAllActive(@Param("questId") int questId);

    @Query("SELECT q FROM Question q, Quest qst WHERE qst.id = q.quest.id " +
            "AND q.id=:id AND qst.id=:questId AND qst.isActive = TRUE ORDER BY q.number ASC")
    Question getActive(@Param("id") int id, @Param("questId") int questId);
}
