package nextstep.member.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.command.EmailPasswordVerifier;
import nextstep.auth.domain.entity.TokenPrincipal;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberEmailPasswordVerifier implements EmailPasswordVerifier {
    private final MemberRepository memberRepository;

    @Override
    public TokenPrincipal verify(String email, String password) {
        Member member =  memberRepository.findByEmailOrThrow(email);
        member.verifyPassword(password);
        return member.toTokenPrincipal();
    }
}
