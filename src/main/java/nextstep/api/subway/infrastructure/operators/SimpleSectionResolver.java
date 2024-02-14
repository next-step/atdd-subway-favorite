package nextstep.api.subway.infrastructure.operators;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.domain.model.entity.Section;
import nextstep.api.subway.domain.operators.SectionResolver;
import nextstep.api.subway.infrastructure.persistence.SectionRepository;

/**
 * @author : Rene Choi
 * @since : 2024/02/13
 */
@Component
@RequiredArgsConstructor
public class SimpleSectionResolver implements SectionResolver {
	private final SectionRepository sectionRepository;

	@Override
	public Optional<Section> findByUpStationIdAndDownStationId(Long sourceStationId, Long targetStationId) {
		return sectionRepository.findByUpStationIdAndDownStationId(sourceStationId, targetStationId);
	}

}
