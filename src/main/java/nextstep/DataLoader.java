package nextstep;

import nextstep.member.application.LoginMemberService;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_ADMIN;
import static nextstep.member.domain.RoleType.ROLE_MEMBER;

@Component
public class DataLoader {

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void loadData() {
        Member masterAdmin = new Member("masterAdmin",
                "password",
                1,
                List.of(ROLE_ADMIN.name(),
                        ROLE_MEMBER.name()));
        memberRepository.save(masterAdmin);
    }
}
