package nextstep.member.application;

import nextstep.auth.application.UserDetail;
import nextstep.auth.application.dto.OAuth2Response;
import nextstep.core.DatabaseCleaner;
import nextstep.core.TestConfig;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
class MemberDetailsServiceTest {
    private final String EMAIL = "test@test.com";
    private final int AGE = 20;

    @Autowired
    private MemberService memberService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MemberDetailsService memberDetailsService;

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @Test
    @DisplayName("loadUserByEmail 시 member 가 이미 존재한다면 생성하지 않고 기존 멤버 정보를 불러온다.")
    void loadUserByEmailWhenMemberExist() {
        final MemberResponse memberResponse = memberService.createMember(new MemberRequest(EMAIL, "", AGE));

        final UserDetail userDetail = memberDetailsService.loadUserByEmail(EMAIL);

        assertThat(memberResponse.getId()).isEqualTo(userDetail.getId());
    }

    @Test
    @DisplayName("loadUserByEmail 시 member 가 존재하지 않는다면 MemberNotFoundException 이 던져진다.")
    void loadUserByEmailWhenMemberNotExist() {
        assertThatThrownBy(() -> memberDetailsService.loadUserByEmail(EMAIL))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("loadOrCreateUser 시 존재하지 않는다면 새로 만든 뒤 반환 받고 존재한다면 UserDetail 정보를 반환 받을 수 있다.")
    void loadOrCreateUserTest() {
        final UserDetail userDetail = memberDetailsService.loadOrCreateUser(new OAuth2Response(EMAIL, AGE));
        final UserDetail userDetailSecond = memberDetailsService.loadOrCreateUser(new OAuth2Response(EMAIL, AGE));

        assertThat(userDetail).usingRecursiveComparison().isEqualTo(userDetailSecond);
    }
}
