package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

import java.util.Objects;

public class FavoriteResponse {
    private final long id;
    private final StationResponse source;
    private final StationResponse target;

    private FavoriteResponse(long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(long id, StationResponse source, StationResponse target) {
        return new FavoriteResponse(id, source, target);
    }

    public static FavoriteResponse of(Favorite favorite) {
        return of(
            favorite.getId(),
            StationResponse.of(favorite.getSource()),
            StationResponse.of(favorite.getTarget())
        );
    }

    public long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteResponse)) return false;
        FavoriteResponse that = (FavoriteResponse) o;
        return id == that.id || (
               Objects.equals(source, that.source) &&
               Objects.equals(target, that.target));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }
}
