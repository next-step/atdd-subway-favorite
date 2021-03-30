package nextstep.subway.favorite.application;

import nextstep.subway.exception.NotExistsStationException;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class FavoriteServiceTest {

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    StationRepository stationRepository;

    private static final LoginMember USER = new LoginMember(1L, "email@email.com", "qwer123", 20);
    private Station 강남역, 역삼역, 양재역;
    private FavoriteRequest favoriteRequest;

    @BeforeEach
    void setUp(){
        // given
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        양재역 = new Station("양재역");
        saveStations(강남역, 역삼역, 양재역);
        favoriteRequest = new FavoriteRequest(강남역.getId(), 역삼역.getId());
    }

    void saveStations(Station ... stations){
        for(Station station : stations){
            stationRepository.save(station);
        }
    }

    @DisplayName("즐겨찾기 등록/조회/삭제 테스트")
    @Test
    void manageFavoriteTest(){

        // when 즐겨찾기 등록
        FavoriteResponse favorite = favoriteService.createFavorite(USER, favoriteRequest);

        // then
        assertThat(favorite.getSource().getId()).isEqualTo(강남역.getId());
        assertThat(favorite.getTarget().getId()).isEqualTo(역삼역.getId());

        // when 즐겨찾기 조회
        List<FavoriteResponse> favorites = favoriteService.findFavorites(USER);

        // then
        assertThat(favorites)
                .map(it -> it.getSource().getId())
                .contains(강남역.getId());
        assertThat(favorites)
                .map(it -> it.getSource().getId())
                .doesNotContain(양재역.getId());

        // when 즐겨찾기 삭제
        favoriteService.deleteFavorite(USER, favorites.get(0).getId());
        favorites = favoriteService.findFavorites(USER);

        // then
        assertThat(favorites)
                .map(it -> it.getSource().getId())
                .doesNotContain(강남역.getId());

    }

    @DisplayName("[예외처리] 존재하지 않는 역 아이디 등록")
    @Test
    void createFavoriteWithNotExistsStation(){
        // given
        favoriteRequest = new FavoriteRequest(100L, 역삼역.getId());

        // when + then
        assertThatThrownBy(() -> {
            favoriteService.createFavorite(USER, favoriteRequest);
        }).isInstanceOf(NotExistsStationException.class);
    }

    @DisplayName("[예외처리] 로그인한 계정의 즐겨찾기가 아니면 예외발생")
    @Test
    void checkFavoriteOfMine(){
        // when + then
        assertThatThrownBy(() -> {
            favoriteService.checkFavoriteOfMine(1L, 2L);
        }).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("[예외처리] 존재하지 않는 즐겨찾기 제거")
    @Test
    void deleteFavoriteWithNotExistsFavorite(){
        // when + then
        assertThatThrownBy(() -> {
            favoriteService.deleteFavorite(USER, 100L);
        }).isInstanceOf(RuntimeException.class);
    }

}