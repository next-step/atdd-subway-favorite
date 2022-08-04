package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader {

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        Member admin = new Member("admin@email.com", "password", 20, List.of(RoleType.ROLE_ADMIN.name()));
        Member member = new Member("member@email.com", "password", 21, List.of(RoleType.ROLE_MEMBER.name()));
        memberRepository.saveAll(List.of(admin, member));
    }
}
