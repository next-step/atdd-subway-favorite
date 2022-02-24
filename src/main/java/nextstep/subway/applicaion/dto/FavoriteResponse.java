package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;

public class FavoriteResponse {
	private Long id;
	private Long sourceId;
	private Long targetId;

	public FavoriteResponse(Favorite favorite) {
		this.id = favorite.getId();
		this.sourceId = favorite.getSource().getId();
		this.targetId = favorite.getTarget().getId();
	}

	public Long getId() {
		return id;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public Long getTargetId() {
		return targetId;
	}
}
