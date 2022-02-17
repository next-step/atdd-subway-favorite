package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorites;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoritesResponse {

    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public FavoritesResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static List<FavoritesResponse> convertFavoritesResponses(List<Favorites> favoriteses) {
        List<FavoritesResponse> favoritesResponses = new ArrayList<>();
        for (Favorites favorites : favoriteses) {
            StationResponse source = StationResponse.createStationResponse(favorites.getSource());
            StationResponse target = StationResponse.createStationResponse(favorites.getTarget());
            favoritesResponses.add(new FavoritesResponse(favorites.getId(), source, target));
        }
        return favoritesResponses;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavoritesResponse that = (FavoritesResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(source, that.source) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, target);
    }
}
