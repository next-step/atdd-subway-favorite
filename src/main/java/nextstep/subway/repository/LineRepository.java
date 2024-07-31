package nextstep.subway.repository;

import nextstep.subway.entity.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {
    @Override
    @Query("SELECT DISTINCT l FROM Line l " +
            "LEFT JOIN FETCH l.sections s " +
            "LEFT JOIN FETCH s.sectionList ss " +
            "LEFT JOIN FETCH ss.upStation " +
            "LEFT JOIN FETCH ss.downStation")
    List<Line> findAll();

    @Override
    @Query("SELECT DISTINCT l FROM Line l " +
            "LEFT JOIN FETCH l.sections s " +
            "LEFT JOIN FETCH s.sectionList ss " +
            "LEFT JOIN FETCH ss.upStation " +
            "LEFT JOIN FETCH ss.downStation " +
            "WHERE l.id = :id")
    Optional<Line> findById(@Param("id") Long id);
}
