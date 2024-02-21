package nextstep.subway.application;

import nextstep.exception.NotFoundLineException;
import nextstep.subway.application.dto.ShowLineDto;
import nextstep.subway.application.request.AddSectionRequest;
import nextstep.subway.application.request.CreateLineRequest;
import nextstep.subway.application.request.UpdateLineRequest;
import nextstep.subway.application.response.*;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public CreateLineResponse saveLine(CreateLineRequest createLineRequest) {
        Station upStation = stationService.findById(createLineRequest.getUpStationId());
        Station downStation = stationService.findById(createLineRequest.getDownStationId());
        Section section = Section.of(upStation, downStation, createLineRequest.getDistance());

        Line line = lineRepository.save(
                Line.of(
                        createLineRequest.getName(), createLineRequest.getColor()
                )
        );

        line.addSection(section);

        return CreateLineResponse.from(line);
    }

    public ShowAllLinesResponse findAllLines() {
        return ShowAllLinesResponse.of(lineRepository.findAll().stream()
                .map(ShowLineDto::from)
                .collect(Collectors.toList()));
    }

    public ShowLineResponse findLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(NotFoundLineException::new);

        return ShowLineResponse.from(line);
    }

    @Transactional
    public UpdateLineResponse updateLine(Long lineId, UpdateLineRequest updateLineRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(NotFoundLineException::new);

        line.updateLine(updateLineRequest.getColor());

        return UpdateLineResponse.from(line);
    }

    @Transactional
    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional
    public AddSectionResponse addSection(Long lineId, AddSectionRequest addSectionRequest) {
        Station upStation = stationService.findById(addSectionRequest.getUpStationId());
        Station downStation = stationService.findById(addSectionRequest.getDownStationId());
        Section section = Section.of(upStation, downStation, addSectionRequest.getDistance());

        Line line = lineRepository.findById(lineId)
                .orElseThrow(NotFoundLineException::new);

        line.addSection(section);

        return AddSectionResponse.from(section);
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(NotFoundLineException::new);
        Station deletedStation = stationService.findById(stationId);

        line.deleteSection(deletedStation);
    }

}
