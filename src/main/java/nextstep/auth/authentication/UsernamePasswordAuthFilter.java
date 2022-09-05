package nextstep.auth.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.UserDetail;
import nextstep.auth.user.UserDetailService;
import nextstep.common.interceptor.NotProgressInterceptor;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequiredArgsConstructor
public class UsernamePasswordAuthFilter extends NotProgressInterceptor {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private final UserDetailService loginMemberService;

    @Override
    protected void execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String username = paramMap.get(USERNAME_FIELD)[0];
        String password = paramMap.get(PASSWORD_FIELD)[0];

        AuthenticationToken token = new AuthenticationToken(username, password);

        String principal = token.getPrincipal();
        UserDetail loginMember = loginMemberService.loadUserByUsername(principal);

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
