package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_ADMIN;

@Component
public class DataLoader {
    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        Member admin = new Member("admin@email.com", "password", 20, List.of(ROLE_ADMIN.name()));
        memberRepository.saveAndFlush(admin);
    }
}
