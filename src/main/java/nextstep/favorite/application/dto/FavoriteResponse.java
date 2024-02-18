package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.application.dto.PathResponse;
import nextstep.subway.application.dto.StationResponse;

import java.util.List;

/**
 * TODO: StationResponse를 포함하는 클래스로 만듭니다.
 */
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(final Long id, final Long sourceId, final String sourceName,
                            final Long targetId, final String targetName) {
        this.id = id;
        this.source = new StationResponse(sourceId, sourceName);
        this.target = new StationResponse(targetId, targetName);
    }

    public FavoriteResponse(final Favorite savedFavorite, final PathResponse pathResponse) {
        this.id = savedFavorite.getId();

        final List<StationResponse> stations = pathResponse.getStations();
        this.source = stations.get(0);
        this.target = stations.get(stations.size() - 1);
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
    public String toString() {
        return "FavoriteResponse{" +
                "id=" + id +
                ", source=" + source +
                ", target=" + target +
                '}';
    }
}
