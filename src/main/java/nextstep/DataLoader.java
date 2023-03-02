package nextstep;

import static nextstep.member.domain.RoleType.*;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;

@Component
public class DataLoader {
    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, Arrays.asList(ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password", 20, Arrays.asList(ROLE_MEMBER.name())));
        memberRepository.save(new Member("test@email.com", "password", 40, Arrays.asList(ROLE_MEMBER.name())));
    }
}
