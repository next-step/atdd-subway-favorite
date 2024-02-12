package nextstep.api.subway.infrastructure.operators;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.domain.model.entity.Line;
import nextstep.api.subway.domain.operators.LineResolver;
import nextstep.api.subway.infrastructure.persistence.LineRepository;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Component
@RequiredArgsConstructor
public class SimpleLineResolver implements LineResolver {
	private final LineRepository lineRepository;

	@Override
	public List<Line> fetchAll() {
		return lineRepository.findAll();
	}

	@Override
	public Optional<Line> fetchOptional(Long id) {
		return lineRepository.findById(id);
	}

}
