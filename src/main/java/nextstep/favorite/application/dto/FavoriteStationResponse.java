package nextstep.favorite.application.dto;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import nextstep.subway.domain.Station;

@Getter
public class FavoriteStationResponse {
    private Long id;
    private String name;

    public FavoriteStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static FavoriteStationResponse of(Station station) {
        return new FavoriteStationResponse(station.getId(), station.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavoriteStationResponse that = (FavoriteStationResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
