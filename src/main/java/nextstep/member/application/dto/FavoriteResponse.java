package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;
import nextstep.subway.domain.Station;

public class FavoriteResponse {
    private Long memberId;
    private Station source;
    private Station target;

    protected FavoriteResponse() {
    }

    public FavoriteResponse(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getMemberId(), favorite.getSource(), favorite.getTarget());
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
