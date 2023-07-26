package subway.line.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.constant.SubwayMessage;
import subway.exception.SubwayNotFoundException;
import subway.line.application.dto.LineCreateRequest;
import subway.line.application.dto.LineModifyRequest;
import subway.line.application.dto.LineRetrieveResponse;
import subway.line.application.dto.SectionCreateRequest;
import subway.line.application.dto.SectionDeleteRequest;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.Section;
import subway.station.application.StationService;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {


    private final LineRepository lineRepository;
    private final StationService stationService;
    private final SectionService sectionService;

    @Transactional
    public Line saveLine(LineCreateRequest createRequest,
                         Station upStation,
                         Station downStation) {
        Line request = LineCreateRequest.to(createRequest, upStation, downStation);
        return lineRepository.save(request);
    }

    @Transactional
    public Line saveLine(Line line) {
        return lineRepository.save(line);
    }

    @Transactional
    public void updateLine(Long id, LineModifyRequest request) {
        Line line = this.findLineById(id);
        line.updateLine(request.getName(), request.getColor());
    }

    public List<LineRetrieveResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(LineRetrieveResponse::from)
                .collect(Collectors.toList());
    }

    public LineRetrieveResponse findById(Long id) {
        return lineRepository.findById(id)
                .map(LineRetrieveResponse::from)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.LINE_NOT_FOUND));
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.LINE_NOT_FOUND));
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.findById(id)
                .orElseThrow(() -> new SubwayNotFoundException(SubwayMessage.LINE_NOT_FOUND));
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineRetrieveResponse createLine(LineCreateRequest lineRequest) {
        Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineRequest.getDownStationId());

        Line line = LineCreateRequest.to(lineRequest, upStation, downStation);
        lineRepository.save(line);

        Section section = Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(lineRequest.getDistance())
                .build();
        line.addSection(section);
        return LineRetrieveResponse.from(line);
    }

    @Transactional
    public void appendSection(final Long lineId, SectionCreateRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Line foundLine = this.findLineById(lineId);

        Section section = Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();
        foundLine.addSection(section);
    }

    @Transactional
    public void deleteSection(SectionDeleteRequest request) {
        Line foundLine = this.findLineById(request.getLineId());
        Station station = stationService.findStationById(request.getStationId());
        foundLine.deleteSectionByStation(station);
    }

    public List<Line> findByStation(Station sourceStation, Station targetStation) {
        List<Section> sectionsByStation = sectionService.findByStation(sourceStation, targetStation);
        return sectionsByStation.stream()
                .map(Section::getLine).distinct().collect(Collectors.toList());
    }
}
