package nextstep.core.member.application;

import nextstep.common.annotation.ComponentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static nextstep.core.member.fixture.TokenFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("깃허브 클라이언트 컴포넌트 테스트")
@ComponentTest
public class GithubClientTest {

    @Autowired
    GithubClient githubClient;

    /**
     * When 코드를 통해 토큰을 요청하고 응답받았고
     * When    응답받은 토큰을 통해 리소스를 요청하고
     * When        리소스를 응답받았을 경우
     * Then 응답받은 리소스를 기반으로 회원 정보를 반환한다.
     */
    @ParameterizedTest(name = "코드({0})로_요청하고_회원정보({1})_응답")
    @MethodSource(value = "provideCodeAndToken")
    void 회원_정보_반환(String code, String email) {
        // when
        GithubProfileResponse githubProfileResponse = githubClient.requestMemberInfo(code);

        // then
        assertThat(githubProfileResponse.getEmail()).isEqualTo(email);
    }


    static Stream<Arguments> provideCodeAndToken() { // JDK 16 미만 버전, 내부 클래스(@Nested)에 static 키워드 허용 X
        return Stream.of(
                Arguments.of(KIM.getCode(), KIM.getEmail()),
                Arguments.of(HWANG.getCode(), HWANG.getEmail()),
                Arguments.of(JUNG.getCode(), JUNG.getEmail()),
                Arguments.of(LEE.getCode(), LEE.getEmail()));
    }
}
