package nextstep.member.domain;

import nextstep.subway.domain.SubwayMember;

public class SubWayMember implements SubwayMember {
	private Long memberId;
	private String email;

	public SubWayMember(Long memberId, String email) {
		this.memberId = memberId;
		this.email = email;
	}

	public static SubWayMember of(Member member) {
		return new SubWayMember(member.getId(), member.getEmail());
	}

	@Override
	public Long getMemberId() {
		return this.memberId;
	}
}
