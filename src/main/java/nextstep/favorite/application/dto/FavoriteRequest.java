package nextstep.favorite.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FavoriteRequest {
	@JsonProperty("source")
	private Long sourceId;

	@JsonProperty("target")
	private Long targetId;

	public FavoriteRequest(Long sourceId, Long targetId) {
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public Long getTargetId() {
		return targetId;
	}
}
