package nextstep.member.application;

import nextstep.auth.AuthMember;
import nextstep.error.exception.BusinessException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import static nextstep.error.exception.ErrorCode.MEMBER_NOT_EXISTS;
import static nextstep.error.exception.ErrorCode.MISMATCHED_PASSWORD;

@Service
public class MemberService {
	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public MemberResponse createMember(MemberRequest request) {
		Member member = memberRepository.save(request.toMember());
		return MemberResponse.of(member);
	}

	public MemberResponse findMember(Long id) {
		Member member = memberRepository.findById(id).orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS));
		return MemberResponse.of(member);
	}

	public void updateMember(Long id, MemberRequest param) {
		Member member = memberRepository.findById(id).orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS));
		member.update(param.toMember());
	}

	public void deleteMember(Long id) {
		memberRepository.findById(id).orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS));
		memberRepository.deleteById(id);
	}

	public Member findMemberByEmailAndPassword(String email, String password) {
		return memberRepository.findByEmail(email)
				.filter(it -> it.checkPassword(password))
				.orElseThrow(() -> new BusinessException(MISMATCHED_PASSWORD));
	}

	public MemberResponse findMemberByEmail(String email) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS));
		return MemberResponse.of(member);
	}

	public MemberResponse findMemberByGithubEmailOrElseCreateMember(String id, String email) {
		Member member = memberRepository.findByEmail(email)
				.orElse(memberRepository.save(new Member(email, id, null)));
		return MemberResponse.of(member);
	}

	public AuthMember findAuthMemberByEmail(String email) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS));
		return AuthMember.of(member);
	}
}