package nextstep.subway.infra;

import org.springframework.data.jpa.repository.JpaRepository;

import nextstep.subway.domain.model.Line;
import nextstep.subway.domain.repository.LineRepository;

public interface JpaLineRepository extends LineRepository, JpaRepository<Line, Long> {
}
