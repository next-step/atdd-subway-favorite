package nextstep;

import java.util.List;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {
    private final MemberRepository memberRepository;
    public static final String ADMIN_EMAIL = "admin@email.com";
    public static final String ADMIN_PASSWORD = "adminPassword";
    public static final int ADMIN_AGE = 20;

    public static final String MEMBER_EMAIL = "member@email.com";
    public static final String MEMBER_PASSWORD = "memberPassword";
    public static final int MEMBER_AGE = 25;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE, List.of(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member(MEMBER_EMAIL, MEMBER_PASSWORD, MEMBER_AGE, List.of(RoleType.ROLE_MEMBER.name())));
    }
}
