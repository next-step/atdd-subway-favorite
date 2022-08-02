package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader {

    public static final String ADMIN_EMAIL = "email@email.com";
    public static final String ADMIN_PASSWORD = "password";

    private final MemberRepository memberRepository;

    DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member(ADMIN_EMAIL, ADMIN_PASSWORD, 20, List.of(RoleType.ROLE_ADMIN.name())));
    }
}
