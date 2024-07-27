package nextstep.subway.domain.service;

import nextstep.subway.domain.model.Line;
import nextstep.subway.domain.model.Section;

public interface SectionAdditionStrategyFactory {
    SectionAdditionStrategy getStrategy(Line line, Section section);
}
