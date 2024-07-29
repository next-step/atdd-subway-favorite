package nextstep.subway.domain.repository;

import java.util.List;
import java.util.Optional;

import nextstep.subway.domain.model.Line;

public interface LineRepository {
    Optional<Line> findById(Long id);

    List<Line> findAll();

    Line save(Line line);

    void deleteById(Long id);

    boolean existsById(Long id);
}
