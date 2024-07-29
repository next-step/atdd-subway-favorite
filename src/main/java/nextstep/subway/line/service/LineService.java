package nextstep.subway.line.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.common.ErrorMessage;
import nextstep.subway.exception.NoLineExistException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.UpdateLineRequest;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineResponse saveLine(LineRequest lineRequest) {

        Station upStation = stationService.findById(lineRequest.getUpStationId());
        Station downStation = stationService.findById(lineRequest.getDownStationId());

        Section section = Section.firstSection(upStation, downStation, lineRequest.getDistance());
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), section);
        Line savedLine = lineRepository.save(line);

        return LineResponse.from(savedLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLines() {
        List<Line> lines = lineRepository.findAllWithSectionsAndStations();

        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponse(Long lineId) {
        Line line = findLineByIdWithSectionsAndStations(lineId);

        return LineResponse.from(line);
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long lineId) {
        return findLineByIdWithSectionsAndStations(lineId);
    }

    public void updateLine(Long lineId, UpdateLineRequest updateLineRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NoLineExistException(ErrorMessage.NO_LINE_EXIST));
        line.updateName(updateLineRequest.getName());
        line.updateColor(updateLineRequest.getColor());
    }

    public void deleteLine(Long lineId) {
        Line line = findLineByIdWithSectionsAndStations(lineId);
        lineRepository.delete(line);
    }

    public LineResponse addSection(long lineId, SectionRequest sectionRequest) {
        Line line = findLineByIdWithSectionsAndStations(lineId);

        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());

        Section section = Section.firstSection(upStation, downStation, sectionRequest.getDistance());
        line.addSection(section);

        return LineResponse.from(line);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineByIdWithSectionsAndStations(lineId);

        Station station = stationService.findById(stationId);

        line.deleteSection(station);
    }

    private Line findLineByIdWithSectionsAndStations(Long lineId) {
        return lineRepository.findByIdWithSectionsAndStations(lineId)
                .orElseThrow(() -> new NoLineExistException(ErrorMessage.NO_LINE_EXIST));
    }
}
