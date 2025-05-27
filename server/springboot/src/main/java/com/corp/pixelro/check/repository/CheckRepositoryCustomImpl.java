package com.corp.pixelro.check.repository;

import com.corp.pixelro.check.dto.CheckSummary;
import com.corp.pixelro.check.repository.projection.RecentExaminationRawDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CheckRepositoryCustomImpl implements CheckRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public LocalDateTime findLatestCheckDateByUserId(Long userId) {
        String sql = """
                SELECT MAX(latest) FROM (
                                SELECT MAX(created_at) as latest FROM amsler_checks WHERE user_id = :userId AND deleted = false
                                UNION ALL
                                SELECT MAX(created_at) FROM mchart_checks WHERE user_id = :userId AND deleted = false
                                UNION ALL
                                SELECT MAX(created_at) FROM presbyopia_checks WHERE user_id = :userId AND deleted = false
                                UNION ALL
                                SELECT MAX(created_at) FROM sight_checks WHERE user_id = :userId AND deleted = false
                            ) t
                """;
        Object result = em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .getSingleResult();

        if (result == null) return null;
        if (result instanceof java.sql.Timestamp ts) {
            return ts.toLocalDateTime();
        }

        return null;
    }

    @Override
    public List<RecentExaminationRawDto> findLatestExaminations(Long userId) {
        String sql = """
                SELECT * FROM (
                    (SELECT 'sight' AS type, id, NULL AS status, created_at, NULL AS ai_result,
                        left_sight, right_sight,
                        left_sight_prediction, right_sight_prediction,
                        NULL AS left_macular_loc, NULL AS right_macular_loc,
                        NULL AS left_eye_ver, NULL AS right_eye_ver,
                        NULL AS left_eye_hor, NULL AS right_eye_hor,
                        NULL AS age, NULL AS age_prediction
                    FROM sight_checks
                    WHERE user_id = :userId AND deleted = false
                    ORDER BY created_at DESC LIMIT 1)
                
                    UNION ALL
                
                    (SELECT 'amsler' AS type, id, NULL AS status, created_at, NULL AS ai_result,
                        NULL, NULL,
                        NULL, NULL,
                        left_macular_loc, right_macular_loc,
                        NULL, NULL,
                        NULL, NULL,
                        NULL, NULL
                    FROM amsler_checks
                    WHERE user_id = :userId AND deleted = false
                    ORDER BY created_at DESC LIMIT 1)
                
                    UNION ALL
                
                    (SELECT 'mchart' AS type, id, NULL AS status, created_at, NULL AS ai_result,
                        NULL, NULL,
                        NULL, NULL,
                        NULL, NULL,
                        left_eye_ver, right_eye_ver,
                        left_eye_hor, right_eye_hor,
                        NULL, NULL
                    FROM mchart_checks
                    WHERE user_id = :userId AND deleted = false
                    ORDER BY created_at DESC LIMIT 1)
                
                    UNION ALL
                
                    (SELECT 'presbyopia' AS type, id, NULL AS status, created_at, NULL AS ai_result,
                        NULL, NULL,
                        NULL, NULL,
                        NULL, NULL,
                        NULL, NULL,
                        NULL, NULL,
                        age, age_prediction
                    FROM presbyopia_checks
                    WHERE user_id = :userId AND deleted = false
                    ORDER BY created_at DESC LIMIT 1)
                ) AS latest_checks
                """;

        List<Object[]> resultList = em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .getResultList();

        return resultList.stream()
                .map(row -> new RecentExaminationRawDto(
                        (String) row[0],
                        ((Number) row[1]).longValue(),
                        (String) row[2],
                        ((java.sql.Timestamp) row[3]).toLocalDateTime(),
                        (String) row[4],
                        row[5] != null ? ((Number) row[5]).intValue() : null,
                        row[6] != null ? ((Number) row[6]).intValue() : null,
                        row[7] != null ? ((Number) row[7]).intValue() : null,
                        row[8] != null ? ((Number) row[8]).intValue() : null,
                        (String) row[9],
                        (String) row[10],
                        row[11] != null ? ((Number) row[11]).intValue() : null,
                        row[12] != null ? ((Number) row[12]).intValue() : null,
                        row[13] != null ? ((Number) row[13]).intValue() : null,
                        row[14] != null ? ((Number) row[14]).intValue() : null,
                        row[15] != null ? ((Number) row[15]).intValue() : null,
                        row[16] != null ? ((Number) row[16]).intValue() : null
                ))
                .toList();
    }

    @Override
    public List<CheckSummary> findTotalChecks(Long userId, int page, int size) {
        String sql = """
                SELECT
                    merged.created_at,
                    MAX(merged.has_sight) AS has_sight,
                    MAX(merged.has_presbyopia) AS has_presbyopia,
                    MAX(merged.has_amsler) AS has_amsler,
                    MAX(merged.has_mchart) AS has_mchart
                FROM (
                    SELECT created_at, true AS has_sight, false AS has_presbyopia, false AS has_amsler, false AS has_mchart
                    FROM sight_checks
                    WHERE user_id = :userId AND created_at IS NOT NULL and deleted is false
                                   
                    UNION ALL
        
                    SELECT created_at, false, true, false, false
                    FROM presbyopia_checks
                    WHERE user_id = :userId AND created_at IS NOT NULL and deleted is false
        
                    UNION ALL
        
                    SELECT created_at, false, false, true, false
                    FROM amsler_checks
                    WHERE user_id = :userId AND created_at IS NOT NULL and deleted is false
        
                    UNION ALL
        
                    SELECT created_at, false, false, false, true
                    FROM mchart_checks
                    WHERE user_id = :userId AND created_at IS NOT NULL and deleted is false
                ) AS merged
                GROUP BY merged.created_at
                ORDER BY merged.created_at DESC
                LIMIT :size OFFSET :offset;
        """;

        List<Tuple> tuples = em.createNativeQuery(sql, Tuple.class)
                .setParameter("userId", userId)
                .setParameter("size", size)
                .setParameter("offset", page * size)
                .getResultList();

        // Tuple → DTO 매핑
        return tuples.stream()
                .map(tuple -> new CheckSummary(
                        ((Timestamp) tuple.get("created_at")).toLocalDateTime(),
                        ((Number) tuple.get("has_sight")).intValue() == 1,
                        ((Number) tuple.get("has_presbyopia")).intValue() == 1,
                        ((Number) tuple.get("has_amsler")).intValue() == 1,
                        ((Number) tuple.get("has_mchart")).intValue() == 1
                ))
                .toList();
    }


}
