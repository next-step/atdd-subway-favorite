package nextstep.member.application;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.member.domain.LoginMember;

public class CustomUserDetailServiceStrategy implements UserDetailsServiceStrategy{

    private final CustomUserDetailsService userDetailsService;

    public CustomUserDetailServiceStrategy(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public LoginMember authenticate(String principal, AuthenticationToken token) {
        final LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);
        return userDetails;
    }

    private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
