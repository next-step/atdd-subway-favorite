package nextstep.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.line.domain.LineRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static nextstep.favorite.acceptance.FavoriteAcceptanceTestFixture.*;
import static nextstep.utils.AssertUtil.assertResponseCode;
import static nextstep.utils.UserInformation.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 인수테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private Member member;
    private Station 강남역;
    private Station 성수역;
    private String token;

    @BeforeEach
    public void setUp() {
        super.setUp();
        member = memberRepository.save(new Member(사용자1.getEmail(), 사용자1.getPassword(), 사용자1.getAge()));
        강남역 = stationRepository.save(Station.from("강남역"));
        성수역 = stationRepository.save(Station.from("성수역"));
        lineRepository.save(신분당선(강남역, 성수역));
        token = 로그인(member.getEmail(), member.getPassword());
    }

    /**
     * Given: 사용자와 지하철 역과 구간이 존재하고,
     * When: 로그인한 사용자가 즐겨찾기 추가 요청을 보내면,
     * Then: 즐겨찾기를 조회 했을 때 즐겨찾기 목록에 추가된 즐겨찾기가 존재한다.
     */
    @DisplayName("즐겨찾기 추가 요청은, 출발역과 도착역을 입력하고 즐겨찾기 추가 요청하면 즐겨찾기 조회시 즐겨찾기 목록에 포함된다.")
    @Test
    void createFavoriteTest() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_추가_요청(token, 강남역.getId(), 성수역.getId());

        // then
        assertResponseCode(response, HttpStatus.CREATED);
        assertThat(getStationNames(즐겨찾기_조회_요청(token))).containsExactly(강남역.getName(), 성수역.getName());
    }

    /**
     * Given: 사용자와 지하철 역과 구간이 존재하며, 추가된 즐겨찾기가 존재하고,
     * When: 로그인한 사용자가 즐겨찾기 조회 요청을 보내면,
     * Then: 본인의 즐겨찾기 목록을 조회할 수 있다.
     */
    @DisplayName("즐겨찾기 조회 요청은, 로그인한 사용자가 즐겨찾기 조회 요청하면 나의 즐겨찾기 목록이 응답된다.")
    @Test
    void findFavoriteTest() {
        // given
        favoriteRepository.save(new Favorite(강남역.getId(), 성수역.getId(), member.getId()));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(token);

        // then
        assertResponseCode(response, HttpStatus.OK);
        assertThat(getStationNames(response)).containsExactly(강남역.getName(), 성수역.getName());
    }

    /**
     * Given: 지하철 역과 구간이 존재하며, 추가된 즐겨찾기가 존재하고,
     * When: 로그인한 사용자가 즐겨찾기 삭제 요청을 보내면,
     * Then: 즐겨찾기를 조회 했을 때 즐겨찾기 목록에 삭제된 즐겨찾기가 존재하지 않는다.
     */
    @DisplayName("즐겨찾기 삭제 요청은, 로그인한 사용자가 즐겨찾기 조회 요청하면 나의 즐겨찾기 목록이 응답된다.")
    @Test
    void deleteFavoriteTest() {
        // given
        Station 교대역 = stationRepository.save(Station.from("교대역"));
        Station 홍대역 = stationRepository.save(Station.from("홍대역"));
        lineRepository.save(이호선(교대역, 홍대역));
        Favorite 강남역_성수역 = favoriteRepository.save(new Favorite(강남역.getId(), 성수역.getId(), member.getId()));
        favoriteRepository.save(new Favorite(교대역.getId(), 홍대역.getId(), member.getId()));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(token, 강남역_성수역.getId());

        // then
        assertResponseCode(response, HttpStatus.NO_CONTENT);
        assertThat(getStationNames(즐겨찾기_조회_요청(token))).doesNotContain(강남역.getName(), 성수역.getName());
    }
}