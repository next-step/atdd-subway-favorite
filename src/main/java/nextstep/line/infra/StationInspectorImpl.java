package nextstep.line.infra;

import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationInspector;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StationInspectorImpl implements StationInspector {
    private final LineRepository lineRepository;

    public StationInspectorImpl(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public boolean belongsToLine(Station station) {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .anyMatch(line -> line.contains(station));
    }
}
