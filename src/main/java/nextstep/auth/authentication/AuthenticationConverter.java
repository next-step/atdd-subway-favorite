package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.application.UserDetailsServiceStrategy;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AuthenticationConverter {
    AuthenticationToken convert(HttpServletRequest request) throws IOException;

//    default Authentication authenticate(AuthenticationToken token, UserDetailsServiceStrategy userDetailsService) {
//        String principal = token.getPrincipal();
//        final LoginMember userDetails = userDetailsService.authenticate(principal, token);
//        return new Authentication(userDetails);
//    }
}
