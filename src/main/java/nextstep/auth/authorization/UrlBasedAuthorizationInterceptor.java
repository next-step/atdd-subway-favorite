package nextstep.auth.authorization;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.domain.LoginMember;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UrlBasedAuthorizationInterceptor extends SecurityContextInterceptor {

	private final List<URI> authorizedUrlsMatcher = new ArrayList<>();
	private final AntPathMatcher antPathMatcher;

	public UrlBasedAuthorizationInterceptor(List<URI> authorizedUrls) {
		antPathMatcher = new AntPathMatcher();
		authorizedUrlsMatcher.addAll(authorizedUrls);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!isMatch(getRequestPath(request))) {
			return true;
		}
		if (isAuthorized(authentication)) {
			return true;
		}
		responseWithUnauthorized(response);
		return false;
	}

	private boolean isAuthorized(Authentication authentication) {
		if (Objects.isNull(authentication) || Objects.isNull(authentication.getPrincipal())) {
			return false;
		}
		return authentication.getPrincipal() instanceof LoginMember;
	}

	private void responseWithUnauthorized(HttpServletResponse response) {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}

	private String getRequestPath(HttpServletRequest request) {
		return request.getServletPath();
	}

	public boolean isMatch(String inspectedUri) {
		return authorizedUrlsMatcher
				.stream()
				.anyMatch(uri -> isMatch(inspectedUri, uri));
	}

	private boolean isMatch(String inspectedUri, URI uri) {
		return antPathMatcher.match(uri.getPath(), inspectedUri);
	}
}
