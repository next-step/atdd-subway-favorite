package nextstep.member.application;

import org.springframework.stereotype.Service;

import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.SubWayMember;
import nextstep.subway.applicaion.SubwayMemberDetails;

@Service
public class SubwayMemberDetailsService implements SubwayMemberDetails {
	private MemberRepository memberRepository;

	public SubwayMemberDetailsService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Override
	public SubWayMember getMemberIdByEmail(String email) {
		return SubWayMember.of(memberRepository.findByEmail(email)
			.orElseThrow(RuntimeException::new));
	}
}
