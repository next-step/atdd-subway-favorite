package nextstep.auth.application;

import lombok.AllArgsConstructor;
import nextstep.auth.infrastructure.token.TokenGenerator;
import nextstep.auth.application.dto.EmailPasswordAuthRequest;
import nextstep.auth.dto.TokenResponse;
import nextstep.auth.exception.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailPasswordAuthenticationService {

    private final MemberService memberService;
    private final TokenGenerator tokenGenerator;

    public TokenResponse authenticate(EmailPasswordAuthRequest request) {

        Member member = memberService.findMemberByEmail(request.getEmail());
        if (!member.getPassword().equals(request.getPassword())) {
            throw new AuthenticationException();
        }
        String token = tokenGenerator.createToken(member.getEmail());

        return new TokenResponse(token);
    }
}
