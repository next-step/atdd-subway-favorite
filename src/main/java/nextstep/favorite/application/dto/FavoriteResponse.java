package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;

public class FavoriteResponse {
    private Long id;
    private Member member;
    private Station source;
    private Station target;

    public FavoriteResponse() {

    }

    public FavoriteResponse(Long id) {
        this.id = id;
    }

    public FavoriteResponse(Long id, Member member, Station source, Station target, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.member = member;
        this.source = source;
        this.target = target;
        this.source.setCreatedDate(createdDate);
        this.target.setCreatedDate(createdDate);
        this.source.setModifiedDate(modifiedDate);
        this.target.setModifiedDate(modifiedDate);
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getMember(), favorite.getSource(), favorite.getTarget(), favorite.getCreatedDate(), favorite.getModifiedDate());
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

}
