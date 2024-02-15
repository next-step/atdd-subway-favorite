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

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
