package nextstep.member.config.argument.resolver;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.dto.MemberInfoVo;
import nextstep.member.application.dto.abstractive.MemberProvider;
import nextstep.member.config.argument.annotation.MemberInfo;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class FindMemberOfMineRequestArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("authorization");
        MemberProvider memberOfMine = new MemberInfoVo(
                jwtTokenProvider.getPrincipal(token),
                jwtTokenProvider.getRoles(token));
        return memberOfMine;
    }
}
