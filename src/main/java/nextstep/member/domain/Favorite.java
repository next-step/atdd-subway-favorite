package nextstep.member.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.subway.domain.BaseEntity;

@Entity
public class Favorite extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long memberId;

	private Long sourceId;

	private Long targetId;

	public Favorite() {
	}

	public Favorite(Long memberId, Long sourceId, Long targetId) {
		this.memberId = memberId;
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

	public Long getId() {
		return id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public Long getSource() {
		return sourceId;
	}

	public Long getTarget() {
		return targetId;
	}

	public void validateMember(Long memberId) {
		if(!this.memberId.equals(memberId)) {
			throw new AuthenticationException();
		}
	}
}
