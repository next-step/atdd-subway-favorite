package nextstep.subway.line.application;

import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.application.dto.SectionRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.exception.IllegalSectionException;
import nextstep.subway.exception.NoSuchLineException;
import nextstep.subway.exception.NoSuchStationException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
        Station upStation = null;
        Station downStation = null;

        if (lineRequest.getUpStationId() != null) {
            upStation = getStation(lineRequest.getUpStationId());
        }
        if (lineRequest.getDownStationId() != null) {
            downStation = getStation(lineRequest.getDownStationId());
        }
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        line.addSection(upStation, downStation, lineRequest.getDistance());

        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLines() {
        List<Line> allLines = lineRepository.findAll();
        return allLines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = getLine(id);
        return LineResponse.from(line);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = getLine(id);
        Optional.ofNullable(lineRequest.getName()).ifPresent(line::setName);
        Optional.ofNullable(lineRequest.getColor()).ifPresent(line::setColor);
    }

    @Transactional
    public void removeLine(Long id) {
        Line line = getLine(id);
        lineRepository.delete(line);
    }

    @Transactional
    public LineResponse addSection(Long id, SectionRequest sectionRequest) {
        Line line = getLine(id);
        Station upStation = getStation(sectionRequest.getUpStationId());

        Station downStation = getStation(sectionRequest.getDownStationId());

        if (!line.addableSection(upStation, downStation, sectionRequest.getDistance())) {
            throw new IllegalSectionException("추가할 수 없는 구간입니다.");
        }

        line.addSection(upStation, downStation, sectionRequest.getDistance());

        return LineResponse.from(line);
    }

    @Transactional
    public void removeSection(Long id, Long stationId) {
        Line line = getLine(id);
        Station station = getStation(stationId);

        line.removeSection(station);
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchLineException("존재하지 않는 노선입니다."));
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NoSuchStationException("존재하지 않는 역입니다."));
    }
}
