package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_ADMIN;
import static nextstep.member.domain.RoleType.ROLE_MEMBER;

@Component
@RequiredArgsConstructor
public class DataLoader {

    public static final String ADMIN_EMAIL = "admin@email.com";
    public static final String MEMBER_EMAIL = "member@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    private final MemberRepository memberRepository;

    public void loadData() {
        memberRepository.save(new Member(ADMIN_EMAIL, PASSWORD, AGE, List.of(ROLE_ADMIN.name())));
        memberRepository.save(new Member(MEMBER_EMAIL, PASSWORD, AGE, List.of(ROLE_MEMBER.name())));
    }
}
