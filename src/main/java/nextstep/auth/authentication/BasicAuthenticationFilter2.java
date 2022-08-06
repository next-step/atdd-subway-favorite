package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.service.CustomUserDetails;

public class BasicAuthenticationFilter2 extends AuthenticationInterceptor {
	private CustomUserDetails customUserDetails;

	public BasicAuthenticationFilter2(CustomUserDetails customUserDetails) {
		super(customUserDetails);
	}

	@Override
	public AuthenticationToken convert(HttpServletRequest request) throws Exception {
		String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
		String authHeader = new String(Base64.decodeBase64(authCredentials));

		String[] splits = authHeader.split(":");
		String principal = splits[0];
		String credentials = splits[1];

		return new AuthenticationToken(principal, credentials);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		try {
			AuthenticationToken authenticationToken = convert(request);
			Authentication authentication = super.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return true;
		} catch (Exception e) {
			return true;
		}
	}
}
