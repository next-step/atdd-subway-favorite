package nextstep.subway.unit;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.message.Message;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("findMemberOfMine 테스트 : 정상")
    void findMemberOfMine_test_success() {
//        memberRepository.save(new Member("admin@email.com", "password", 1));
        assertThat(memberService.findMemberOfMine("admin@email.com")).extracting(MemberResponse::getEmail).isEqualTo("admin@email.com");
    }

    @Test
    @DisplayName("findMemberOfMine 테스트 : 존재하지 않은 이메일")
    void findMemberOfMine_test_invalid_token() {
        assertThatThrownBy(() -> {
            memberService.findMemberOfMine("invalid");
        }).hasMessageContaining(Message.NOT_EXIST_EAMIL);
    }

}
