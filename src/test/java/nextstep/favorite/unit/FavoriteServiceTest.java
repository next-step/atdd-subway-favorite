package nextstep.favorite.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 역삼역;
    private Station 남부터미널역;
    private Station 석남역;

    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    private Member 회원_1;
    private Member 회원_2;
    private Favorite 즐겨찾기_1;
    private Favorite 즐겨찾기_2;
    private Favorite 즐겨찾기_3;

    @BeforeEach
    void setUp() {
        // 역 생성
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        역삼역 = new Station("역삼역");
        남부터미널역 = new Station("남부터미널역");
        석남역 = new Station("석남역");

        stationRepository.saveAll(List.of(교대역, 강남역, 양재역, 역삼역, 남부터미널역, 석남역));

        // 노선 및 구간 생성
        이호선 = new Line("이호선", "green", new Section(교대역, 강남역, 10));
        이호선.addSection(new Section(강남역, 역삼역, 2));
        이호선 = lineRepository.save(이호선);
        신분당선 = new Line("신분당선", "red", new Section(강남역, 양재역, 4));
        신분당선 = lineRepository.save(신분당선);
        삼호선 = new Line("삼호선", "orange", new Section(교대역, 남부터미널역, 6));
        삼호선.addSection(new Section(남부터미널역, 양재역, 3));
        삼호선 = lineRepository.save(삼호선);

        // 회원 등록
        회원_1 = memberRepository.save(new Member("testemail@test.com", "abc", 20));
        회원_2 = memberRepository.save(new Member("testemail2@test.com", "abc", 20));

        // 즐겨찾기 생성
        즐겨찾기_1 = favoriteRepository.save(new Favorite(교대역, 강남역, 회원_1));
    }

    @DisplayName("즐겨찾기 생성 성공 케이스")
    @Nested
    class successCase {

        @DisplayName("유효한 경로로 즐겨찾기 생성 시 즐겨찾기가 성공한다.")
        @Test
        void 즐겨찾기_생성() {
            //given
            var 신규_즐겨찾기 = new FavoriteRequest(교대역.getId(), 양재역.getId());
            var 로그인_정보 =  new LoginMember("testemail@test.com");

            //when
            var 즐겨찾기_생성_결과 = favoriteService.createFavorite(신규_즐겨찾기, 로그인_정보);

            //then
            assertThat(즐겨찾기_생성_결과).isNotNull();
        }

        @DisplayName("로그인 한 회원의 즐겨찾기 조회 시 즐겨찾기 목록이 반환된다.")
        @Test
        void 즐겨찾기_조회() {
            // TODO: 즐겨찾기 생성이 필요한 경우 이렇게 테스트 메서드를 호출해도 될까?
            즐겨찾기_생성();

            var 로그인_정보 =  new LoginMember("testemail@test.com");
            var 즐겨찾기_목록 = favoriteService.findFavorites(로그인_정보);

            assertThat(즐겨찾기_목록.size()).isGreaterThan(0);
        }

        @DisplayName("자신이 등록한 즐겨찾기를 삭제하면 즐겨찾기가 삭제된다")
        @Test
        void 즐겨찾기_삭제() {
            //given
            var 신규_즐겨찾기 = new FavoriteRequest(교대역.getId(), 양재역.getId());
            var 로그인_정보 =  new LoginMember("testemail@test.com");
            var 즐겨찾기_생성_결과 = favoriteService.createFavorite(신규_즐겨찾기, 로그인_정보);

            //when
            favoriteService.deleteFavorite(즐겨찾기_생성_결과.getId(), 로그인_정보);

            //then
            var 즐겨찾기_목록 = favoriteService.findFavorites(로그인_정보);
            assertThat(즐겨찾기_목록.stream()
                    .map(FavoriteResponse::getId)
                    .collect(Collectors.toList()))
                    .doesNotContain(즐겨찾기_생성_결과.getId());
        }
    }

    @DisplayName("즐겨찾기 생성 실패 케이스")
    @Nested
    class failureCase {

        @DisplayName("존재하지 않는 경로를 즐겨찾기로 등록 시 실패한다.")
        @Test
        void 없는_경로를_등록() {
            //given
            var 없는_경로_즐겨찾기 = new FavoriteRequest(교대역.getId(), 석남역.getId());
            var 로그인_정보 = new LoginMember("testemail@test.com");

            //then
            assertThatThrownBy(() -> favoriteService.createFavorite(없는_경로_즐겨찾기, 로그인_정보)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 등록한 경로를 다시 즐겨찾기로 등록 시 실패한다.")
        @Test
        void 등록한_즐겨찾기를_재등록() {
            //given
            var 이미_등록한_즐겨찾기 = new FavoriteRequest(교대역.getId(), 강남역.getId());
            var 로그인_정보 = new LoginMember("testemail@test.com");

            //then
            assertThatThrownBy(() -> favoriteService.createFavorite(이미_등록한_즐겨찾기, 로그인_정보)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("자신이 등록하지 않은 즐겨찾기를 삭제 시 실패한다.")
        @Test
        void 다른_회원의_즐겨찾기를_삭제() {
            //given
            var 회원1_로그인_정보 = new LoginMember("testemail@test.com");
            var 회원2_로그인_정보 = new LoginMember("testemail2@test.com");

            //then
            FavoriteResponse favoriteResponse = favoriteService.findFavorites(회원1_로그인_정보).stream().findFirst().get();
            assertThatThrownBy(() -> favoriteService.deleteFavorite(favoriteResponse.getId(), 회원2_로그인_정보)).isInstanceOf(AuthenticationException.class);
        }
    }
}
