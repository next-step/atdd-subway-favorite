package nextstep.auth.authentication;

import nextstep.auth.authentication.execption.AuthenticationException;
import nextstep.auth.context.Authentication;

// AuthenticationManager 에 등록된 AuthenticationProvider  에 의해서 처리됨
public interface AuthenticationManager {
    // 인증이 성고하면 객체를 생성하여, Security Context 에 저장함. 실패시 AuthenticationException
    Authentication authenticate(Authentication authentication) throws AuthenticationException;
}
