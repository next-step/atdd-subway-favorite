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

    public static FavoriteResponse of(FavoriteDto favoriteDto) {
        return new FavoriteResponse(favoriteDto.getId(),
                StationResponse.of(favoriteDto.getTarget()),
                StationResponse.of(favoriteDto.getSource())
        );
    }
}
