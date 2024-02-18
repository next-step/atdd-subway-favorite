package nextstep.member.application;

import nextstep.core.DatabaseCleaner;
import nextstep.core.TestConfig;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;


@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
class MemberServiceTest {
    private final String EMAIL = "test@test.com";
    private final int AGE = 20;

    @Autowired
    private MemberService memberService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private GithubProfileResponse githubProfileResponse;

    @BeforeEach
    void setUp() {
        githubProfileResponse = new GithubProfileResponse(EMAIL, AGE);
    }

    @AfterEach
    void tearDown() {
        databaseCleaner.clear();
    }

    @Test
    @DisplayName("findOrCreateMember 시 member 가 이미 존재한다면 생성하지 않고 기존 멤버 정보를 불러온다.")
    void findOrCreateMemberWhenMemberExist() {
        final MemberResponse memberResponse = memberService.createMember(new MemberRequest(EMAIL, "", AGE));

        final Member member = memberService.findOrCreateMember(githubProfileResponse);

        assertThat(memberResponse.getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("findOrCreateMember 시 member 가 존재하지 않는다면 새로 생성 후 멤버 정보를 불러온다.")
    void findOrCreateMemberWhenMemberNotExist() {
        final Member member = memberService.findOrCreateMember(githubProfileResponse);

        assertSoftly(softly->{
            softly.assertThat(member.getId()).isNotNull();
            softly.assertThat(member.getEmail()).isEqualTo(EMAIL);
            softly.assertThat(member.getAge()).isEqualTo(AGE);
        });
    }
}
