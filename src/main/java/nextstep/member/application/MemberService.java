package nextstep.member.application;

import static nextstep.common.ErrorMsg.*;

import nextstep.auth.AuthMember;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberRepository memberRepository;

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

	public MemberResponse findMemberByToken(AuthMember authMember) {
		Member findMember = memberRepository.findByEmail(authMember.getEmail()).orElseThrow(() -> new IllegalArgumentException(LOGIN_NOT_MATH_EMAIL.isMessage()));
		return MemberResponse.of(findMember);
	}

	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException(LOGIN_NOT_MATH_EMAIL.isMessage()));
	}

	public Member findByEmailOrCreateMember(String email) {
		return memberRepository.findByEmail(email)
			.orElseGet(() -> memberRepository.save(new Member(email, null, null)));
	}
}
