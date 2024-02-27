package subway.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dto.member.GithubProfileResponse;
import subway.dto.member.TokenResponse;

@Transactional(readOnly = true)
@Service
public class TokenService {
	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;
	private final GithubClient githubClient;

	public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider, GithubClient githubClient) {
		this.memberService = memberService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.githubClient = githubClient;
	}

	public TokenResponse createToken(String email, String password) {
		Member member = memberService.findMemberByEmail(email);
		if (!member.checkPassword(password)) {
			throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
		}

		String token = jwtTokenProvider.createToken(member.getEmail());

		return new TokenResponse(token);
	}

	public TokenResponse createTokenByGitHub(String code) {
		String accessToken = githubClient.requestGithubToken(code);
		GithubProfileResponse githubProfileResponse = githubClient.requestUser(accessToken);
		String token = jwtTokenProvider.createToken(githubProfileResponse.getEmail());
		return new TokenResponse(token);
	}
}
