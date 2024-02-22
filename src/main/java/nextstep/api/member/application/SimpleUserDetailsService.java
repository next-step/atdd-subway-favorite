package nextstep.api.member.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.domain.dto.UserPrincipal;
import nextstep.api.auth.domain.service.UserDetailsService;
import nextstep.api.member.domain.MemberRepository;
import nextstep.common.exception.member.MemberNotFoundException;

/**
 * @author : Rene Choi
 * @since : 2024/02/20
 */
@Service
@RequiredArgsConstructor
public class SimpleUserDetailsService implements UserDetailsService {
	private final MemberRepository memberRepository;

	@Override
	public Optional<UserPrincipal> loadUserByEmailOptional(String email) {
		return memberRepository.findByEmail(email).map(UserPrincipal::from);
	}

	@Override
	public UserPrincipal loadUserByEmail(String email) {
		return memberRepository.findByEmail(email).map(UserPrincipal::from).orElseThrow(MemberNotFoundException::new);
	}
}
