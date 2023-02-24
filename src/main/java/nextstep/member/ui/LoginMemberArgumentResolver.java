package nextstep.member.ui;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private JwtTokenProvider jwtTokenProvider;
    private MemberRepository memberRepository;

    public LoginMemberArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];

        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다. token: " + token);
        }

        String email = jwtTokenProvider.getPrincipal(token);
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new InvalidTokenException("유효하지 않은 토큰입니다. token: " + token));

        return new LoginMember(member.getId());
    }
}
