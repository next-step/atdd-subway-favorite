package nextstep.member.application;

import org.springframework.stereotype.Service;

import nextstep.member.application.dto.AuthUser;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.ErrorMessage;
import nextstep.member.exception.NotFoundException;

@Service
public class MemberService {
	private MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public MemberResponse createMember(MemberRequest request) {
		final Member member = memberRepository.save(request.toMember());
		return MemberResponse.of(member);
	}

	public MemberResponse findMemberById(Long id) {
		final Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		return MemberResponse.of(member);
	}

	public void updateMember(Long id, MemberRequest param) {
		final Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		member.update(param.toMember());
	}

	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	public MemberResponse findMemberByToken(AuthUser authUser) {
		final Member member = findMemberByEmail(authUser.getEmail());
		return MemberResponse.of(member);
	}

	private Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_MEMBER_BY_EMAIL));
	}
}
