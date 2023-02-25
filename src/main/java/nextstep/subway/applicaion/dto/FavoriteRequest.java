package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FavoriteRequest {

    private final Long source;
    private final Long target;
}
