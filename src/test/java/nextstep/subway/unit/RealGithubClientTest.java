package nextstep.subway.unit;

import nextstep.member.application.RealGithubClient;
import nextstep.utils.ObjectStringMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(value = {RealGithubClient.class})
public class RealGithubClientTest {
    @Autowired
    private RealGithubClient githubClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Value("${github.url.access-token}")
    private String tokenUrl;

    private String expectedCode;
    private String expectedAccessToken;
    private ExpectedJsonDTO expectedJsonDTO;

    @BeforeEach
    void setUp() {
        expectedCode = "testCodeContext";
        expectedAccessToken = "expectedAccessTokenValue";
        expectedJsonDTO = new ExpectedJsonDTO(expectedAccessToken, "myScope", "Bearer");
    }

    @Test
    @DisplayName("github login test")
    public void githubLoginTest() {
        // given
        String expectedJsonResponse = ObjectStringMapper.convertObjectAsString(expectedJsonDTO);
        mockServer
                .expect(MockRestRequestMatchers.requestTo(tokenUrl))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withSuccess(expectedJsonResponse, MediaType.APPLICATION_JSON));

        // when
        String accessToken = githubClient.getAccessTokenFromGithub(expectedCode);

        // then
        assertThat(accessToken).isEqualTo(expectedAccessToken);
    }

    static class ExpectedJsonDTO {
        private String accessToken;
        private String scope;
        private String tokenType;

        public ExpectedJsonDTO(String accessToken, String scope, String tokenType) {
            this.accessToken = accessToken;
            this.scope = scope;
            this.tokenType = tokenType;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getScope() {
            return scope;
        }

        public String getTokenType() {
            return tokenType;
        }
    }

}