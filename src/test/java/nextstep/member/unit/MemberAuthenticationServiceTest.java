package nextstep.member.unit;

import nextstep.authentication.application.dto.GithubProfileResponse;
import nextstep.authentication.domain.AuthenticationInformation;
import nextstep.authentication.domain.LoginMember;
import nextstep.member.application.MemberAuthenticationService;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.Member;
import nextstep.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.utils.UserInformation.사용자1;
import static nextstep.utils.UserInformation.사용자2;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 인증 서비스 관련 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class MemberAuthenticationServiceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private MemberAuthenticationService memberAuthenticationService;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute(this);
        memberService.save(new MemberRequest(사용자1.getEmail(), 사용자1.getPassword(), 사용자1.getAge()));
    }

    @DisplayName("메일로 회원 찾는 함수는, 이메일을 입력받아 회원을 찾아 인증 정보를 반환한다.")
    @Test
    void findMemberByEmailTest() {
        // when
        AuthenticationInformation authenticationInformation = memberAuthenticationService.findMemberByEmail(사용자1.getEmail());

        // then
        assertThat(authenticationInformation.getEmail()).isEqualTo(사용자1.getEmail());
    }

    @DisplayName("회원을 찾거나 생성하는 함수는, 회원의 깃허브 프로필을 입력받아 회원의 로그인 정보를 반환한다.")
    @Test
    void lookUpMemberByGithubProfilesTest() {
        // when
        LoginMember loginMember = memberAuthenticationService.lookUpOrCreateMember(new GithubProfileResponse(사용자1.getEmail(), 사용자1.getAge()));

        // then
        assertThat(loginMember.getEmail()).isEqualTo(사용자1.getEmail());
    }

    @DisplayName("회원을 찾거나 생성하는 함수는,  비회원의 깃허브 프로필을 입력받아 회원 가입 후 로그인 정보를 반환한다.")
    @Test
    void createMemberByGithubProfilesTest() {
        // when
        LoginMember loginMember = memberAuthenticationService.lookUpOrCreateMember(new GithubProfileResponse(사용자2.getEmail(), 사용자2.getAge()));

        // then
        AuthenticationInformation memberByEmail = memberAuthenticationService.findMemberByEmail(사용자2.getEmail());
        assertThat(loginMember.getEmail()).isEqualTo(memberByEmail.getEmail());
    }
}
