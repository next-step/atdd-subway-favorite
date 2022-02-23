package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;
import nextstep.subway.domain.Station;

public class FavoriteResponse {
	private Long id;
	private Station source;
	private Station target;

	public FavoriteResponse() {
	}

	public FavoriteResponse(Long id, Station source, Station target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	public static FavoriteResponse of(Favorite favorite) {
		return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getSource());
	}

	public Long getId() {
		return id;
	}

	public Station getSource() {
		return source;
	}

	public Station getTarget() {
		return target;
	}
}
