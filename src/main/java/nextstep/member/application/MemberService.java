package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    public static final String MEMBER_NOT_FOUND_MESSAGE = "회원을 찾을 수 없습니다.";

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

    public Member findMemberByEmailOrThrow(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_FOUND_MESSAGE));
    }

    public Member findMemberByEmailOrNull(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }


    public MemberResponse findMe(LoginMember loginMember) {
        Member member = findMemberByEmailOrThrow(loginMember.getEmail());
        return MemberResponse.of(member);
    }
}