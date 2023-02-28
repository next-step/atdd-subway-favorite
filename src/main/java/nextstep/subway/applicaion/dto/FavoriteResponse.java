package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

import java.util.Objects;

@Getter
public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(final Favorite favorite, final Station source, final Station target) {
        this.id = favorite.getId();
        this.source = StationResponse.of(source);
        this.target = StationResponse.of(target);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FavoriteResponse that = (FavoriteResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(source, that.source) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }

    @Override
    public String toString() {
        return "FavoriteResponse{" +
                "id=" + id +
                ", source=" + source +
                ", target=" + target +
                '}';
    }
}
