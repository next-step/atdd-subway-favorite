package nextstep.api.auth.application.facade;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.application.dto.GithubLoginRequest;
import nextstep.api.auth.application.dto.TokenRequest;
import nextstep.api.auth.application.dto.TokenResponse;
import nextstep.api.auth.domain.dto.outport.OAuthUserInfo;
import nextstep.api.auth.domain.service.AuthService;
import nextstep.api.auth.domain.service.impl.TokenService;
import nextstep.api.member.application.MemberService;
import nextstep.api.member.domain.Member;
import nextstep.common.exception.member.AuthenticationException;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
@Component
@RequiredArgsConstructor
public class AuthFacade {

	private final AuthService authService;
	private final MemberService memberService;
	private final TokenService tokenService;

	public TokenResponse githubLogin(GithubLoginRequest loginRequest) {
		OAuthUserInfo oauthUser = authService.authenticateWithGithub(loginRequest.getCode());

		Member member = memberService.findMemberByEmailOptional(oauthUser.getEmail())
			.orElseGet(() -> memberService.registerNewMember(oauthUser));

		return tokenService.createToken(member.getEmail());
	}

	public TokenResponse createToken(TokenRequest request) {
		Member member = memberService.findMemberByEmail(request.getEmail());
		if (!member.getPassword().equals(request.getPassword())) {
			throw new AuthenticationException();
		}

		return tokenService.createToken(request.getEmail());
	}
}
