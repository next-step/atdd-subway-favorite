package nextstep.line.application;

import nextstep.exception.LineNotFoundException;
import nextstep.exception.StationNotFoundException;
import nextstep.line.application.request.LineCreateRequest;
import nextstep.line.application.request.LineModifyRequest;
import nextstep.line.application.request.SectionAddRequest;
import nextstep.line.application.response.LineResponse;
import nextstep.line.application.response.ShortPathResponse;
import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.line.domain.PathFinder;
import nextstep.line.domain.ShortPath;
import nextstep.station.application.StationService;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {

        Line line = new Line(lineCreateRequest.getName(),
                lineCreateRequest.getColor(),
                findStation(lineCreateRequest.getUpStationId()),
                findStation(lineCreateRequest.getDownStationId()),
                lineCreateRequest.getDistance()
        );

        lineRepository.save(line);
        return LineResponse.of(line);
    }

    @Transactional
    public void modifyLine(Long id, LineModifyRequest lineModifyRequest) {
        lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new)
                .modify(lineModifyRequest.getName(), lineModifyRequest.getColor());
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::of)
                .orElseThrow(LineNotFoundException::new);
    }

    public ShortPathResponse findShortPath(Long startStationId, Long endStationId) {
        Station startStation = findStation(startStationId);
        Station endStation = findStation(endStationId);

        PathFinder pathFinder = new PathFinder(lineRepository.findAll());
        ShortPath shortPath = pathFinder.findShortPath(startStation, endStation);
        return ShortPathResponse.of(shortPath);
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long lineId, SectionAddRequest sectionAddRequest) {
        Station upStation = findStation(sectionAddRequest.getUpStationId());
        Station downStation = findStation(sectionAddRequest.getDownStationId());

        lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new)
                .addSection(upStation, downStation, sectionAddRequest.getDistance());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Station station = findStation(stationId);

        lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new)
                .removeSection(station);
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(StationNotFoundException::new);
    }

}
