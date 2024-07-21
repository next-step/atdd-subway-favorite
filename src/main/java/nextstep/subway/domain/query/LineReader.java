package nextstep.subway.domain.query;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.domain.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import nextstep.subway.domain.entity.line.Line;
import nextstep.subway.domain.entity.station.Station;
import nextstep.subway.domain.view.LineView;
import nextstep.subway.domain.view.StationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineReader {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional(readOnly = true)
    public LineView.Main getOneById(Long id) {
        Line line = lineRepository.findByIdOrThrow(id);
        Map<Long, Station> stationMap = getStationMapByIds(line.getSections().getAllStationIds());
        return joinAndTransform(line, stationMap);
    }


    @Transactional(readOnly = true)
    public List<LineView.Main> getAllLines() {
        List<Line> lines = lineRepository.findAll();
        List<Long> stationIds = lines.stream()
                .flatMap(line -> line.getSections().getAllStationIds().stream())
                .collect(Collectors.toList());
        Map<Long, Station> stationMap = getStationMapByIds(stationIds);
        return lines.stream()
                .map(line -> joinAndTransform(line, stationMap))
                .collect(Collectors.toList());
    }

    private Map<Long, Station> getStationMapByIds(Iterable<Long> ids) {
        Map<Long, Station> stationMap = new HashMap<>();
        stationRepository.findAllById(ids).forEach((station -> stationMap.putIfAbsent(station.getId(), station)));
        return stationMap;
    }

    private LineView.Main joinAndTransform(Line line, Map<Long, Station> stationMap) {
        List<StationView.Main> allStations = line.getSections().getAllStationIds().stream()
                .map(stationMap::get)
                .map(station -> new StationView.Main(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new LineView.Main(line.getId(), line.getName(), line.getColor(), allStations);
    }
}
