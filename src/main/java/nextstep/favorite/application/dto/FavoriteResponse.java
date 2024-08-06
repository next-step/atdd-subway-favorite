package nextstep.favorite.application.dto;

import nextstep.station.dto.StationResponse;

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

    public static FavoriteResponse of(final Long id, final StationResponse sourceStationResponse, final StationResponse targetStationResponse) {
        return new FavoriteResponse(id, sourceStationResponse, targetStationResponse);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FavoriteResponse that = (FavoriteResponse) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(source, that.source) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }
}

