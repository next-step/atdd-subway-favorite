package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_ADMIN;

@Component
public class DataLoader {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE, List.of(ROLE_ADMIN.name())));
    }
}
