package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LoginMemberService {
    private final MemberRepository memberRepository;

    public LoginMember loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(NoSuchElementException::new);
        return LoginMember.of(member);
    }
}
