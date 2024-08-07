package nextstep.member.ui;

import nextstep.member.exception.AuthenticationException;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");

        if (authorization == null) {
            throw new AuthenticationException();
        }

        String[] authHeader = authorization.split(" ");

        if (!"bearer".equalsIgnoreCase(authHeader[0]) || authHeader.length != 2) {
            throw new AuthenticationException();
        }

        String token = authHeader[1];

        if (jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getPrincipal(token);
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(AuthenticationException::new);

            return new LoginMember(member.getId(), member.getEmail());
        }

        throw new AuthenticationException();
    }
}
