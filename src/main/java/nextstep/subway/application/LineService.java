package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.common.LineNotFoundException;
import nextstep.common.StationNotFoundException;
import nextstep.subway.infrastructure.LineRepository;
import nextstep.subway.infrastructure.StationRepository;
import nextstep.subway.presentation.LineRequest;
import nextstep.subway.presentation.LineResponse;
import nextstep.subway.presentation.LineUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = findStationByIdOrThrow(lineRequest.getUpStationId());
        Station downStation = findStationByIdOrThrow(lineRequest.getDownStationId());

        Line createdline = Line.createLine(upStation, downStation, lineRequest);
        lineRepository.save(createdline);

        return LineResponse.of(createdline);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = findLineByIdOrThrow(id);
        return LineResponse.of(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = findLineByIdOrThrow(id);
        line.changeName(lineUpdateRequest.getName());
        line.changeColor(lineUpdateRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = findLineByIdOrThrow(id);
        lineRepository.delete(line);
    }

    @Transactional
    public void deleteStationFromLine(Long lineId, Long stationId) {
        Line line = findLineByIdOrThrow(lineId);
        Station station = findStationByIdOrThrow(stationId);

        line.deleteStation(station);
    }

    private Line findLineByIdOrThrow(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    private Station findStationByIdOrThrow(Long lineRequest) {
        return stationRepository.findById(lineRequest)
                .orElseThrow(() -> new StationNotFoundException(lineRequest));
    }
}

