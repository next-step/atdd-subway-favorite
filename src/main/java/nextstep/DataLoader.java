package nextstep;

import java.util.List;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {
    private final MemberRepository memberRepository;
    public static final String ADMIN_EMAIL = "superAdmin@email.com";
    public static final String ADMIN_PASSWORD = "test";
    public static final int ADMIN_AGE = 20;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE, List.of(RoleType.ROLE_ADMIN.name())));
    }
}
