package nextstep.member.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.Station;

@Entity
public class Favorite extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long memberId;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "source")
	private Station source;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "target")
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

	public void validateMember(Long memberId) {
		if(!this.memberId.equals(memberId)) {
			throw new AuthenticationException();
		}
	}
}
