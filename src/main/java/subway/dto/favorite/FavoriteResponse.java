package subway.dto.favorite;

import subway.dto.station.StationResponse;
import subway.favorite.Favorite;

public class FavoriteResponse {
	private Long id;
	private StationResponse source;
	private StationResponse target;

	public FavoriteResponse() {
	}

	public FavoriteResponse(Favorite favorite) {
		this.id = favorite.getId();
		this.source = StationResponse.of(favorite.getSourceStation());
		this.target = StationResponse.of(favorite.getTargetStation());
	}

	public static FavoriteResponse of(Favorite favorite) {
		return new FavoriteResponse(favorite);
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
}
