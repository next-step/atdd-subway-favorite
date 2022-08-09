package nextstep.subway.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private long memberId;

	private long sourceStationId;

	private long targetStationId;

	public Favorite() {
	}

	public Favorite(long memberId, long sourceStationId, long targetStationId) {
		this.memberId = memberId;
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	public boolean isSameFavorite(long sourceId, long targetId) {
		return (this.sourceStationId == sourceId && this.targetStationId == targetId);
	}

	public boolean isSameFavorite(long favoriteId) {
		return this.id == favoriteId;
	}

	public boolean isNotSameMember(long memberId) {
		return this.memberId != memberId;
	}

	public Long getId() {
		return id;
	}

	public long getMemberId() {
		return memberId;
	}

	public long getSourceStationId() {
		return sourceStationId;
	}

	public long getTargetStationId() {
		return targetStationId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Favorite favorite = (Favorite)o;
		return memberId == favorite.memberId && sourceStationId == favorite.sourceStationId
			&& targetStationId == favorite.targetStationId && Objects.equals(id, favorite.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, memberId, sourceStationId, targetStationId);
	}
}
