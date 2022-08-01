package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader {
    private static final String ADMIN_EMAIL = "admin@email.com";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    private final MemberRepository memberRepository;

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        log.info("call DataLoader");
        Member admin = new Member(ADMIN_EMAIL, PASSWORD, AGE, List.of(RoleType.ROLE_ADMIN.name()));
        Member user = new Member(EMAIL, PASSWORD, AGE, List.of(RoleType.ROLE_MEMBER.name()));
        memberRepository.save(admin);
        memberRepository.save(user);
        //memberRepository.saveAll(List.of(admin, user));
    }
}
