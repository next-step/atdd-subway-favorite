package nextstep.subway.repository;

import nextstep.subway.domain.Line;
import nextstep.exception.ApplicationException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {
    default Line getBy(Long id) {
        return findById(id)
                .orElseThrow(() -> new ApplicationException(
                        "노선이 존재하지 않습니다. id=" + id));
    }
}
