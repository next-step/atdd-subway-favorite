package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException(email));
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member authenticate(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException(email));
        verifyPassword(password, member);

        return member;
    }

    private static void verifyPassword(String password, Member member) {
        if (!member.checkPassword(password)) {
            throw new WrongPasswordException(password);
        }
    }

    @Transactional
    public Member findMemberOrElseJoin(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            return memberRepository.save(new Member(email, "임시 비밀번호", 0));
        }

        return member.get();
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));
    }
}