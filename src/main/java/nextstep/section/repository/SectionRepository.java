package nextstep.section.repository;

import nextstep.section.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByUpStationId(Long upStationId);

    Optional<Section> findByDownStationId(Long downStationId);
}

