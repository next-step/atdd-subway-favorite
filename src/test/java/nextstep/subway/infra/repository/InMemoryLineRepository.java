package nextstep.subway.domain.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.test.util.ReflectionTestUtils;

import nextstep.subway.domain.model.Line;

public class InMemoryLineRepository implements LineRepository {
    private static final String ID = "id";
    private Map<Long, Line> lines = new HashMap<>();
    private AtomicLong idGenerator = new AtomicLong();

    @Override
    public Optional<Line> findById(Long id) {
        return Optional.ofNullable(lines.get(id));
    }

    @Override
    public List<Line> findAll() {
        return new ArrayList<>(lines.values());
    }

    @Override
    public Line save(Line line) {
        if (line.getId() == null) {
            ReflectionTestUtils.setField(line, ID, idGenerator.incrementAndGet());
        }

        lines.put(line.getId(), line);
        return line;
    }

    @Override
    public void deleteById(Long id) {
        lines.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return lines.containsKey(id);
    }
}
