package nextstep.api.subway.domain.service;

import nextstep.api.subway.domain.dto.inport.SectionCreateCommand;
import nextstep.api.subway.domain.dto.outport.SectionInfo;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
public interface SectionService {
	SectionInfo addSection(Long lineId, SectionCreateCommand createRequest);

	void deleteSection(Long lineId, Long stationId);
}
