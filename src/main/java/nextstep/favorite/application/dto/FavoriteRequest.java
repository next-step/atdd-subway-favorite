package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;

public class FavoriteRequest {

	private Long source;
	private Long target;

	public FavoriteRequest() {
	}

	public FavoriteRequest(Long source, Long target) {
		this.source = source;
		this.target = target;
	}

	public Favorite toFavorite(Long memberId) {
		return new Favorite(
			null,
			memberId,
			source,
			target
		);
	}

	public Long getSource() {
		return source;
	}

	public Long getTarget() {
		return target;
	}
}
