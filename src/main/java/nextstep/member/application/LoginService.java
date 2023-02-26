package nextstep.member.application;

import org.springframework.stereotype.Service;

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
}
