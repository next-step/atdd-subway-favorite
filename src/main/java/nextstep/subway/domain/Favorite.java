package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long memberId;

	private Long sourceStationId;

	private Long targetStationId;

	public Favorite(Long memberId, Long sourceStationId, Long targetStationId) {
		this.memberId = memberId;
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}
}
