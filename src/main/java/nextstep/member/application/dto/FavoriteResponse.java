package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

public class FavoriteResponse {

	private Long id;
	private StationResponse source;
	private StationResponse target;

	public static FavoriteResponse of(Favorite favorite) {

		return new FavoriteResponse(
						favorite.getId(),
						StationResponse.of(favorite.getSource()),
						StationResponse.of(favorite.getTarget()));
	}

	public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
		this.id = id;
		this.source = source;
		this.target = target;
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