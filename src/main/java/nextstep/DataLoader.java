package nextstep;

import java.util.Arrays;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {
    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20,
            Arrays.asList(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password", 20,
            Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("email@email.com", "password", 20,
            Arrays.asList(RoleType.ROLE_MEMBER.name())));
    }
}
