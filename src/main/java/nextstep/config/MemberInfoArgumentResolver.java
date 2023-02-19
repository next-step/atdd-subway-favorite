package nextstep.config;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import nextstep.exception.NotFoundMemberException;
import nextstep.member.annotation.MemberInfo;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.MemberInfoDto;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class MemberInfoArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthorizationExtractor authorizationExtractor;

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberInfoDto.class) &&
            parameter.hasParameterAnnotation(MemberInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest httpServletRequest = Objects.requireNonNull(
            webRequest.getNativeRequest(HttpServletRequest.class));

        String token = authorizationExtractor.extract(httpServletRequest, "Bearer");

        String principal = jwtTokenProvider.getPrincipal(token);

        Member member = memberRepository.findByEmail(principal)
            .orElseThrow(NotFoundMemberException::new);

        return MemberInfoDto.of(member);
    }
}
