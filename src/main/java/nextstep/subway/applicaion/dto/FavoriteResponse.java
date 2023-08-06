package nextstep.subway.applicaion.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.domain.Favorite;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
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

    public static List<FavoriteResponse> listOf(List<Favorite> favorites) {
        return favorites.stream()
            .map(favorite -> new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget())))
            .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavoriteResponse that = (FavoriteResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getSource(),
            that.getSource()) && Objects.equals(getTarget(), that.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSource(), getTarget());
    }
}
