package nextstep.auth.ui;

import nextstep.common.exception.AuthorizationException;
import nextstep.common.config.JwtTokenProvider;
import nextstep.auth.domain.AppMember;
import nextstep.auth.domain.AuthenticationMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AppMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TYPE = "Bearer ";

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AppMemberArgumentResolver(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = extractToken(webRequest.getHeader(AUTHORIZATION));
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException();
        }

        final String email = jwtTokenProvider.getPrincipal(token);
        final Member member = memberRepository.findByEmail(email).orElseThrow(AuthorizationException::new);
        return new AppMember(member.getId(), member.getEmail(), member.getAge());
    }

    private String extractToken(String headerValue) {
        if (headerValue == null || !isBearerType(headerValue)) {
            throw new AuthorizationException();
        }

        return headerValue.substring(BEARER_TYPE.length());
    }

    private static boolean isBearerType(String headerValue) {
        return headerValue.startsWith(BEARER_TYPE);
    }
}
