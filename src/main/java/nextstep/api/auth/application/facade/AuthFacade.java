package nextstep.api.auth.application.facade;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.application.dto.GithubLoginRequest;
import nextstep.api.auth.application.dto.TokenRequest;
import nextstep.api.auth.application.dto.TokenResponse;
import nextstep.api.auth.domain.dto.UserPrincipal;
import nextstep.api.auth.domain.service.AuthService;
import nextstep.api.auth.domain.service.impl.TokenService;
import nextstep.api.user.OAuthUserRegistrationRequest;
import nextstep.api.user.OAuthUserRegistrationService;
import nextstep.api.user.UserDetailsService;
import nextstep.common.exception.member.AuthenticationException;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
@Component
@RequiredArgsConstructor
public class AuthFacade {

	private final AuthService authService;
	private final UserDetailsService userDetailsService;
	private final TokenService tokenService;
	private final OAuthUserRegistrationService oAuthUserRegistrationService;

	public TokenResponse githubLogin(GithubLoginRequest loginRequest) {
		UserPrincipal oauthUser = authService.authenticateWithGithub(loginRequest.getCode());

		UserPrincipal member = userDetailsService.loadUserByEmailOptional(oauthUser.getEmail())
			.orElseGet(() -> UserPrincipal.from(oAuthUserRegistrationService.registerOAuthUser(OAuthUserRegistrationRequest.of(oauthUser.getEmail()))));

		return tokenService.createToken(member.getEmail());
	}

	public TokenResponse createToken(TokenRequest request) {
		UserPrincipal member = userDetailsService.loadUserByEmail(request.getEmail());
		if (!member.getPassword().equals(request.getPassword())) {
			throw new AuthenticationException();
		}

		return tokenService.createToken(request.getEmail());
	}
}
