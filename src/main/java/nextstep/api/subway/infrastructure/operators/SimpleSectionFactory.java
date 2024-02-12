package nextstep.api.subway.infrastructure.operators;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.domain.dto.inport.LineCreateCommand;
import nextstep.api.subway.domain.dto.inport.SectionCreateCommand;
import nextstep.api.subway.domain.model.entity.Line;
import nextstep.api.subway.domain.model.entity.Section;
import nextstep.api.subway.domain.model.entity.Station;
import nextstep.api.subway.domain.operators.SectionFactory;
import nextstep.api.subway.infrastructure.persistence.SectionRepository;
import nextstep.api.subway.infrastructure.persistence.StationRepository;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Component
@RequiredArgsConstructor
public class SimpleSectionFactory implements SectionFactory {
	private final StationRepository stationRepository;
	private final SectionRepository sectionRepository;

	@Override
	public Section createSection(SectionCreateCommand command, Station upStation, Station downStation) {
		Section section = Section.of(upStation, downStation, command.getDistance());
		return sectionRepository.save(section);
	}

	@Override
	public Section createSection(LineCreateCommand createCommand) {
		Station upStation = stationRepository.findById(createCommand.getUpStationId()).orElseThrow();
		Station downStation = stationRepository.findById(createCommand.getDownStationId()).orElseThrow();
		return sectionRepository.save(Section.of(upStation, downStation, createCommand.getDistance()));
	}

	@Override
	public void deleteByLine(Line line) {
		sectionRepository.deleteAll(line.getSectionCollection());
	}

}
