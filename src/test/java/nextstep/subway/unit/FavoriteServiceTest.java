package nextstep.subway.unit;

import io.restassured.RestAssured;
import nextstep.favorite.applicataion.FavoriteService;
import nextstep.favorite.applicataion.dto.FavoriteResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관리")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FavoriteServiceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    private FavoriteService favoriteService;

    private Station 출발역;
    private Station 도착역;
    private Member 회원;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();

        출발역 = stationRepository.save(new Station("출발역"));
        도착역 = stationRepository.save(new Station("도착역"));
        lineService.saveLine(new LineRequest("이호선", "green", 출발역.getId(), 도착역.getId(), 10));
        회원 = memberRepository.save(new Member("e@mail.com", "password", 20));
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        //when
        Long favoriteId = favoriteService.createFavorite(회원.getId(), 출발역.getId(), 도착역.getId());

        //then
        assertThat(favoriteId).isPositive();
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void getAllFavorites() {
        //given
        favoriteService.createFavorite(회원.getId(), 출발역.getId(), 도착역.getId());

        //when
        List<FavoriteResponse> allFavorites = favoriteService.getAllFavorites(회원.getId());

        //then
        assertThat(allFavorites).hasSize(1);

    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        //given
        Long favoriteId = favoriteService.createFavorite(회원.getId(), 출발역.getId(), 도착역.getId());

        //when
        favoriteService.removeFavorite(회원.getId(), favoriteId);

        List<FavoriteResponse> allFavorites = favoriteService.getAllFavorites(회원.getId());

        //then
        assertThat(allFavorites).isEmpty();

    }
}
