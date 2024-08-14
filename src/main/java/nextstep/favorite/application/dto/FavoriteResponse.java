package nextstep.favorite.application.dto;

import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;

import java.util.Objects;

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

    public static FavoriteResponse of(Long id, Station source, Station target) {
        return new FavoriteResponse(id,
                StationResponse.from(source),
                StationResponse.from(target));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StationResponse getSource() {
        return source;
    }

    public void setSource(StationResponse source) {
        this.source = source;
    }

    public StationResponse getTarget() {
        return target;
    }

    public void setTarget(StationResponse target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteResponse that = (FavoriteResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(source, that.source) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }
}
