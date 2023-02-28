package nextstep.auth.argumentResolver;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER_PREFIX = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return bearerResolver(authorization);
    }

    private MemberResponse bearerResolver(String authorization) {
        if (authorization == null || !BEARER_PREFIX.equals(authorization.split(" ")[0])) {
            throw new RuntimeException();
        }

        String accessToken = authorization.split(" ")[1];

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new IllegalArgumentException(accessToken);
        }
        String email = jwtTokenProvider.getPrincipal(accessToken);

        MemberResponse memberResponse = MemberResponse.of(memberService.findByEmail(email));

        return memberResponse;
    }
}
