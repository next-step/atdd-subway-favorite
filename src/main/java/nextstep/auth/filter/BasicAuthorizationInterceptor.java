package nextstep.auth.filter;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BasicAuthorizationInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final String authorization = request.getHeader("Authorization");
        if (Objects.isNull(authorization) || !getAuthorizationType(authorization).equals("Basic")) {
            return true;
        }
        final String token = authorization.split(" ")[1];
        final byte[] bytes = Base64.getDecoder().decode(token);
        final String principal = new String(bytes).split(":")[0];
        final Member member = memberService.getMember(principal);
        request.setAttribute("user", member);
        return true;
    }

    private String getAuthorizationType(final String authorizationHeader) {
        return authorizationHeader.split(" ")[0];
    }
}
