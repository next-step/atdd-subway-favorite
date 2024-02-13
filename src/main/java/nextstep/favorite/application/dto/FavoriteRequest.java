package nextstep.favorite.application.dto;

import lombok.Getter;
import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.station.Station;

@Getter
public class FavoriteRequest {
    private Long source;
    private Long target;


}
