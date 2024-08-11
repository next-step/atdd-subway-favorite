package nextstep.section.application;

import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.section.domain.Section;
import nextstep.section.domain.SectionRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.line.application.dto.LineResponse;
import nextstep.section.application.dto.SectionRequest;
import nextstep.line.application.LineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class SectionService {
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final LineService lineService;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository, StationRepository stationRepository, LineService lineService) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.lineService = lineService;
    }

    @Transactional
    public void saveSection(Long lineId, SectionRequest sectionRequest) {

        Line line = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
                .orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
                .orElseThrow(IllegalArgumentException::new);

        line.addSection(new Section(line, upStation.getId(), downStation.getId(), sectionRequest.getDistance()));
    }

    @Transactional
    public LineResponse deleteSection(Long id, Long stationId) {

        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.removeStation(stationId);
        return LineResponse.createResponse(line, lineService.getLineStations(line));

    }

}
