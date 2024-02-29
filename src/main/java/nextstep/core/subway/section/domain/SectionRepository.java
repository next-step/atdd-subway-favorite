package nextstep.core.subway.section.domain;

import nextstep.core.subway.line.domain.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("SELECT s FROM Section s WHERE s.line = :line")
    List<Section> findAllByLine(@Param("line") Line line);
}
