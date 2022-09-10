package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DataLoader {

    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, List.of("ROLE_ADMIN")));
        memberRepository.save(new Member("member@email.com", "password", 16, List.of("ROLE_MEMBER")));
    }
}
