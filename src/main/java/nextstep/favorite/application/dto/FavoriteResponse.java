package nextstep.favorite.application.dto;

import nextstep.subway.application.dto.StationResponse;
import nextstep.subway.domain.Station;

import java.util.List;

/**
 * TODO: StationResponse를 포함하는 클래스로 만듭니다.
 */
public class FavoriteResponse {
    private Long id;
    private List<StationResponse> stations;

    public FavoriteResponse() {
    }

    public FavoriteResponse(final Long id, final Long sourceId, final String sourceName,
                            final Long targetId, final String targetName) {
        this.id = id;
        this.stations = List.of(new StationResponse(sourceId, sourceName),
                new StationResponse(targetId, targetName));
    }

    public Long getId() {
        return id;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    @Override
    public String toString() {
        return "FavoriteResponse{" +
                "id=" + id +
                ", stations=" + stations +
                '}';
    }
}
