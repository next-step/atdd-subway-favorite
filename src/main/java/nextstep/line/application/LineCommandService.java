package nextstep.line.application;

import nextstep.exceptions.ErrorMessage;
import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.line.exception.LineNotFoundException;
import nextstep.line.payload.AddSectionRequest;
import nextstep.line.payload.CreateLineRequest;
import nextstep.line.payload.LineResponse;
import nextstep.line.payload.UpdateLineRequest;
import nextstep.line.repository.LineRepository;
import nextstep.path.repository.PathResolver;
import nextstep.station.domain.Station;
import nextstep.station.exception.NonExistentStationException;
import nextstep.station.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Service
public class LineCommandService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    private final PathResolver pathResolver;


    public LineCommandService(final LineRepository lineRepository, final StationRepository stationRepository, final PathResolver pathResolver) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.pathResolver = pathResolver;
    }

    public LineResponse saveLine(final CreateLineRequest request) {
        var upStation = this.getStationById(request.getUpStationId());
        var downStation = this.getStationById(request.getDownStationId());

        Line line = lineRepository.save(
                new Line(request.getName(),
                        request.getColor(),
                        new Section(upStation.getId(), downStation.getId(), request.getDistance())
                ));

        pathResolver.removeAll();
        return LineResponse.from(line, getLineStations(line));
    }

    public void modify(final Long id, final UpdateLineRequest request) {
        var line = getLineById(id);
        line.update(request.getName(), request.getColor());
        pathResolver.removeAll();
    }

    public void delete(final Long id) {
        var line = getLineById(id);
        lineRepository.delete(line);
        pathResolver.removeAll();
    }

    public void addSection(final Long id, final AddSectionRequest request) {
        Line line = getLineById(id);
        var upStation = getStationById(request.getUpStationId());
        var downStation = getStationById(request.getDownStationId());
        line.addSection(upStation.getId(), downStation.getId(), request.getDistance());
        pathResolver.removeAll();
    }

    public void removeSection(final Long id, final Long stationId) {
        Line line = getLineById(id);
        line.removeStation(stationId);
        pathResolver.removeAll();
    }

    private Station getStationById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NonExistentStationException(ErrorMessage.NON_EXISTENT_STATION));
    }

    private Line getLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException(ErrorMessage.LINE_NOT_FOUND));
    }

    private List<Station> getLineStations(final Line line) {
        return stationRepository.findByIdIn(line.getStationIds());
    }

}
