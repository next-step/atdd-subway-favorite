package nextstep.favorite.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.common.SubwayErrorMessage;
import nextstep.subway.exception.NoStationException;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationResponse;

import java.util.List;

/**
 * TODO: StationResponse를 포함하는 클래스로 만듭니다.
 */
@Getter
@NoArgsConstructor
public class FavoriteResponse {

    private long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(Favorite favorite, List<Station> stations) {
        Station sourceStation = stations.stream().filter(station -> station.getId().equals(favorite.getSourceStationId()))
                .findFirst()
                .orElseThrow(() -> new NoStationException(SubwayErrorMessage.NO_STATION_EXIST));

        Station targetStation = stations.stream().filter(station -> station.getId().equals(favorite.getTargetStationId()))
                .findFirst()
                .orElseThrow(() -> new NoStationException(SubwayErrorMessage.NO_STATION_EXIST));

        return new FavoriteResponse(favorite.getId(),
                new StationResponse(sourceStation.getId(), sourceStation.getName()),
                new StationResponse(targetStation.getId(), targetStation.getName())
        );
    }
}
