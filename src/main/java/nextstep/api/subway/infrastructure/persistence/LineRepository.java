package nextstep.api.subway.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.api.subway.domain.model.entity.Line;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface LineRepository extends JpaRepository<Line, Long> {
}
