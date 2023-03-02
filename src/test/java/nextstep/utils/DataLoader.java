package nextstep.utils;

import java.util.List;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class DataLoader {

    private final MemberRepository memberRepository;

    public DataLoader(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 26, List.of(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password", 25, List.of(RoleType.ROLE_MEMBER.name())));
    }
}
