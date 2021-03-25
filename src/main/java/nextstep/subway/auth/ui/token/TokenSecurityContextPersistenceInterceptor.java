package nextstep.subway.auth.ui.token;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.SecurityContextInterceptor;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

	private final JwtTokenProvider jwtTokenProvider;
	private final ObjectMapper objectMapper;

	public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider,
		ObjectMapper objectMapper) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.objectMapper = objectMapper;
	}

	@Override
	protected SecurityContext extractSecurityContext(HttpServletRequest request) {
		String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
		if (!jwtTokenProvider.validateToken(credentials)) {
			return null;
		}

		try {
			String payload = jwtTokenProvider.getPayload(credentials);
			TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
			};

			Map principal = objectMapper.readValue(payload, typeRef);
			return new SecurityContext(new Authentication(principal));
		} catch (Exception e) {
			return null;
		}
	}
}
