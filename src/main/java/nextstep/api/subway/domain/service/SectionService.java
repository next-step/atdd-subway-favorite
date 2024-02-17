package nextstep.api.subway.domain.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import nextstep.api.subway.domain.dto.inport.SectionCreateCommand;
import nextstep.api.subway.domain.dto.outport.SectionInfo;
import nextstep.api.subway.domain.model.entity.Section;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
public interface SectionService {
	SectionInfo addSection(Long lineId, SectionCreateCommand createRequest);

	void deleteSection(Long lineId, Long stationId);

	@Transactional(readOnly = true)
	Section fetchSection(Long sourceStationId, Long targetStationId);

	@Transactional(readOnly = true)
	Optional<Section> fetchSectionOptional(Long sourceStationId, Long targetStationId);
}
