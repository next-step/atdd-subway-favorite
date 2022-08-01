package nextstep;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;

@Component
public class DataLoader {
	private MemberRepository memberRepository;

	public DataLoader(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public void loadData() {
		memberRepository.save(
			new Member("admin@email.com", "password", 20
				, Arrays.asList(RoleType.ROLE_ADMIN.name())));
	}
}
