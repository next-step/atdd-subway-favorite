package nextstep;

import static nextstep.member.domain.RoleType.ROLE_ADMIN;
import static nextstep.member.domain.RoleType.ROLE_MEMBER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final MemberRepository memberRepository;

    public void loadData() {
        Member admin = new Member("admin@email.com", "password", 20, List.of(ROLE_ADMIN.name()));
        Member member = new Member("member@email.com", "password", 20, List.of(ROLE_MEMBER.name()));
        memberRepository.saveAll(List.of(admin, member));
    }
}
