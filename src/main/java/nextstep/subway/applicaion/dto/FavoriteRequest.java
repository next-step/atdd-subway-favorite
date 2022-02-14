package nextstep.subway.applicaion.dto;

import javax.validation.constraints.Positive;

public class FavoriteRequest {

    @Positive
    private Long source;
    @Positive
    private Long target;

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
