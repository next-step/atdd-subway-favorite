package nextstep.common.utils;

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

        memberRepository.save(new Member("email1@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("email2@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("email3@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("email4@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
    }
}
