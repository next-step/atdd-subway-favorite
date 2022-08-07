package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.Setter;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Getter
public class FavoriteResponse {
    private Long id;
    private Member member;
    private Station source;
    private Station target;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getMember(), favorite.getSourceStation(), favorite.getTargetStation()
                , favorite.getCreatedDate(), favorite.getModifiedDate());
    }

    public FavoriteResponse(Long id,Member member, Station source, Station target
            , LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.member = member;
        this.source = source;
        this.target = target;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }


}
