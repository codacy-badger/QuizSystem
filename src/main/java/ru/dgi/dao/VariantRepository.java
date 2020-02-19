package ru.dgi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dgi.model.Variant;
import ru.dgi.to.VariantStatistics;
import java.util.List;

@Transactional(readOnly = true)
public interface VariantRepository extends JpaRepository<Variant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Variant v WHERE v.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT DISTINCT v.name FROM Variant v WHERE lower(v.name) like CONCAT('%',lower(:query),'%')")
    List<String> getTagsByQuery(@Param("query")String query);

    Variant getByName(@Param("name") String name);

    @Query("SELECT new ru.dgi.to.VariantStatistics(v.name, COUNT(a)) FROM Variant v, Result r, Quest qst " +
            "JOIN v.questions q " +
            "LEFT JOIN Answer a ON (v.id = a.variant.id and q.id = a.question.id) " +
            "WHERE qst.id = q.quest.id AND qst.id = r.quest.id AND qst.id=:questId AND " +
            "q.id=:questionId AND r.id = a.result.id AND r.status = 2 GROUP BY v.name, q.id")
    List<VariantStatistics> getVariantStatistics(@Param("questionId") int questionId, @Param("questId") int questId);
}
