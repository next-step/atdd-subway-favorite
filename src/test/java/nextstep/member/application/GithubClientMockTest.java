package nextstep.member.application;

import nextstep.auth.application.GithubClient;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.ProfileResponse;
import nextstep.auth.exception.ApiCallException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static nextstep.utils.dtoMock.GithubResponse.사용자1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class GithubClientMockTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private GithubClient githubClient;

    private String GITHUB_ACCESSTOKEN_URL = "githubAccessTokenUrl";
    private String GITHUB_ACCESSPROFILE_URL = "githubAccessProfileUrl";
    private String GITHUB_CLIENT_ID = "githubClientId";
    private String GITHUB_CLIENT_SECRET = "githubClientSecret";

    private String 코드 = "code";
    private String 토큰 = "token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(githubClient, GITHUB_ACCESSTOKEN_URL, GITHUB_ACCESSTOKEN_URL);
        ReflectionTestUtils.setField(githubClient, GITHUB_ACCESSPROFILE_URL, GITHUB_ACCESSPROFILE_URL);
        ReflectionTestUtils.setField(githubClient, GITHUB_CLIENT_ID, GITHUB_CLIENT_ID);
        ReflectionTestUtils.setField(githubClient, GITHUB_CLIENT_SECRET, GITHUB_CLIENT_SECRET);
    }

    @DisplayName("[requestGithubAccessToken] 깃허브 토큰을 성공적으로 발급받는다.")
    @Test
    void requestGithubAccessToken_success() {
        // given
        var mockResponse = new GithubAccessTokenResponse(토큰);
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(GithubAccessTokenResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // when
        var 발급받은_토큰 = githubClient.requestAccessToken(코드);

        // then
        assertThat(발급받은_토큰).isEqualTo(토큰);
    }

    @DisplayName("[requestGithubAccessToken] 예외 발생 시 ApiCallException을 발생시킨다.")
    @Test
    void requestGithubAccessToken_whenApiCallFails_throwsApiCallException() {
        // given
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(GithubAccessTokenResponse.class)))
                .thenThrow(new RuntimeException("Simulated exception"));

        // when & then
        assertAll(
                () -> assertThrows(ApiCallException.class, () -> githubClient.requestAccessToken(코드)).getMessage()
                        .equals("GITHUB_NOT_FOUND : Unexpected error occurred")
        );
    }

    @DisplayName("[requestGithubProfile] 깃허브 사용자 프로필을 성공적으로 가져온다.")
    @Test
    void requestGithubProfile_success() {
        // given
        var 사용자_프로필_응답 = ProfileResponse.of(사용자1.getEmail(), 사용자1.getAge());

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(ProfileResponse.class)))
                .thenReturn(new ResponseEntity<>(사용자_프로필_응답, HttpStatus.OK));

        // when
        var 가져온_사용자_프로필 = githubClient.requestProfile(토큰);

        // then
        assertAll(
                () -> assertThat(가져온_사용자_프로필.getEmail()).isEqualTo(사용자_프로필_응답.getEmail()),
                () -> assertThat(가져온_사용자_프로필.getAge()).isEqualTo(사용자_프로필_응답.getAge())
        );
    }

    @DisplayName("[requestGithubProfile] 예외 발생 시 ApiCallException을 발생시킨다.")
    @Test
    void requestGithubProfile_whenApiCallFails_throwsApiCallException() {
        // given
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(ProfileResponse.class)))
                .thenThrow(new RuntimeException("Simulated exception"));

        // when & then
        assertAll(
                () -> assertThrows(ApiCallException.class, () -> githubClient.requestProfile(토큰))
                        .getMessage().equals("GITHUB_NOT_FOUND : Unexpected error occurred")

        );
    }

}

