package nextstep.member.domain.command;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.command.authenticator.AuthenticateSocialOAuthCommand;
import nextstep.member.domain.command.authenticator.SocialOAuthAuthenticator;
import nextstep.member.domain.command.authenticator.SocialOAuthUser;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.entity.TokenPrincipal;
import nextstep.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenCommander {
    private final TokenGenerator tokenGenerator;
    private final MemberRepository memberRepository;
    private final SocialOAuthAuthenticator socialOAuthAuthenticator;

    public String createToken(String email, String password) {
        Member member =  memberRepository.findByEmailOrThrow(email);
        member.verifyPassword(password);
        return tokenGenerator.createToken(TokenPrincipal.of(member));
    }

    @Transactional
    public String authenticateSocialOAuth(AuthenticateSocialOAuthCommand.ByAuthCode command) {
        SocialOAuthUser socialOAuthUser = socialOAuthAuthenticator.authenticate(command);
        Member member = memberRepository.findByEmail(socialOAuthUser.getEmail())
                .orElseGet(() -> memberRepository.save(Member.of(socialOAuthUser)));
        return tokenGenerator.createToken(TokenPrincipal.of(member));
    }
}
