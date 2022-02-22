package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;

public class FavoriteResponse {
    private Long id;
    private Long memberId;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse(Long id, Long memberId,
        StationResponse source, StationResponse target) {
        this.id = id;
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getMemberId(),
            StationResponse.of(favorite.getSource()), StationResponse.of(favorite.getTarget()));
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
