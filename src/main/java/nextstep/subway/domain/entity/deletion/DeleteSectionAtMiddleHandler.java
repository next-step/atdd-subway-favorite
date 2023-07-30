package nextstep.subway.domain.entity.deletion;

import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.vo.Sections;
import nextstep.subway.domain.entity.Station;

public class DeleteSectionAtMiddleHandler extends SectionDeletionHandler{
    @Override
    public boolean checkApplicable(Sections sections, Station station) {
        return !sections.getFirstStation().equals(station) && !sections.getLastStation().equals(station);
    }

    @Override
    public void apply(Sections sections, Station station) {
        Section sectionIncludesByDownStation = sections.getSectionByDownStation(station);
        Section sectionIncludesByUpStation = sections.getSectionByUpStation(station);

        sections.forceSectionRemove(sectionIncludesByDownStation);
        sections.forceSectionRemove(sectionIncludesByUpStation);

        Section newSection = new Section(sectionIncludesByDownStation.getLine(),
                sectionIncludesByDownStation.getUpStation(),
                sectionIncludesByUpStation.getDownStation(),
                sectionIncludesByDownStation.getDistance() + sectionIncludesByUpStation.getDistance());
        sections.forceSectionAddition(newSection);
    }
}
