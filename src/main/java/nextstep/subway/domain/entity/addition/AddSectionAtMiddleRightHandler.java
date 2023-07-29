package nextstep.subway.domain.entity.addition;

import nextstep.common.exception.CreationValidationException;
import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.vo.Sections;

public class AddSectionAtMiddleRightHandler extends SectionAdditionHandler {
    @Override
    public boolean checkApplicable(Sections sections, Section section) {
        return sections.checkDownStationsContains(section.getDownStation());
    }

    @Override
    public void validate(Sections sections, Section section) {
        validateNewSectionLengthSmaller(sections.getSectionByDownStation(section.getDownStation()), section);
    }

    @Override
    public void apply(Sections sections, Section newSection) {
        Section sectionByDownStation = sections.getSectionByDownStation(newSection.getDownStation());
        sectionByDownStation.changeDownStation(newSection.getUpStation());
        sections.forceSectionAddition(newSection);
    }

    private static void validateNewSectionLengthSmaller(Section originalSection, Section section) {
        if (section.getDistance().compareTo(originalSection.getDistance()) != -1) {
            throw new CreationValidationException("section.0002");
        }
    }
}
