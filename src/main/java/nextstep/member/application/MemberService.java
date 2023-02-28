package nextstep.member.application;

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
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotExistsMemberException("존재하지 않는 아이디. id: " + id));
        return MemberResponse.of(member);
    }

    public Member login(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new NotExistsMemberException("존재하지 않는 이메일. email: " + email));

        if (!member.checkPassword(password)) {
            throw new PasswordMismatchException("올바르지 않은 비밀번호");
        }

        return member;
    }

    public Member findByEmailOrCreateMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email, "", 0)));
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
