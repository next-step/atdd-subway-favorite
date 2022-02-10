package nextstep.auth.unit;

import nextstep.auth.authentication.UserDetailsService;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class AuthTest {
    protected static final String USERNAME_FIELD = "username";
    protected static final String PASSWORD_FIELD = "password";
    protected static final String EMAIL = "email@email.com";
    protected static final String PASSWORD = "password";
    protected static final Integer AGE = 20;

    @Autowired
    private MemberService memberService;

    @Autowired
    protected UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        memberService.createMember(new MemberRequest(EMAIL, PASSWORD, AGE));
    }
}
