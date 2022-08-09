package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_ADMIN;
import static nextstep.member.domain.RoleType.ROLE_MEMBER;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final MemberRepository memberRepository;

    public void loadData() {
        memberRepository.save(
                new Member(
                        ROLE_ADMIN.getEmail(),
                        ROLE_ADMIN.getPassword(),
                        ROLE_ADMIN.getAge(),
                        List.of(ROLE_ADMIN.name())
                )
        );
        memberRepository.save(
                new Member(
                        ROLE_MEMBER.getEmail(),
                        ROLE_MEMBER.getPassword(),
                        ROLE_MEMBER.getAge(),
                        List.of(ROLE_MEMBER.name())
                )
        );
    }
}
