package nextstep.line.application;


import nextstep.line.application.dto.line.AddLineCommand;
import nextstep.line.application.dto.line.LineDto;
import nextstep.line.application.dto.line.UpdateLineCommand;
import nextstep.line.application.dto.section.AddSectionCommand;
import nextstep.line.application.dto.section.SectionDto;
import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.line.domain.Section;
import nextstep.line.domain.exception.LineNotFoundException;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.station.domain.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public LineDto saveLine(AddLineCommand command) {
        Station upStation = findStationByIdOrFail(command.getUpStationId());
        Station downStation = findStationByIdOrFail(command.getDownStationId());
        Line line = lineRepository.save(Line.create(
                command.getName(),
                command.getColor(),
                upStation,
                downStation,
                command.getDistance()
        ));
        return LineDto.from(line);
    }

    public List<LineDto> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineDto::from).collect(Collectors.toList());
    }

    public LineDto getLineByIdOrFail(Long id) {
        Line line = this.findLineByIdOrFail(id);
        return LineDto.from(line);
    }

    @Transactional
    public void updateLine(UpdateLineCommand command) {
        Line line = this.findLineByIdOrFail(command.getTargetId());
        line.update(command.getName(), command.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public SectionDto addSection(AddSectionCommand command) {
        Station upStation = findStationByIdOrFail(command.getUpStationId());
        Station downStation = findStationByIdOrFail(command.getDownStationId());

        Line line = findLineByIdOrFail(command.getLineId());

        Section section = Section.create(upStation, downStation, command.getDistance());
        line.addSection(section);

        return SectionDto.from(section);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineByIdOrFail(lineId);
        line.deleteStation(stationId);
    }

    private Line findLineByIdOrFail(Long id) {
        return lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
    }

    private Station findStationByIdOrFail(Long id) {
        return stationRepository.findById(id).orElseThrow(StationNotFoundException::new);
    }
}
