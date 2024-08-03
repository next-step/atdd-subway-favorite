package nextstep.member.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.entity.TokenPrincipal;
import nextstep.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenCommander {
    private final TokenGenerator tokenGenerator;
    private final MemberRepository memberRepository;

    public String createToken(String email, String password) {
        Member member =  memberRepository.findByEmailOrThrow(email);
        member.verifyPassword(password);
        return tokenGenerator.createToken(new TokenPrincipal(member.getId(), member.getEmail()));
    }
}
