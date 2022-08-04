package nextstep.auth.interceptor.nonchain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
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
		UserDetails userDetails = userDetailsService.loadUserByUsername(info.getEmail());

		validationMember(info, userDetails);

		afterValidation(response, userDetails);

		return false;
	}

	protected abstract MemberInfo createPrincipal(HttpServletRequest request);

	private void validationMember(MemberInfo info, UserDetails userDetails) {
		if (userDetails == null) {
			throw new AuthenticationException();
		}

		if (!userDetails.checkPassword(info.getPassword())) {
			throw new AuthenticationException();
		}
	}

	protected abstract void afterValidation(HttpServletResponse response, UserDetails userDetails) throws IOException;
}