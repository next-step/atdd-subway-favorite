package nextstep.member.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.configuration.error.AuthenticationException;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenGenerator tokenGenerator;
    private final MemberRepository memberRepository;

    public String createToken(String email, String password) {
        Member member =  memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        if (!member.getPassword().equals(password)) {
            throw new AuthenticationException();
        }

        return tokenGenerator.createToken(member.getEmail());
    }
}
