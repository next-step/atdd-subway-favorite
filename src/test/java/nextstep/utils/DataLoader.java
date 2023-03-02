package nextstep.utils;

import nextstep.auth.acceptance.fake.FakeGithubResponses;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Profile("test")
@Component
public class DataLoader {
    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 29, Arrays.asList(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password", 29, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(FakeGithubResponses.사용자1.getEmail(), "password", 29, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(FakeGithubResponses.사용자2.getEmail(), "password", 29, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(FakeGithubResponses.사용자3.getEmail(), "password", 29, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(FakeGithubResponses.사용자4.getEmail(), "password", 29, Arrays.asList(RoleType.ROLE_MEMBER.name())));
    }
}
