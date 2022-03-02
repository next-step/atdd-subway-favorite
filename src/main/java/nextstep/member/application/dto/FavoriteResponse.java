package nextstep.member.application.dto;

import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;

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

	public static FavoriteResponse from(Long id, Station source, Station target) {
		return new FavoriteResponse(id, StationResponse.of(source), StationResponse.of(target));
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
