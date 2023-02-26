package nextstep.member.application.config;

import nextstep.auth.application.JwtTokenProvider;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberRestApiException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthMemberArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isRegUserAnnotation = parameter.getParameterAnnotation(AuthMember.class) != null;

        return isRegUserAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            Object accessToken = webRequest.getAttribute("accessToken", 0);

            String email = jwtTokenProvider.getPrincipal(String.valueOf(accessToken));

            return memberRepository.findByEmail(email).orElseThrow(MemberRestApiException::new);
        } catch (Exception e) {
            throw new MemberRestApiException("인증 요청값 accessToken이 문자열인지 확인 필요");
        }
    }
}
