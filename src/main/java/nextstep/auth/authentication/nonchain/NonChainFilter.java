package nextstep.auth.authentication.nonchain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public abstract class NonChainFilter implements HandlerInterceptor {

	private final LoginMemberService loginMemberService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		MemberInfo info = createPrincipal(request);
		LoginMember loginMember = loginMemberService.loadUserByUsername(info.getEmail());

		validationMember(info, loginMember);

		afterValidation(response, loginMember);

		return false;
	}

	private void validationMember(MemberInfo info, LoginMember loginMember) {
		if (loginMember == null) {
			throw new AuthenticationException();
		}

		if (!loginMember.checkPassword(info.getPassword())) {
			throw new AuthenticationException();
		}
	}

	protected abstract MemberInfo createPrincipal(HttpServletRequest request);

	protected abstract void afterValidation(HttpServletResponse response, LoginMember loginMember) throws IOException;
}
