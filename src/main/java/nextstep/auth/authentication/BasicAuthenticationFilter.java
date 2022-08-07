package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;

import nextstep.auth.context.Authentication;
import nextstep.auth.domain.CustomUser;
import nextstep.auth.service.CustomUserDetails;

public class BasicAuthenticationFilter extends AuthenticationChainInterceptor {

	private CustomUserDetails customUserDetails;

	public BasicAuthenticationFilter(CustomUserDetails customUserDetails) {
		this.customUserDetails = customUserDetails;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			AuthenticationToken authenticationToken = convert(request);
			Authentication authentication = authenticate(authenticationToken);
			super.afterAuthenticate(authentication);
			return true;
		} catch (Exception e) {
			return true;
		}
	}

	private AuthenticationToken convert(HttpServletRequest request) {
		String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
		String authHeader = new String(Base64.decodeBase64(authCredentials));

		String[] splits = authHeader.split(":");
		String principal = splits[0];
		String credentials = splits[1];

		return new AuthenticationToken(principal, credentials);
	}

	private Authentication authenticate(AuthenticationToken authenticationToken) {
		CustomUser customUser = customUserDetails.loadUserByEmail(authenticationToken.getPrincipal());
		if (customUser == null) {
			throw new AuthenticationException();
		}
		if (!customUser.isValidPassword(authenticationToken.getCredentials())) {
			throw new AuthenticationException();
		}
		return new Authentication(authenticationToken.getPrincipal(), customUser.getAuthorities());
	}
}
