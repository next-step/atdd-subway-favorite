package nextstep.api.subway.domain.operators;

import java.util.Optional;

import nextstep.api.subway.domain.model.entity.Section;

/**
 * @author : Rene Choi
 * @since : 2024/02/13
 */
public interface SectionResolver {
	Optional<Section> findByUpStationIdAndDownStationId(Long sourceStationId, Long targetStationId);

}
