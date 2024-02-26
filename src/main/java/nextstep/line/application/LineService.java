package nextstep.line.application;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.line.exception.LineNotFoundException;
import nextstep.line.persistance.LineRepository;
import nextstep.line.presentation.LineRequest;
import nextstep.line.presentation.LineResponse;
import nextstep.station.domain.Station;
import nextstep.station.exception.StationNotFoundException;
import nextstep.station.persistance.StationRepository;
import nextstep.station.presentation.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance()));

        List<StationResponse> stationResponses = line.getStations().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }

    private Station getStation(long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(Long.toString(stationId)));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineService::toLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return lineRepository.findById(id)
                .map(LineService::toLineResponse)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        lineRepository.findById(id)
                .ifPresent(line -> {
                    line.update(lineRequest.getName(), lineRequest.getColor());
                    lineRepository.save(line);
                });
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.findById(id)
                .ifPresent(lineRepository::delete);
    }

    @Transactional
    public void addSection(Line line, Section section) {
        line.addSection(section);
        lineRepository.save(line);
    }

    private static LineResponse toLineResponse(Line line) {
        List<StationResponse> stationResponses = line.getStations()
                .stream().map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }
}
