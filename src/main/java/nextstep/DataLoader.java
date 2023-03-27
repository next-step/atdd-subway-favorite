package nextstep;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.MemberRepository;

@Component
@Profile("test")
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

    public void loadMemberData(String email, String password, int age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        memberRepository.save(memberRequest.toMember());
    }
}
