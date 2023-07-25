package subway.member.application.dto;

import lombok.Builder;
import lombok.Getter;
import subway.member.domain.Favorite;
import subway.member.domain.MemberFavorites;
import subway.station.application.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class FavoriteRetrieveResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static List<FavoriteRetrieveResponse> from(MemberFavorites memberFavorites) {
        List<Favorite> favorites = memberFavorites.getFavorites();
        return favorites.stream()
                .map(FavoriteRetrieveResponse::from)
                .collect(Collectors.toList());
    }

    public static FavoriteRetrieveResponse from(Favorite favorite) {
        StationResponse sourceStation = StationResponse.from(favorite.getSourceStation());
        StationResponse targetStation = StationResponse.from(favorite.getTargetStation());
        return FavoriteRetrieveResponse.builder()
                .id(favorite.getId())
                .source(sourceStation)
                .target(targetStation)
                .build();
    }

}
