package nextstep.auth.authentication.provider;

import nextstep.auth.authentication.execption.AuthenticationException;
import nextstep.auth.context.Authentication;

// 실제 인증에 대한 부분을 처리함
public interface AuthenticationProvider {
    // 인증 전의 Authenticaion 객체를 받아서 인증된 Authentication 객체를 반환
    Authentication authenticate(Authentication authentication) throws AuthenticationException;

    boolean supports(Class<?> authentication);
}
