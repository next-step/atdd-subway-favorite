package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteRequest {

    private Long source;
    private Long target;

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }
}
