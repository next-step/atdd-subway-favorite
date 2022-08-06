package nextstep.auth.authentication.archive;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

@Deprecated
public class UsernamePasswordAuthenticationFilterOld implements HandlerInterceptor {
	private static final String PRINCIPAL_NAME = "username";
	private static final String CREDENTIAL_NAME = "password";

	private LoginMemberService loginMemberService;

	public UsernamePasswordAuthenticationFilterOld(LoginMemberService loginMemberService) {
		this.loginMemberService = loginMemberService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		try {
			Map<String, String[]> parameterMap = request.getParameterMap();
			String userName = parameterMap.get(PRINCIPAL_NAME)[0];
			String password = parameterMap.get(CREDENTIAL_NAME)[0];

			AuthenticationToken token = new AuthenticationToken(userName, password);

			LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());

			if (loginMember == null) {
				throw new AuthenticationException();
			}

			if (!loginMember.checkPassword(token.getCredentials())) {
				throw new AuthenticationException();
			}

			Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			return false;
		} catch (Exception e) {
			return true;
		}

	}
}
