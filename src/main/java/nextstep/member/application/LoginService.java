package nextstep.member.application;

import org.springframework.stereotype.Service;

import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubLoginRequest;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.ErrorMessage;
import nextstep.member.exception.NotFoundException;

@Service
public class LoginService {
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;
	private final GithubClient githubClient;

	public LoginService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository,
		GithubClient githubClient) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.memberRepository = memberRepository;
		this.githubClient = githubClient;
	}

	public TokenResponse createToken(TokenRequest request) {
		final Member member = findMemberByEmail(request.getEmail());
		member.validPassword(request.getPassword());

		final String accessToken = jwtTokenProvider.createToken(request.getEmail(), member.getRoles());

		return TokenResponse.of(accessToken);
	}

	private Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_MEMBER_BY_EMAIL));
	}

	private Member findMemberByEmailOrElseCreate(String email) {
		return memberRepository.findByEmail(email)
			.orElse(memberRepository.save(Member.ofCreatedByGithub(email)));
	}

	public TokenResponse createTokenByGithubToken(GithubLoginRequest request) {
		GithubAccessTokenResponse accessToken = githubClient.getAccessTokenFromGithub(request.getCode());
		GithubProfileResponse githubProfileResponse = githubClient.getGithubProfileFromGithub(
			accessToken.getAccessToken());
		Member member = findMemberByEmailOrElseCreate(githubProfileResponse.getEmail());
		String token = jwtTokenProvider.createToken(member.getEmail(), member.getRoles());
		return TokenResponse.of(token);
	}
}
