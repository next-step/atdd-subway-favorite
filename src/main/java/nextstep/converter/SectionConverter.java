package nextstep.converter;

import nextstep.line.entity.Line;
import nextstep.section.dto.SectionResponse;
import nextstep.section.entity.Section;
import nextstep.station.dto.StationResponse;
import nextstep.line.entity.Line;
import nextstep.section.dto.SectionResponse;
import nextstep.section.entity.Section;
import nextstep.station.dto.StationResponse;

public class SectionConverter {

    private SectionConverter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static SectionResponse convertToSectionResponseByLineAndSection(final Line line, final Section section) {

        return SectionResponse.of(line.getId(), section.getId(), StationResponse.of(section.getUpStation().getId(), section.getUpStation().getName()),
                StationResponse.of(section.getDownStation().getId(), section.getDownStation().getName()), section.getDistance());

    }
}

