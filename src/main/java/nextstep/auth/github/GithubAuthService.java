package nextstep.auth.github;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.auth.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;

@Service
@RequiredArgsConstructor
public class GithubAuthService {

	private final MemberService memberService;
	private final GithubClient githubClient;
	private final JwtTokenProvider jwtTokenProvider;

	public TokenResponse loginByGithub(GithubLoginRequest githubAccessTokenRequest) {
		String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(githubAccessTokenRequest.getCode());
		GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);
		Member member = memberService.findByEmailOrCreateMember(githubProfile.getEmail());

		String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
		return new TokenResponse(token);
	}
}
