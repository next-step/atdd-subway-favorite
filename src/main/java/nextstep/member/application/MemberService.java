package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.error.exception.BusinessException;
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
		Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		return MemberResponse.of(member);
	}

	public void updateMember(Long id, MemberRequest param) {
		Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		member.update(param.toMember());
	}

	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	public Member findMemberByEmailAndPassword(String email, String password) {
		return memberRepository.findByEmail(email).filter(it -> it.checkPassword(password)).orElseThrow(() -> new BusinessException(MISMATCHED_PASSWORD));
	}

	public MemberResponse findMemberOfMine(String email) {
		return MemberResponse.of(memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS)));
	}
}