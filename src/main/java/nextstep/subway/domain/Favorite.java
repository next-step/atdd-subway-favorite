package nextstep.subway.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private long memberId;

	private long sourceStationId;

	private long targetStationId;

	public Favorite(long memberId, long sourceStationId, long targetStationId) {
		this.memberId = memberId;
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	public boolean isSameFavorite(long sourceId, long targetId) {
		return (this.sourceStationId == sourceId && this.targetStationId == targetId);
	}

}
