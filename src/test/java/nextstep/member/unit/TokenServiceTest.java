package nextstep.member.unit;

import nextstep.member.GithubResponse;
import nextstep.member.application.TokenService;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TokenServiceTest {

    @Autowired
    TokenService tokenService;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("깃허브 로그인")
    @Test
    void githubLogin() {
        //given
        GithubResponse user = GithubResponse.사용자1;

        //when
        TokenResponse result = tokenService.createGithubToken(user.getCode());

        //then
        assertThat(result.getAccessToken()).isEqualTo(user.getAccessToken());
    }

    @DisplayName("최초 로그인일 경우 DB에 저장한다")
    @Test
    void saveMember() {
        //given
        GithubResponse user = GithubResponse.사용자1;

        //when
        TokenResponse result = tokenService.createGithubToken(user.getCode());

        //then
        assertThat(result.getAccessToken()).isEqualTo(user.getAccessToken());

        Member member = memberRepository.findByEmail(user.getEmail()).get();
        assertThat(member.getEmail()).isEqualTo(user.getEmail());
        assertThat(member.getAge()).isEqualTo(user.getAge());
    }
}