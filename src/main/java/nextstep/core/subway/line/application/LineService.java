package nextstep.core.subway.line.application;

import nextstep.core.subway.line.application.converter.LineConverter;
import nextstep.core.subway.line.application.dto.LineRequest;
import nextstep.core.subway.line.application.dto.LineResponse;
import nextstep.core.subway.line.domain.Line;
import nextstep.core.subway.line.domain.LineRepository;
import nextstep.core.subway.section.application.dto.SectionRequest;
import nextstep.core.subway.section.application.dto.SectionResponse;
import nextstep.core.subway.section.domain.Section;
import nextstep.core.subway.station.domain.Station;
import nextstep.core.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
    public LineResponse createLine(LineRequest request) {
        Line line = LineConverter.convertToLine(request);
        line.addSection(createSection(request, line));
        return LineConverter.convertToResponse(lineRepository.save(line));
    }

    public List<LineResponse> findAllLineResponses() {
        return lineRepository.findAll().stream()
                .map(LineConverter::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateLine(Long lineId, LineRequest request) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(EntityNotFoundException::new);
        LineConverter.convertToResponse(updateLine(request, line));
    }

    @Transactional
    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    public LineResponse findLineWithConvertResponse(Long lineId) {
        return LineConverter.convertToResponse(findLineById(lineId));
    }

    @Transactional
    public SectionResponse addSection(SectionRequest request) {
        Line line = findLineById(request.getLineId());
        Section section = createSection(request, line);

        return convertToSectionResponse(section.setLine(line));
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationIdToDelete) {
        Line line = findLineById(lineId);
        line.delete(findStation(stationIdToDelete));
    }

    public Line findLineById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("노선 번호에 해당하는 노선이 없습니다."));
    }

    public Station findStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("역 번호에 해당하는 역이 없습니다."));
    }

    private Line updateLine(LineRequest request, Line line) {
        return line.update(
                request.getName(),
                request.getColor()
        );
    }

    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }


    private Section createSection(LineRequest request, Line line) {
        return new Section(
                findStation(request.getUpStationId()),
                findStation(request.getDownStationId()),
                request.getDistance(),
                line);
    }

    private Section createSection(SectionRequest request, Line line) {
        return new Section(
                findStation(request.getUpStationId()),
                findStation(request.getDownStationId()),
                request.getDistance(),
                line
        );
    }

    private SectionResponse convertToSectionResponse(Section section) {
        return new SectionResponse(
                section.getId(),
                findStation(section.getUpStation().getId()),
                findStation(section.getDownStation().getId()),
                section.getDistance()
        );
    }
}
