package nextstep.subway.favorite.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "member_id", nullable = false)
	private Long memberId;

	@ManyToOne
	@JoinColumn(name = "source_id")
	private Station source;

	@ManyToOne
	@JoinColumn(name = "target_id")
	private Station target;

	protected Favorite() {
	}

	public Favorite(Long memberId, Station source, Station target) {
		this.memberId = memberId;
		this.source = source;
		this.target = target;
	}

	public static Favorite of(Long memberId, Station source, Station target) {
		return new Favorite(memberId, source, target);
	}

	public boolean isOwner(Long memberId) {
		return this.memberId.equals(memberId);
	}

	public Long getId() {
		return id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public Station getSource() {
		return source;
	}

	public Station getTarget() {
		return target;
	}
}
