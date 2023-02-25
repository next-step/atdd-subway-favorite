package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
    Long id;
    Station source;
    Station target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSourceStation(), favorite.getTargetStation());
    }

    public static List<FavoriteResponse> listOf(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
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
