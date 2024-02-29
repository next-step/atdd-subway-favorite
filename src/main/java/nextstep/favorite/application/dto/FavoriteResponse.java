package nextstep.favorite.application.dto;

import java.util.List;
import nextstep.subway.applicaion.dto.StationResponse;

/**
 * TODO: StationResponse를 포함하는 클래스로 만듭니다.
 */
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StationResponse getSource() {
        return source;
    }

    public void setSource(StationResponse source) {
        this.source = source;
    }

    public StationResponse getTarget() {
        return target;
    }

    public void setTarget(StationResponse target) {
        this.target = target;
    }
}
