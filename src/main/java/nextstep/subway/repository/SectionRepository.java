package nextstep.subway.repository;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByLineIn(List<Line> lines);
    List<Section> findByLine(Line line);
    @Modifying
    void deleteByLine(Line line);
}
