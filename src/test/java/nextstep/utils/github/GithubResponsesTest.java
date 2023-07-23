package nextstep.utils.github;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GithubResponsesTest {

    @DisplayName("코드로 사용자를 조회한다")
    @Test
    void ofCode() {
        // given
        String code = GithubResponses.사용자0.getCode();

        // when
        GithubResponses response = GithubResponses.ofCode(code);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(GithubResponses.사용자0);
    }

    @DisplayName("토큰으로 사용자를 조회한다")
    @Test
    void ofAccessToken() {
        // given
        String accessToken = GithubResponses.사용자1.getAccessToken();

        // when
        GithubResponses response = GithubResponses.ofAccessToken(accessToken);

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(GithubResponses.사용자1);
    }

    @DisplayName("없는 코드로 사용자를 조회하면 에러 발생")
    @Test
    void ofCode_NonExistingCode_Exception() {
        // given
        String code = "없는_코드";

        // when, then
        assertThatThrownBy(() -> GithubResponses.ofCode(code))
                .isInstanceOf(DataIntegrityViolationException.class)
                .message()
                .isEqualTo("코드가 존재하지 않습니다.");
    }

    @DisplayName("없는 토큰으로 사용자를 조회하면 에러 발생")
    @Test
    void ofAccessToken_NonExistingToken_Exception() {
        // given
        String accessToken = "없는_토큰";

        // when, then
        assertThatThrownBy(() -> GithubResponses.ofAccessToken(accessToken))
                .isInstanceOf(DataIntegrityViolationException.class)
                .message()
                .isEqualTo("토큰이 존재하지 않습니다.");
    }
}
