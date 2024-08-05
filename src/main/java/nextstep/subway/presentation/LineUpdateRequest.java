package nextstep.subway.presentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LineUpdateRequest {
    private final String name;
    private final String color;
}
