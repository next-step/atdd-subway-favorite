package nextstep.subway.applicaion.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }
}
