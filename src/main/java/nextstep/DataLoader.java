package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoader {

    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20));
    }
}
