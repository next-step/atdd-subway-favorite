package nextstep.member.application.config;

import nextstep.auth.application.JwtTokenProvider;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.FavoriteRestApiException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthFavoriteArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthFavoriteArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isRegUserAnnotation = parameter.getParameterAnnotation(AuthFavorite.class) != null;

        return isRegUserAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object accessToken = webRequest.getAttribute("accessToken", 0);

        String email = jwtTokenProvider.getPrincipal(String.valueOf(accessToken));
        return memberRepository.findByEmail(email).orElseThrow(FavoriteRestApiException::new);
    }
}
