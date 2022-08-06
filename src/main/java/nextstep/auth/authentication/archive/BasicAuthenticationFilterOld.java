package nextstep.auth.authentication.archive;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

@Deprecated
public class BasicAuthenticationFilterOld implements HandlerInterceptor {
	private LoginMemberService loginMemberService;

	public BasicAuthenticationFilterOld(LoginMemberService loginMemberService) {
		this.loginMemberService = loginMemberService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
			String authHeader = new String(Base64.decodeBase64(authCredentials));

			String[] splits = authHeader.split(":");
			String principal = splits[0];
			String credentials = splits[1];

			AuthenticationToken token = new AuthenticationToken(principal, credentials);

			LoginMember loginMember = loginMemberService.loadUserByUsername(token.getPrincipal());
			if (loginMember == null) {
				throw new AuthenticationException();
			}

			if (!loginMember.checkPassword(token.getCredentials())) {
				throw new AuthenticationException();
			}

			Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authentication);

			return true;
		} catch (Exception e) {
			return true;
		}
	}
}
