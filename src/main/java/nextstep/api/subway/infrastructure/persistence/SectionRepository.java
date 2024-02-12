package nextstep.api.subway.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.api.subway.domain.model.entity.Section;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface SectionRepository extends JpaRepository<Section, Long> {
}
