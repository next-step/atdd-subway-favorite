package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.FavoriteSteps.*;
import static nextstep.subway.acceptance.MemberAcceptanceTest.*;
import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class FavoriteAcceptanceTest extends AcceptanceTest {
	/**
	 * When 지하철 노선 생성을 요청 하면
	 * Then 지하철 노선 생성이 성공한다.
	 */
	@DisplayName("즐겨찾기 생성")
	@Test
	void createFavoriteTest() {
		회원_생성_요청(EMAIL, PASSWORD, AGE);

		String 로그인_회원_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);

		Long sourceId = 1L;
		Long targetId = 2L;
		ExtractableResponse<Response> response = 즐겨찾기_생성(sourceId, targetId, 로그인_회원_토큰);

		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}
}
