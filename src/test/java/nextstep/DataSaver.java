package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSaver {
    public static final String MEMBER_EMAIL = "email";
    public static final String MEMBER_PASSWORD = "password";
    public static final Integer MEMBER_AGE = 30;

    @Autowired
    private MemberRepository memberRepository;

    public void saveData() {
        memberRepository.save(new Member(MEMBER_EMAIL, MEMBER_PASSWORD, MEMBER_AGE));
    }
}
