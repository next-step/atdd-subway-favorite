package nextstep.line.application;

import static nextstep.global.exception.ExceptionCode.NOT_FOUND_LINE;
import static nextstep.global.exception.ExceptionCode.NOT_FOUND_STATION;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.station.application.StationReader;
import nextstep.station.domain.Station;
import nextstep.global.exception.CustomException;
import nextstep.line.domain.Line;
import nextstep.line.domain.Sections;
import nextstep.line.infrastructure.LineRepository;
import nextstep.line.presentation.dto.LineRequest;
import nextstep.line.presentation.dto.LineResponse;
import nextstep.line.presentation.dto.SectionRequest;
import nextstep.line.presentation.dto.SectionResponse;
import nextstep.line.presentation.dto.SectionsResponse;
import nextstep.station.infrastructure.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final StationReader stationReader;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, StationReader stationReader) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.stationReader = stationReader;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(toEntity(lineRequest));

        line.addSection(stationReader.findById(lineRequest.getUpStationId()), stationReader.findById(lineRequest.getDownStationId()), lineRequest.getDistance());

        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse.Builder(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .distance(line.getDistance())
                .sections(createSectionsResponse(line.getSections()))
                .build();
    }

    private Line toEntity(LineRequest lineRequest) {
        return new Line(
                lineRequest.getName(),
                lineRequest.getColor());
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지하철 노선은 존재하지 않습니다. id=" + lineId));
        return createLineResponse(line);
    }

    private SectionsResponse createSectionsResponse(Sections sections) {
        return new SectionsResponse(sections.getLineSections().stream()
                .map(section -> new SectionResponse.Builder()
                        .id(section.getId())
                        .upStationId(section.getUpStationId())
                        .downStationId(section.getDownStationId())
                        .distance(section.getDistance())
                        .build())
                .collect(Collectors.toList()));
    }

    @Transactional
    public void updateLine(Long lineId, LineRequest lineRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LINE));

        line.updateName(lineRequest.getName());
        line.updateColor(lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = this.lineRepository.findById(lineId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LINE));

        line.addSection(stationReader.findById(sectionRequest.getUpStationId()),
                stationReader.findById(sectionRequest.getDownStationId()), sectionRequest.getDistance());
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = this.lineRepository.findById(lineId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_LINE));

        Station station = this.stationRepository.findById(stationId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_STATION));

        line.deleteSection(station);
    }
}
