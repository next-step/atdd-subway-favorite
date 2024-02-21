package subway.member;

import org.springframework.stereotype.Service;

import subway.dto.member.TokenResponse;

@Service
public class TokenService {
	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;

	public TokenService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
		this.memberService = memberService;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public TokenResponse createToken(String email, String password) {
		Member member = memberService.findMemberByEmail(email);
		if (!member.getPassword().equals(password)) {
			throw new AuthenticationException();
		}

		String token = jwtTokenProvider.createToken(member.getEmail());

		return new TokenResponse(token);
	}
}
