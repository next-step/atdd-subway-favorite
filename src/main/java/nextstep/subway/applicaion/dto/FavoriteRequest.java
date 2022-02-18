package nextstep.subway.applicaion.dto;

public class FavoriteRequest {
	private Long sourceId;
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
