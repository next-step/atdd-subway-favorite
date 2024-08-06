package nextstep.line.application;

import nextstep.exceptions.ErrorMessage;
import nextstep.line.domain.Line;
import nextstep.line.exception.LineNotFoundException;
import nextstep.line.payload.LineResponse;
import nextstep.line.repository.LineRepository;
import nextstep.station.domain.Station;
import nextstep.station.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class LineQueryService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineQueryService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        Set<Long> stationsIds = lines.stream()
                .flatMap(line -> line.getStationIds().stream())
                .collect(Collectors.toSet());

        return lines.stream()
                .map(line ->
                        LineResponse.from(line, getSortedStations(stationsIds))
                ).collect(Collectors.toList());
    }

    public LineResponse getLine(final Long id) {
        Line line = getById(id);
        return LineResponse.from(line, getSortedStations(line.getStationIds()));
    }

    private Map<Long, Station> getStationMap(final Collection<Long> stationsIds) {
        return stationRepository.findByIdIn(stationsIds)
                .stream()
                .collect(Collectors.toMap(Station::getId, (station -> station)));
    }

    private List<Station> getSortedStations(final Collection<Long> stationsIds) {
        Map<Long, Station> stationMap = getStationMap(stationsIds);
        return stationsIds.stream()
                .map(stationMap::get)
                .collect(Collectors.toList());
    }

    private Line getById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException(ErrorMessage.LINE_NOT_FOUND));
    }

}
