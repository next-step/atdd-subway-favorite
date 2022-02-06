package nextstep.member.domain;

import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "source_id")
	private Station source;

	@ManyToOne
	@JoinColumn(name = "target_id")
	private Station target;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	protected Favorite() {
	}

	public Favorite(Station source, Station target, Member member) {
		this.source = source;
		this.target = target;
		this.member = member;
	}

	public Long getId() {
		return id;
	}

	public Station getSource() {
		return source;
	}

	public Station getTarget() {
		return target;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member addedMember) {
		if (Objects.nonNull(member)) {
			member.getFavorites().remove(this);
		}
		member = addedMember;
		member.getFavorites().add(this);
	}


	public boolean isMemberEqual(Member member) {
		return this.member.equals(member);
	}
}
