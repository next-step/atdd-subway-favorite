package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader {
    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, Set.of(RoleType.ROLE_ADMIN)));
        memberRepository.save(new Member("member@email.com", "password", 20, Set.of(RoleType.ROLE_MEMBER)));
    }
}
