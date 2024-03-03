package nextstep.member.application;

import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    private final String EMAIL = "test@test.com";
    private final int AGE = 20;

    @Autowired
    private MemberService memberService;

    private GithubProfileResponse githubProfileResponse;

    @BeforeEach
    void setUp() {
        githubProfileResponse = new GithubProfileResponse(EMAIL, AGE);
    }

    @DisplayName("이미 존재하는 회원이라면 정보 조회")
    @Test
    void findOrCreateMemberWhenMemberExist() {
        MemberResponse memberResponse = memberService.createMember(new MemberRequest(EMAIL, "", AGE));
        Member member = memberService.findOrCreateMember(githubProfileResponse);

        assertThat(memberResponse.getId()).isEqualTo(member.getId());
    }

    @DisplayName("존재하지 않는 회원이라면 저장 후 정보 조회")
    @Test
    void findOrCreateMemberWhenMemberNotExist() {
        final Member member = memberService.findOrCreateMember(githubProfileResponse);

        assertSoftly(softly->{
            softly.assertThat(member.getId()).isNotNull();
            softly.assertThat(member.getEmail()).isEqualTo(EMAIL);
            softly.assertThat(member.getAge()).isEqualTo(AGE);
        });
    }
}
