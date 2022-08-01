package nextstep;

import java.util.List;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, List.of(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("test@email.com", "password", 25, List.of(RoleType.ROLE_MEMBER.name())));
    }
}
