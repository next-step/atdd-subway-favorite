package nextstep;

import lombok.AllArgsConstructor;
import nextstep.member.application.LoginMemberService;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class DataLoader {

    private final MemberRepository memberRepository;

    private final String ADMIN_EMAIL = "admin@email.com";
    private final String ADMIN_PASSWORD = "password";
    private final int ADMIN_AGE = 20;

    public void loadData() {
        memberRepository.save(new Member(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE));
    }
}
