package nextstep.api.subway.domain.operators;

import nextstep.api.subway.domain.dto.inport.LineCreateCommand;
import nextstep.api.subway.domain.dto.inport.SectionCreateCommand;
import nextstep.api.subway.domain.model.entity.Line;
import nextstep.api.subway.domain.model.entity.Section;
import nextstep.api.subway.domain.model.entity.Station;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface SectionFactory {

	Section createSection(LineCreateCommand createCommand);

	Section createSection(SectionCreateCommand command, Station upStation, Station downStation);

	void deleteByLine(Line line);

}
