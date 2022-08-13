package nextstep.member.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.domain.Favorite;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteDto {

    private Long id;
    private Station target;
    private Station source;

    public static FavoriteDto of(Long id, Station target, Station source) {
        return new FavoriteDto(id, target, source);
    }

    public static List<FavoriteDto> of(List<Favorite> favorites, Map<Long, Station> stations) {
        return favorites.stream().map(
                favorite -> FavoriteDto.of(favorite.getId(),
                        stations.get(favorite.getTargetId()),
                        stations.get(favorite.getSourceId()))
        ).collect(Collectors.toList());
    }

}
