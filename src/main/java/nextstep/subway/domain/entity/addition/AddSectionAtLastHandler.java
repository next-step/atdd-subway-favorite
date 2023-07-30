package nextstep.subway.domain.entity.addition;

import nextstep.common.exception.CreationValidationException;
import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.vo.Sections;

public class AddSectionAtLastHandler extends SectionAdditionHandler {
    @Override
    public boolean checkApplicable(Sections sections, Section section) {
        return sections.equalsLastStation(section.getUpStation());
    }

    @Override
    public void validate(Sections sections, Section section) {
        validateNewSectionDownStationIsNewcomer(sections, section);
    }

    @Override
    public void apply(Sections sections, Section newSection) {
        sections.forceSectionAddition(newSection);
    }

    private void validateNewSectionDownStationIsNewcomer(Sections sections, Section section) {
        if (sections.hasStation(section.getDownStation())) {
            throw new CreationValidationException("section.0001");
        }
    }
}
