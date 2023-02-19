package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberNotFoundException;
import nextstep.member.exception.MemberAuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member verificateAndFindMember(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if (!member.checkPassword(password)) {
            throw new MemberAuthenticationException();
        }

        return member;

    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Member findOrCreateMember(String email) {
        Member member = memberRepository.findByEmail(email).orElse(new Member(email));
        return memberRepository.save(member);
    }
}
