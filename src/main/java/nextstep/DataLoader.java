package nextstep;

import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    public static final String ADMIN_EMAIL = "admin@email.com";
    public static final String ADMIN_PASSWORD = "password";
    public static final int ADMIN_AGE = 20;
    public static final String MEMBER_EMAIL = "member@email.com";
    public static final String MEMBER_PASSWORD = "password";
    public static final int MEMBER_AGE = 15;

    private final MemberService memberService;

    public DataLoader(MemberService memberService) {
        this.memberService = memberService;
    }

    public void loadData() {
        memberService.createMember(new MemberRequest(ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_AGE, true));
        memberService.createMember(new MemberRequest(MEMBER_EMAIL, MEMBER_PASSWORD, MEMBER_AGE, false));
    }
}
