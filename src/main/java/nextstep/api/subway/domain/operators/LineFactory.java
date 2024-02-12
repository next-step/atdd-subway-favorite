package nextstep.api.subway.domain.operators;

import nextstep.api.subway.domain.dto.inport.LineCreateCommand;
import nextstep.api.subway.domain.model.entity.Line;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface LineFactory {
	Line createLine(LineCreateCommand request);

	void deleteLine(Line line);

}
