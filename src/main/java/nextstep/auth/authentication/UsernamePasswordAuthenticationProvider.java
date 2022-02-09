package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();

        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    @Override
    public boolean supports(Class<?> authenticationToken) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authenticationToken);
    }

    private void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }

}
