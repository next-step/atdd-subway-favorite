package nextstep.line.application;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.line.exception.LineNotFoundException;
import nextstep.line.persistance.LineRepository;
import nextstep.line.presentation.SectionRequest;
import nextstep.line.presentation.SectionResponse;
import nextstep.station.domain.Station;
import nextstep.station.exception.StationNotFoundException;
import nextstep.station.persistance.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    public SectionService(LineRepository lineRepository,
                          StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }


    public SectionResponse createSection(long lineId, SectionRequest sectionRequest) {

        Line line = getLine(lineId);
        Station downStation = getStation(sectionRequest.getDownStationId());
        Station upStation = getStation(sectionRequest.getUpStationId());
        Section section = new Section(upStation, downStation, sectionRequest.getDistance(), line);
        line.addSection(section);
        Line saved = lineRepository.save(line);

        return new SectionResponse(saved.getFirstStation().getId(), saved.getLastStation().getId(), saved.getDistance());
    }

    public void deleteSection(long lineId, long stationId) {

        Line line = getLine(lineId);

        line.removeStation(getStation(stationId));
        lineRepository.save(line);
    }

    private Line getLine(long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }

    private Station getStation(long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(Long.toString(stationId)));
    }
}