package nextstep.auth.authentication.provider;

import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.execption.AuthenticationException;
import nextstep.auth.authentication.token.UsernamePasswordAuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;

// provider 는 authentication 를 받고, 올바른 인증이 맞는지 확인함. userDetailService 를 사용함
public class UserDetailsAuthenticationProvider implements AuthenticationManager {
    private final UserDetailsService userDetailsService;

    public UserDetailsAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 인증이 완료 될 경우 인증된 객체 반환. 그렇지 않을경우 AuthenticationException
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 인증 제공자에 인증 통과
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // 인증 토큰으로부터 아이디와 비밀번호를 조회함. => UserDetailsService 인터페이스로부터 아이디로 사용자 조회.
        String principal = (String) token.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);

        // 인증 체크
        checkAuthentication(userDetails, token);

        // 인증 완료된 토큰을 반환
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    // 인증 체크
    private void checkAuthentication(UserDetails userDetails, UsernamePasswordAuthenticationToken token) {
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
