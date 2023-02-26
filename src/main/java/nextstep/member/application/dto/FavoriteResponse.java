package nextstep.member.application.dto;

import lombok.Builder;
import nextstep.member.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    @Builder
    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return FavoriteResponse.builder()
            .id(favorite.getId())
            .source(new StationResponse(favorite.getSourceId(), favorite.getSourceName()))
            .target(new StationResponse(favorite.getTargetId(), favorite.getTargetName()))
            .build();
    }
}
