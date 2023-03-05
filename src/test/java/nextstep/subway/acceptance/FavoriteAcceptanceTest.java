package nextstep.subway.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.message.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 즐겨찾기 관리 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    String token;
    Long source;
    Long target;

    @BeforeEach
    public void setUp() {
        token = MemberSteps.베어러_인증_로그인_요청("admin@email.com", "password")
                .jsonPath()
                .getString("accessToken");
        source = StationSteps.지하철역_생성_요청("강남역").jsonPath().getLong("id");
        target = StationSteps.지하철역_생성_요청("판교역").jsonPath().getLong("id");
    }

    /**
     * Given 로그인 하고, 지하철 역 두개를 생성한 다음
     * When  즐겨찾기에 두 역을 추가하면
     * Then  즐겨찾기가 생성된다.
     */
    @Test
    @DisplayName("성공 : 지하철 역 즐겨찾기 생성")
    void station_favorite_create_success() {
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성(token, source, target);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).startsWith("/favorites/");
    }
    
    /**
     * Given 로그인 하고, 지하철 역 한개를 생성한 다음
     * When  즐겨찾기에 같은역을 추가하면
     * Then  즐겨찾기가 실패한다.
     */
    @Test
    @DisplayName("실패 : 지하철 역 즐겨찾기 생성")
    void station_favorite_create_fail_duplicate() {
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성(token, source, source);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains(Message.SOURCE_TARGET_DUPLICATE_STATION.getMessage());
    }

    /**
     * Given 로그인 안하고, 지하철 역 두개를 생성한 다음
     * When  즐겨찾기에 두 역을 추가하면
     * Then  오류가 나온다.
     */
    @Test
    @DisplayName("실패 : 지하철 역 즐겨찾기 생성")
    void station_favorite_create_fail() {
        String token = null;
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성(token, source, target);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 로그인 하고, 로그인 하고, 지하철 역 두개를 생성하고, 즐겨찾기에 두 역을 추가한 다음
     * When  즐겨찾기를 조회하면
     * Then  즐겨찾기를 조회한다.
     */
    @Test
    @DisplayName("성공 : 지하철 역 즐겨찾기 조회")
    void station_favorite_read_success() {
        FavoriteSteps.즐겨찾기_생성(token, source, target);

        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_조회(token);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String name = response.jsonPath().getString("source.name");
        assertThat(name).contains("강남역");
    }

    /**
     * Given 로그인 안하고
     * When  즐겨찾기를 조회하면
     * Then  오류가 나온다.
     */
    @Test
    @DisplayName("실패 : 지하철 역 즐겨찾기 조회")
    void station_favorite_read_fail() {
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_조회(null);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Given 로그인하고, 지하철 역 두개를 생성하고, 즐겨찾기에 두 역을 추가한 다음
     * When  즐겨찾기를 삭제하면
     * Then  즐겨찾기가 삭제된다.
     */
    @Test
    @DisplayName("성공 : 지하철 역 즐겨찾기 삭제")
    void station_favorite_delete_success() {
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_생성(token, source, target);
        String uri = response.header("Location");

        ExtractableResponse<Response> response2 = FavoriteSteps.즐겨찾기_삭제(token, uri);
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 로그인 안하고
     * When  즐겨찾기를 삭제하면
     * Then  즐겨찾기가 삭제된다.
     */
    @Test
    @DisplayName("실패 : 지하철 역 즐겨찾기 삭제")
    void station_favorite_delete_fail() {
        ExtractableResponse<Response> response = FavoriteSteps.즐겨찾기_삭제(null, "/favorites/1");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


}
