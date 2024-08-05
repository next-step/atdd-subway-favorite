package nextstep.subway.line.domain;

import nextstep.subway.line.domain.entity.Line;
import nextstep.subway.line.exception.LineNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {

    default Line findByIdOrThrow(Long id) {
        return findById(id)
            .orElseThrow(() -> new LineNotFoundException(id));
    }
}
