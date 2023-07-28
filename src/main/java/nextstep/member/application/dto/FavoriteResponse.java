package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                new StationResponse(favorite.getSource().getId(), favorite.getSource().getName()),
                new StationResponse(favorite.getTarget().getId(), favorite.getTarget().getName())
        );
    }

    public static List<FavoriteResponse> of(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
