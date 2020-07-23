package nextstep.subway.member.ui;

import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MemberControllerTest {

    private String email;
    private String password;
    private Integer age;
    private LoginMember loginMember;
    private MemberService memberService;
    private MemberController memberController;

    @BeforeEach
    void setUp() {
        email = "dhlee@Test.com";
        password = "password";
        age = 20;

        loginMember = new LoginMember(1L, email, password, age);
        memberService = mock(MemberService.class);
        memberController = new MemberController(memberService);
    }

    @Test
    @DisplayName("내 정보를 수정한다.")
    public void updateMemberOfMineTest() {
        // when
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        ResponseEntity<MemberResponse> responseEntity = memberController.updateMemberOfMine(loginMember, memberRequest);

        // then
        verify(memberService).updateMember(anyLong(), any());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("내 정보를 삭제한다.")
    public void deleteMemberOfMineTest() {
        // when
        ResponseEntity<MemberResponse> responseEntity = memberController.deleteMemberOfMine(loginMember);

        // then
        verify(memberService).deleteMember(anyLong());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }
}