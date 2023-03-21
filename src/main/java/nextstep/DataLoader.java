package nextstep;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import nextstep.member.domain.MemberRepository;

@Component
public class DataLoader {

    public void loadData() {
    }
    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadGithubMemberData() {
        memberRepository.save(GithubAccountFixtures.ACCOUNT1.createMember());
        memberRepository.save(GithubAccountFixtures.ACCOUNT2.createMember());
    }
}
