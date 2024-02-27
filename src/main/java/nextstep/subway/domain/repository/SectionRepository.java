package nextstep.subway.domain.repository;

import nextstep.subway.domain.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
