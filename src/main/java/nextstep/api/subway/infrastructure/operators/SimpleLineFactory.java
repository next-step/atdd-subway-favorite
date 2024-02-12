package nextstep.api.subway.infrastructure.operators;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import nextstep.api.subway.domain.dto.inport.LineCreateCommand;
import nextstep.api.subway.domain.model.entity.Line;
import nextstep.api.subway.domain.operators.LineFactory;
import nextstep.api.subway.infrastructure.persistence.LineRepository;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Component
@RequiredArgsConstructor
public class SimpleLineFactory implements LineFactory {

	private final LineRepository lineRepository;

	@Override
	public Line createLine(LineCreateCommand request) {
		return lineRepository.save(Line.from(request));
	}

	@Override
	public void deleteLine(Line line) {
		lineRepository.delete(line);
	}

}
