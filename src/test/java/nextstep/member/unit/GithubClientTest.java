package nextstep.member.unit;

import nextstep.member.GithubResponse;
import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {

    @Autowired
    GithubClient client;

    @DisplayName("깃허브 토큰 요청")
    @Test
    void requestToken() {
        //given
        GithubResponse user = GithubResponse.사용자1;

        //when
        String accessToken = client.requestToken(user.getCode());

        //then
        assertThat(accessToken).isEqualTo(user.getAccessToken());
    }

    @DisplayName("깃허브 유저 조회")
    @Test
    void requestUser() {
        //given
        GithubResponse user = GithubResponse.사용자1;

        //when
        GithubProfileResponse profile = client.requestUser(user.getAccessToken());

        //then
        assertThat(profile.getEmail()).isEqualTo(user.getEmail());
        assertThat(profile.getAge()).isEqualTo(user.getAge());
    }
}
