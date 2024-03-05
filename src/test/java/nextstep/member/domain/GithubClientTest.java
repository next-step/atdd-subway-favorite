package nextstep.member.domain;

import static nextstep.member.domain.GithubResponses.사용자1;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
class GithubClientTest {

    @Test
    void requestGithubToken() {
        GithubClient client = Mockito.mock(GithubClient.class);
        String code = 사용자1.getCode();

        when(client.requestGithubToken(anyString())).thenReturn(사용자1.getAccessToken());
        String accessToken = client.requestGithubToken(code);

        assertThat(accessToken).isNotNull();
        assertThat(사용자1.getAccessToken()).isEqualTo(accessToken);
    }

}