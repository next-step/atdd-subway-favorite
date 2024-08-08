package nextstep.line.application;

import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.section.domain.Section;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.line.application.dto.LineRequest;
import nextstep.line.application.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

        Line line = lineRepository.save(
                new Line(lineRequest.getName(), lineRequest.getColor()));

        line.addSection(new Section(line, lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));

        return LineResponse.createResponse(line, getLineStations(line));
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        List<LineResponse> lineResponses = new ArrayList<>();

        for (Line line : lines) {
            lineResponses.add(LineResponse.createResponse(line, getLineStations(line)));
        }

        return lineResponses;
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return LineResponse.createResponse(line, getSortedStations(line.getStationIds()));
    }

    @Transactional
    public void editLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.updateLine(lineRequest.getName(), lineRequest.getColor());
        lineRepository.save(line);

    }
    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }


    public List<Station> getLineStations(Line line) {
        return stationRepository.findByIdIn(line.getStationIds());
    }


    private Map<Long, Station> getStationMap(Collection<Long> stationsIds) {
        return stationRepository.findByIdIn(stationsIds)
                .stream()
                .collect(Collectors.toMap(Station::getId, (station -> station)));
    }

    private List<Station> getSortedStations(Collection<Long> stationsIds) {
        Map<Long, Station> stationMap = getStationMap(stationsIds);
        return stationsIds.stream()
                .map(stationMap::get)
                .collect(Collectors.toList());
    }




}
