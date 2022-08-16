package nextstep.auth.authentication.provider;

import nextstep.auth.authentication.execption.AuthenticationException;
import nextstep.auth.authentication.token.BasicAuthenticationToken;
import nextstep.auth.authentication.token.UsernamePasswordAuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.domain.LoginMember;

public class BasicAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;

    public BasicAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BasicAuthenticationToken token = (BasicAuthenticationToken) authentication;

        String username = (String) token.getPrincipal();
        UserDetails user = userDetailsService.loadUserByUsername(username);


        if (user == null) {
            throw new AuthenticationException();
        }

        checkAuthentication(user, token);

        return new BasicAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BasicAuthenticationToken.class);
    }

    private void checkAuthentication(UserDetails userDetails, BasicAuthenticationToken token) {
        // 조회가 null 이면 인증 오류
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        // 비밀번호를 체크했으나 오렴
        if (!userDetails.checkCredentials(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}