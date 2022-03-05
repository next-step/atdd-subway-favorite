package nextstep.favorite.domain;

import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Long memberId;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "source_id")
	private Station source;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "target_id")
	private Station target;

	public Favorite() {
	}

	public Favorite(Long memberId, Station source, Station target) {
		this.memberId = memberId;
		this.source = source;
		this.target = target;
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

	@Override
	public boolean equals(Object target) {
		if (this == target) {
			return true;
		}

		if (target == null || !(target instanceof Station)) {
			return false;
		}

		Station station = (Station) target;
		return station.getId().equals(station.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
