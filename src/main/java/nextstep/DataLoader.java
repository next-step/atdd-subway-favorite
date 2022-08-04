package nextstep;

import java.util.List;

import org.springframework.stereotype.Component;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;

@Component
public class DataLoader {
    private final String ADMIN_PASSWORD = "password";
    private final String ADMIN_EMAIL = "admin@email.com";
    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member(ADMIN_EMAIL, ADMIN_PASSWORD, 20, List.of(RoleType.ROLE_ADMIN.name())));
    }
}
