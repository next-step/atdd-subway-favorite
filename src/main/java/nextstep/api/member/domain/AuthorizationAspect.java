package nextstep.api.member.domain;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import nextstep.api.auth.domain.dto.UserPrincipal;
import nextstep.common.exception.auth.AuthorizationException;

/**
 * @author : Rene Choi
 * @since : 2024/02/13
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

	private final MemberRepository memberRepository;

	@Pointcut(value = "@annotation(nextstep.common.annotation.PreAuthorize) && args(loginMember, id,..)", argNames = "loginMember,id")
	public void methodSecured(UserPrincipal loginMember, Long id) {
	}

	@Before(value = "methodSecured(loginMember, id)", argNames = "loginMember,id")
	public void authorize(UserPrincipal loginMember, Long id) throws AuthorizationException {
		Member member = memberRepository.findByEmail(loginMember.getEmail())
			.orElseThrow(() -> new AuthorizationException("Member not found"));

		if (member.isNot(id)) {
			throw new AuthorizationException("Unauthorized access");
		}
	}
}