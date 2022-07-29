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
        List<String> roles = List.of(RoleType.ROLE_ADMIN.name(), RoleType.ROLE_MEMBER.name());
        Member 관리자 = new Member("admin@email.com", "password", 20, roles);
        memberRepository.save(관리자);
    }
}
