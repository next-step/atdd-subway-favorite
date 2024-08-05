package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.*;
import nextstep.subway.exception.LineNotFoundException;
import nextstep.subway.exception.StationNotFoundException;
import nextstep.subway.infrastructure.LineRepository;
import nextstep.subway.infrastructure.SectionRepository;
import nextstep.subway.infrastructure.StationRepository;
import nextstep.subway.presentation.SectionRequest;
import nextstep.subway.presentation.SectionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class SectionService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionResponse addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
                .orElseThrow(() -> new StationNotFoundException(sectionRequest.getUpStationId()));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
                .orElseThrow(() -> new StationNotFoundException(sectionRequest.getDownStationId()));

        Section requestSection = Section.createSection(
                line,
                upStation,
                downStation,
                sectionRequest.getDistance()
        );
        line.addSection(requestSection);

        Section createdSection = sectionRepository.save(requestSection);

        return SectionResponse.of(createdSection);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
        Sections sections = line.getSections();
        sections.deleteLastSection();
        sectionRepository.deleteById(stationId);
    }
}
