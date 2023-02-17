package nextstep.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.exception.AuthException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

@Service
public class AuthService {

	private final JwtTokenProvider jwtTokenProvider;

	private final MemberRepository memberRepository;

	public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.memberRepository = memberRepository;
	}

	@Transactional(readOnly = true)
	public TokenResponse createToken(TokenRequest tokenRequest) {
		Member member = memberRepository.findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword())
			.orElseThrow(AuthException::new);

		return new TokenResponse(jwtTokenProvider.createToken(member.getEmail(), member.getRoles()));
	}
}
