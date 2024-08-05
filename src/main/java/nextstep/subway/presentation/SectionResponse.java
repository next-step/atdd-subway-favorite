package nextstep.subway.presentation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Section;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SectionResponse {
    @Getter
    private final Long lineId;
    @Getter
    private final Long sectionId;
    private final Long upStationId;
    @Getter
    private final Long downStationId;
    private final int distance;

    public static SectionResponse of(Section section) {
        return new SectionResponse(
                section.getLine().getId(),
                section.getId(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getSectionDistance().getDistance()
        );
    }
}
