package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.Setter;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Getter
public class FavoriteResponse {
    private Long id;
    private Station source;
    private Station target;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSourceStation(), favorite.getTargetStation()
                , favorite.getCreatedDate(), favorite.getModifiedDate());
    }

    public FavoriteResponse(Long id, Station source, Station target
            , LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }


}
