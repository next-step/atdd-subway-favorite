package nextstep.common.fixture;

import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import nextstep.utils.ReflectionUtils;

public class SectionFactory {
    private SectionFactory() {

    }

    public static Section createSection(final Station upStation, final Station downStation, final int distance) {
        return new Section(upStation, downStation, distance);
    }

    public static Section createSection(final Long id, final Station upStation, final Station downStation, final int distance) {
        final Section section = createSection(upStation, downStation, distance);
        ReflectionUtils.injectIdField(section, id);
        return section;
    }

}
