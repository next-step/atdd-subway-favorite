package nextstep.member.application;

import org.springframework.stereotype.Service;

import nextstep.member.application.dto.GithubAccessTokenRequest;
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

	public LoginService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.memberRepository = memberRepository;
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

	public TokenResponse createTokenByGithubToken(GithubAccessTokenRequest request) {
		//TODO 1. github에 권한증서로 github AccessToken 획득

		//TODO 2. github에 github AccessToken을 통해 회원 정보 획득

		//TODO 3. 회원 정보 조회

		//TODO 3-1. (Optional) 회원 정보가 없을 시, 회원 생성 후 회원 정보 조회

		//TODO 4. 회원 정보를 통해 토큰 생성

		//TODO 5. TokenResponse 반환
		return null;
	}
}
