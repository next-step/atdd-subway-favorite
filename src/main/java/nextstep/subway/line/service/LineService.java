package nextstep.subway.line.service;

import nextstep.subway.line.LineRepository;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.station.Station;
import nextstep.subway.station.service.StationDataService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final LineDataService lineDataService;
    private final StationDataService stationDataService;

    public LineService(LineRepository lineRepository, LineDataService lineDataService, StationDataService stationDataService) {
        this.lineRepository = lineRepository;
        this.lineDataService = lineDataService;
        this.stationDataService = stationDataService;
    }

    public LineResponse saveLine(LineCreateRequest request) {
        Line line = new Line(request.getName(), request.getColor());

        Station upStation = stationDataService.findStation(request.getUpStationId());
        Station downStation = stationDataService.findStation(request.getDownStationId());

        line.generateSection(request.getDistance(), upStation, downStation);

        Line savedLine = lineRepository.save(line);

        return LineResponse.ofEntity(savedLine);
    }

    public List<LineResponse> findLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream().map(LineResponse::ofEntity).collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = lineDataService.findLine(id);

        return LineResponse.ofEntity(line);
    }

    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = lineDataService.findLine(id);

        line.updateLine(request.getName(), request.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
