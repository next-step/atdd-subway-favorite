package nextstep.favorite.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

@Entity
public class Favorite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Member member;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Station source;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Station target;

	public Favorite() {
	}

	public Favorite(Member member, Station source, Station target) {
		this.member = member;
		this.source = source;
		this.target = target;
	}

	public Long getId() {
		return id;
	}

	public Member getMember() {
		return member;
	}

	public Station getSource() {
		return source;
	}

	public Station getTarget() {
		return target;
	}

	public boolean isFavoriteOfMember(Member member) {
		return Objects.equals(this.member, member);
	}
}
