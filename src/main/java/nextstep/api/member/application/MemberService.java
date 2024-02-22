package nextstep.api.member.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.api.member.application.dto.MemberRequest;
import nextstep.api.member.application.dto.MemberResponse;
import nextstep.api.member.domain.LoginMember;
import nextstep.api.member.domain.Member;
import nextstep.api.member.domain.MemberRepository;
import nextstep.common.annotation.PreAuthorize;
import nextstep.common.exception.member.MemberNotFoundException;

@Service
@RequiredArgsConstructor
public class MemberService implements OAuthUserRegistrationService {
	private final MemberRepository memberRepository;

	public MemberResponse createMember(MemberRequest request) {
		Member member = memberRepository.save(request.toMember());
		return MemberResponse.of(member);
	}

	@PreAuthorize
	public MemberResponse findMember(LoginMember loginMember, Long id) {
		Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		return MemberResponse.of(member);
	}

	@PreAuthorize
	public void updateMember(LoginMember loginMember, Long id, MemberRequest param) {
		Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		member.update(param.toMember());
	}

	@PreAuthorize
	public void deleteMember(LoginMember loginMember, Long id) {
		memberRepository.deleteById(id);
	}


	public MemberResponse findMe(LoginMember loginMember) {
		return memberRepository.findByEmail(loginMember.getEmail())
			.map(MemberResponse::of)
			.orElseThrow(MemberNotFoundException::new);
	}

	@Override
	public Member registerOAuthUser(OAuthUserRegistrationRequest oAuthUserRegistrationRequest) {
		return memberRepository.save(new Member(oAuthUserRegistrationRequest.getEmail()));
	}
}