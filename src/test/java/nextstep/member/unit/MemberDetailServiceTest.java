package nextstep.member.unit;

import nextstep.auth.domain.UserDetail;
import nextstep.member.application.MemberDetailService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@DisplayName("회원 서비스 단위 테스트")
public class MemberDetailServiceTest {

    private String email = "email1@email.com";
    private int age = 20;

    @Autowired
    MemberDetailService memberDetailService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 회원 정보를 조회한다.")
    void 이메일로회원정보조회() {
        //given
        Member member = memberRepository.save(Member.builder()
                .age(age)
                .email(email)
                .password(UUID.randomUUID().toString())
                .build());

        //when
        UserDetail userDetail = memberDetailService.findUser(member.getEmail());

        //then
        assertThat(userDetail.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 회원은 저장 후 회원 정보를 조회한다.")
    void 존재하지않는회원_저장후정보조회() {
        //when
        UserDetail userDetail = memberDetailService.findOrCreateUser(email, age);

        //then
        assertThat(userDetail.getId()).isNotNull();
        assertThat(userDetail.getEmail()).isEqualTo(email);
        assertThat(userDetail.getAge()).isEqualTo(age);
    }
}
