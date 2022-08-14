package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static nextstep.member.domain.RoleType.ROLE_ADMIN;

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
    }

}
