package nextstep.member.application;

import static nextstep.common.ErrorMsg.*;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.auth.JwtTokenProvider;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberAuthService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	public TokenResponse createMemberToken(TokenRequest tokenRequest) {
		Member findMember = memberRepository.findByEmail(tokenRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException(LOGIN_NOT_MATH_EMAIL.isMessage()));
		findMember.checkPassword(tokenRequest.getPassword());

		String token = jwtTokenProvider.createToken(tokenRequest.getEmail(), findMember.getRoles());
		return new TokenResponse(token);
	}
}
