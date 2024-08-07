package nextstep.subway.presentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SectionRequest {
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
}
