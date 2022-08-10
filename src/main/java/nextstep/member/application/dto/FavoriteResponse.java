package nextstep.member.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FavoriteResponse {
    private Long id;
    private StationResponse target;
    private StationResponse source;

    public static FavoriteResponse of(Long id, Station target, Station source) {
        return new FavoriteResponse(id, StationResponse.of(target), StationResponse.of(source));
    }
}
