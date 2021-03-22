package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LineRepository {
    Boolean existsByName(String name);
    List<Line> findAll();
    Optional<Line> findById(Long id);
    Line save(Line entity);

    void deleteById(Long id);
}
