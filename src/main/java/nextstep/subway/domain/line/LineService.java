package nextstep.subway.domain.line;

import nextstep.subway.domain.line.dto.LineCreateRequest;
import nextstep.subway.domain.line.dto.LineResponse;
import nextstep.subway.domain.line.dto.LineUpdateRequest;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.dto.SectionCreateRequest;
import nextstep.subway.domain.section.dto.SectionResponse;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private StationService stationService;
    private LineRepository lineRepository;

    public LineService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest request) {
        Station upwardStation = stationService.findByStationId(request.getUpwardStationId());
        Station downwardStation = stationService.findByStationId(request.getDownwardStationId());

        Line line = lineRepository.save(new Line(request.getName(), request.getColor(), new Section(upwardStation, downwardStation, request.getDistance())));

        return LineResponse.of(line);
    }

    public List<LineResponse> findLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = findById(id, "조회할 노선이 존재하지 않습니다.");
        return LineResponse.of(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line line = findById(id, "수정할 노선이 존재하지 않습니다.");
        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public SectionResponse addSection(Long lineId, SectionCreateRequest request) {
        Line line = findById(lineId, "구간을 추가할 노선이 존재하지 않습니다.");
        Station upwardStation = stationService.findByStationId(request.getUpStationId());
        Station downwardStation = stationService.findByStationId(request.getDownStationId());

        Section newSection = new Section(upwardStation, downwardStation, request.getDistance());
        line.addSection(newSection);

        return SectionResponse.of(newSection);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findById(lineId, "구간을 삭제할 노선이 존재하지 않습니다.");
        line.deleteSection(stationService.findByStationId(stationId));
    }

    private Line findById(Long id, String exceptionMessage) {
        return lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(exceptionMessage));
    }
}
