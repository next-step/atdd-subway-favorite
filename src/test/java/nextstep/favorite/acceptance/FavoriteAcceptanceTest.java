package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.station.dto.StationResponse;
import nextstep.station.entity.Station;
import nextstep.station.repository.StationRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.util.AuthStep.로그인_후_토큰_발급;
import static nextstep.subway.util.FavoriteStep.즐겨찾기_생성;
import static nextstep.subway.util.StationStep.지하철_역_등록;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";
    public static final Integer AGE = 20;

    StationResponse 강남역;
    StationResponse 역삼역;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        강남역 = 지하철_역_등록("강남역");
        역삼역 = 지하철_역_등록("역삼역");

    }

    /**
     * User Story : 사용자는 즐겨찾기를 등록할 수 있다.
     **/


    /* Given: 사용자가 로그인에 성공한 다음,
       When: 사용자가 즐겨찾기 생성을 요청하면,
       Then: 사용자의 즐겨찾기가 생성된다. */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    public void create_favorite_success() {
        // given
        memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        var accessToken = 로그인_후_토큰_발급(EMAIL, PASSWORD);

        // when & then
        var 즐겨찾기_요청 = FavoriteRequest.of(강남역.getId(), 역삼역.getId());
        즐겨찾기_생성(accessToken, 즐겨찾기_요청);
    }

    /* Given: 사용자가 로그인에 성공한 다음,
       When: 사용자가 즐겨찾기 목록을 요청하면,
       Then: 사용자의 즐겨찾기 목록이 반환된다. */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    public void retrieve_favorite_success() {}

    /* Given: 사용자가 로그인에 성공한 다음,
       When: 사용자가 즐겨찾기 삭제를 요청하면,
       Then: 사용자의 즐겨찾기가 삭제된다. */
    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    public void delete_favorite_success() {}
}