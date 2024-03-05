package nextstep.member.application;

import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.OAuth2ProfileResponse;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    private static final String EMAIL = "test@test.com";
    private static final int AGE = 20;

    @Autowired
    private MemberService memberService;
    private final static OAuth2ProfileResponse oAuth2ProfileResponse
            = new OAuth2ProfileResponse(EMAIL, AGE);

    @DisplayName("이미 회원이 존재한다면이라면 githubProfile 조회")
    @Test
    void findOrCreateMemberWhenMemberExist() {
        MemberResponse memberResponse
                = memberService.createMember(new MemberRequest(EMAIL, null, AGE));
        Member member = memberService.findOrCreateMember(oAuth2ProfileResponse);

        assertThat(memberResponse.getId()).isEqualTo(member.getId());
    }

    @DisplayName("존재하지 않는 회원이라면 회원 가입시킨 후 githubProfile 조회를 한다.")
    @Test
    void findOrCreateMemberWhenMemberNotExist() {
        Member member = memberService.findOrCreateMember(oAuth2ProfileResponse);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo(EMAIL);
        assertThat(member.getAge()).isEqualTo(AGE);
    }
}
