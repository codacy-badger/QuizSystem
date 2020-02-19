package ru.dgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dgi.model.Answer;
import ru.dgi.to.AnswerTo;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Answer a WHERE a.id=:id AND a.result.id=:resultId")
    int delete(@Param("id") int id, @Param("resultId") int resultId);

    @Query("SELECT a FROM Answer a WHERE a.result.id=:resultId ORDER BY a.id ASC")
    List<Answer> getAll(@Param("resultId") int resultId);

    @Query("SELECT new ru.dgi.to.AnswerTo(a.id, a.answerText) FROM Answer a " +
            "WHERE a.id = " +
            "(SELECT min(a1.id) FROM Answer a1, Question q " +
            " WHERE a1.question.id = q.id AND q.id=:questionId AND a1.id > :id)")
    AnswerTo getOneMoreThanId(@Param("id") int id, @Param("questionId") int questionId);
}
