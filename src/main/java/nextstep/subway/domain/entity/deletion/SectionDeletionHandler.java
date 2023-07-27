package nextstep.subway.domain.entity.deletion;

import nextstep.subway.domain.vo.Sections;
import nextstep.subway.domain.entity.Station;

public abstract class SectionDeletionHandler {
    public abstract boolean checkApplicable(Sections sections, Station station);

    public abstract void apply(Sections sections, Station station);
}
