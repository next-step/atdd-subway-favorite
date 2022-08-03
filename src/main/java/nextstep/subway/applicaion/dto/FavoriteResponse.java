package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class FavoriteResponse {
    private final Long id;
    private final FavoriteStationDto source;
    private final FavoriteStationDto target;

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), FavoriteStationDto.of(favorite.getSource()), FavoriteStationDto.of(favorite.getTarget()));
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    private static class FavoriteStationDto {
        private final Long id;
        private final String name;
        private final LocalDateTime createdDate;
        private final LocalDateTime modifiedDate;


        public static FavoriteStationDto of(Station station) {
            return new FavoriteStationDto(station.getId(), station.getName(), station.getCreateDate(), station.getModifiedDate());
        }
    }
}
