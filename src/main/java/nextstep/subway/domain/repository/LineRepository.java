package nextstep.subway.domain.repository;

import lombok.NonNull;
import nextstep.subway.domain.exception.NotFoundLineException;
import org.springframework.data.jpa.repository.JpaRepository;
import nextstep.subway.domain.entity.line.Line;

public interface LineRepository extends JpaRepository<Line, Long> {
    @NonNull
    default Line findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundLineException(id));
    }
}
