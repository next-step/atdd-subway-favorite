package nextstep.auth.interceptor.nonchain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.user.UserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public abstract class NonChainFilter implements HandlerInterceptor {

	private final UserDetailsService userDetailsService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		MemberInfo info = createPrincipal(request);
		LoginMember loginMember = userDetailsService.loadUserByUsername(info.getEmail());

		validationMember(info, loginMember);

		afterValidation(response, loginMember);

		return false;
	}

	protected abstract MemberInfo createPrincipal(HttpServletRequest request);

	private void validationMember(MemberInfo info, LoginMember loginMember) {
		if (loginMember == null) {
			throw new AuthenticationException();
		}

		if (!loginMember.checkPassword(info.getPassword())) {
			throw new AuthenticationException();
		}
	}

	protected abstract void afterValidation(HttpServletResponse response, LoginMember loginMember) throws IOException;
}