package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.response.StationResponse;
import nextstep.subway.domain.Station;

@Getter
@AllArgsConstructor
public class FavoriteResponse {
    private Long id;
    private MemberResponse memeber;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public static FavoriteResponse of(Favorite favorite) {
        MemberResponse member = MemberResponse.of(favorite.getMember());
        StationResponse source = StationResponse.of(favorite.getSource());
        StationResponse target = StationResponse.of(favorite.getTarget());
        return new FavoriteResponse(favorite.getId(),member,source,target);
    }
}
