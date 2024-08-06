package nextstep.section.service;

import nextstep.line.entity.Line;
import nextstep.line.service.LineService;
import nextstep.section.dto.SectionRequest;
import nextstep.section.dto.SectionResponse;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
import nextstep.section.repository.SectionRepository;
import nextstep.station.entity.Station;
import nextstep.station.service.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static nextstep.converter.SectionConverter.convertToSectionResponseByLineAndSection;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StationService stationService;
    private final LineService lineService;

    public SectionService(SectionRepository sectionRepository, StationService stationService, LineService lineService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional
    public SectionResponse createSection(final Long lineId, final SectionRequest sectionRequest) {
        Line line = lineService.getLineByIdOrThrow(lineId);
        Sections sections = line.getSections();

        Station upStation = stationService.getStationByIdOrThrow(sectionRequest.getUpStationId());
        Station downStation = stationService.getStationByIdOrThrow(sectionRequest.getDownStationId());
        Section section = Section.of(upStation, downStation, sectionRequest.getDistance());

        sections.addSection(section);
        lineService.saveLine(line);

        return convertToSectionResponseByLineAndSection(line, section);
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Line line = lineService.getLineByIdOrThrow(lineId);

        Sections sections = line.getSections();
        Section section = sections.getRemoveTargetSection(stationId);

        sections.removeSection(section);

        deleteSection(section);
        lineService.saveLine(line);
    }

    @Transactional
    public void deleteSection(final Section section) {
        sectionRepository.delete(section);
    }

}

