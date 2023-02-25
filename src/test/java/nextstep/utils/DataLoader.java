package nextstep.utils;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;

import static nextstep.member.fake.ui.GithubResponses.사용자1;
import static nextstep.member.fake.ui.GithubResponses.사용자2;
import static nextstep.member.fake.ui.GithubResponses.사용자3;
import static nextstep.member.fake.ui.GithubResponses.사용자4;

@Profile("test")
@Component
public class DataLoader {
    public static final String ADMIN_EMAIL = "admin@email.com";
    public static final String ADMIN_PASSWORD = "password";
    public static final int ADMIN_AGE = 20;
    public static final Set<RoleType> ADMIN_ROLES = Set.of(RoleType.ROLE_ADMIN);

    public static final String MEMBER_EMAIL = "member@email.com";
    public static final String MEMBER_PASSWORD = "password";
    public static final int MEMBER_AGE = 20;
    public static final Set<RoleType> MEMBER_ROLES = Set.of(RoleType.ROLE_MEMBER);

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE, ADMIN_ROLES));
        memberRepository.save(new Member(MEMBER_EMAIL, MEMBER_PASSWORD, MEMBER_AGE, MEMBER_ROLES));
        memberRepository.save(new Member(사용자1.getEmail(), "password", 20, Set.of(RoleType.ROLE_MEMBER)));
        memberRepository.save(new Member(사용자2.getEmail(), "password", 20, Set.of(RoleType.ROLE_MEMBER)));
        memberRepository.save(new Member(사용자3.getEmail(), "password", 20, Set.of(RoleType.ROLE_MEMBER)));
        memberRepository.save(new Member(사용자4.getEmail(), "password", 20, Set.of(RoleType.ROLE_MEMBER)));
    }
}
