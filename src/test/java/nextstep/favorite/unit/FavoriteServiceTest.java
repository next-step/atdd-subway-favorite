package nextstep.favorite.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.line.LineRepository;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class FavoriteServiceTest {

    @Autowired
    FavoriteService favoriteService;
    @Autowired
    LineRepository lineRepository;
    @Autowired
    StationRepository stationRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FavoriteRepository favoriteRepository;

    Station 교대역;
    Station 강남역;
    Station 양재역;
    Station 남부터미널역;

    Member member;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        stationRepository.save(교대역);
        stationRepository.save(강남역);
        stationRepository.save(양재역);
        stationRepository.save(남부터미널역);

        Line 이호선 = new Line("2호선", "green");
        lineRepository.save(이호선);
        이호선.generateSection(10, 교대역, 강남역);

        Line 신분당선 = new Line("신분당선", "red");
        lineRepository.save(신분당선);
        신분당선.generateSection(10, 강남역, 양재역);

        Line 삼호선 = new Line("3호선", "orange");
        lineRepository.save(삼호선);
        삼호선.generateSection(2, 교대역, 남부터미널역);
        삼호선.generateSection(3, 남부터미널역, 양재역);

        member = new Member("email@email.com", "1234", 25);
        memberRepository.save(member);
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void createFavorite() {
        //given
        FavoriteRequest request = new FavoriteRequest(교대역.getId(), 양재역.getId());
        LoginMember loginMember = new LoginMember(member.getEmail());

        //when
        Long id = favoriteService.createFavorite(request, loginMember);

        //then
        Favorite savedFavorite = favoriteRepository.findById(id).get();
        assertThat(savedFavorite.getSourceStationId()).isEqualTo(교대역.getId());
        assertThat(savedFavorite.getTargetStationId()).isEqualTo(양재역.getId());
    }

    @DisplayName("연결되어있지않은 역들을 즐겨찾기로 생성시 예외 발생")
    @Test
    void createDisConnectedStationsFavorite() {
        //given
        Station 건대입구역 = new Station("건대입구역");
        Station 성수역 = new Station("성수역");
        stationRepository.save(건대입구역);
        stationRepository.save(성수역);
        FavoriteRequest request = new FavoriteRequest(건대입구역.getId(), 성수역.getId());
        LoginMember loginMember = new LoginMember(member.getEmail());

        //when
        assertThatThrownBy(() -> favoriteService.createFavorite(request, loginMember))
                .isExactlyInstanceOf(PathException.class)
                .hasMessage("연결되어있지 않은 출발역과 도착역의 경로는 조회할 수 없습니다.");
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void showFavorites() {
        //given
        FavoriteRequest request = new FavoriteRequest(교대역.getId(), 양재역.getId());
        LoginMember loginMember = new LoginMember(member.getEmail());
        favoriteService.createFavorite(request, loginMember);

        //when
        List<FavoriteResponse> favorites = favoriteService.findFavorites();

        //then
        assertThat(favorites).hasSize(1);
    }
}
