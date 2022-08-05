package nextstep.common.dataloader;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader {

    public static final String ADMIN_EMAIL = "admin@email.com";
    public static final String MEMBER_EMAIL = "member@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member(ADMIN_EMAIL, PASSWORD, AGE, List.of(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member(MEMBER_EMAIL, PASSWORD, AGE, List.of(RoleType.ROLE_MEMBER.name())));
    }
}
