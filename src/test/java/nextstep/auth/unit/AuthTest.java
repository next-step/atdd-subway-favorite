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

import static nextstep.auth.unit.AuthData.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class AuthTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    protected UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        memberService.createMember(new MemberRequest(EMAIL, PASSWORD, AGE));
    }
}
