package nextstep.auth.authentication.provider;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPasswordAuthenticationProvider implements AuthenticationProvider {

    private final LoginMemberService loginMemberService;

    @Override
    public boolean supports(ProviderType providerType) {
        return providerType.isUserPasswordType();
    }

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());
        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
        return new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
    }
}
