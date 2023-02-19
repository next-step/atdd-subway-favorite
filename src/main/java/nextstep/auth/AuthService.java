package nextstep.auth;

import static nextstep.common.ErrorMsg.*;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public AuthResponse createToken(AuthRequest authRequest) {
		Member findMember = memberRepository.findByEmail(authRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException(LOGIN_NOT_MATH_EMAIL.isMessage()));
		findMember.checkPassword(authRequest.getPassword());

		String token = jwtTokenProvider.createToken(authRequest.getEmail(), findMember.getRoles());
		return new AuthResponse(token);
	}
}
