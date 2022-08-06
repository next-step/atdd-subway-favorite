package nextstep.member.application;

import org.springframework.stereotype.Service;

import nextstep.auth.domain.AuthUser;
import nextstep.auth.service.CustomUserDetails;
import nextstep.member.domain.CustomAuthUser;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

@Service
public class CustomUserDetailsService implements CustomUserDetails {

	private MemberRepository memberRepository;

	public CustomUserDetailsService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Override
	public AuthUser loadUserByUsername(String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
		return CustomAuthUser.of(member);
	}
}
