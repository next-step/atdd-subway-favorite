package nextstep;

import nextstep.member.application.dto.GithubResponses;
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
        memberRepository.save(new Member("admin@email.com", "password", 20, List.of(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
    }

    public void loadDataWithGithubUser() {
        memberRepository.save(new Member(GithubResponses.사용자1.getEmail(), null, null, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자2.getEmail(), null, null, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자3.getEmail(), null, null, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자4.getEmail(), null, null, List.of(RoleType.ROLE_MEMBER.name())));
    }

}
