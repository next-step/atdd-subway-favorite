package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {
    private final MemberRepository memberRepository;

    public void loadData() {
        Member admin = new Member("admin@nextstep.com", "12345", 25, List.of(RoleType.ROLE_ADMIN.name()));
        Member user = new Member("user@nextstep.com", "54321", 20, List.of(RoleType.ROLE_MEMBER.name()));

        memberRepository.save(admin);
        memberRepository.save(user);
    }
}
