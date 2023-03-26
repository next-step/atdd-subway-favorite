package nextstep.favorite.application.dto;

import nextstep.subway.applicaion.dto.StationResponse;

public class FavoriteResponse {
	private Long id;
	private StationResponse source;
	private StationResponse target;

	public FavoriteResponse() {
	}

	protected FavoriteResponse(Long id, StationResponse source, StationResponse target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	public static FavoriteResponse of(Long id, StationResponse source, StationResponse target) {
		return new FavoriteResponse(id, source, target);
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
