package nextstep.favorite.application;

import static nextstep.favorite.application.DefaultFavoriteService.*;
import static nextstep.member.application.MemberService.*;
import static nextstep.subway.application.DefaultLineCommandService.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.domain.FavoriteService;
import nextstep.member.AuthenticationException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.model.Line;
import nextstep.subway.domain.model.Section;
import nextstep.subway.domain.model.Station;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.domain.repository.StationRepository;

@DisplayName("DefaultFavoriteService 도메인 테스트")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DefaultFavoriteServiceTest {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private FavoriteService favoriteService;

    private Member member;
    private Station yeoksamStation;
    private Station gangnamStation;
    private Station yangjaeStation;
    private Station suwonStation;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "email@example.com", "password", 20);
        memberRepository.save(member);

        yeoksamStation = new Station(1L, "역삼역");
        gangnamStation = new Station(2L, "강남역");
        yangjaeStation = new Station(3L, "양재역");
        suwonStation = new Station(4L, "수원역");

        stationRepository.save(yeoksamStation);
        stationRepository.save(gangnamStation);
        stationRepository.save(yangjaeStation);
        stationRepository.save(suwonStation);

        Line secondLine = new Line(1L, "2호선", "green");
        Line newBundangLine = new Line(2L, "신분당선", "red");

        Section yeoksam_gangnam = new Section(1L, secondLine, yeoksamStation, gangnamStation, 10);
        Section gangnam_yangjae = new Section(2L, newBundangLine, gangnamStation, yangjaeStation, 5);

        secondLine.addSection(yeoksam_gangnam);
        newBundangLine.addSection(gangnam_yangjae);

        lineRepository.save(secondLine);
        lineRepository.save(newBundangLine);
    }

    @Nested
    @DisplayName("즐겨찾기 생성")
    class CreateFavorites {
        @Test
        @DisplayName("즐겨찾기를 성공적으로 생성한다")
        void createFavoriteSuccess() {
            // given
            FavoriteRequest favoriteRequest = new FavoriteRequest(yeoksamStation.getId(), yangjaeStation.getId());
            LoginMember loginMember = new LoginMember("email@example.com");

            // when
            favoriteService.createFavorite(favoriteRequest, loginMember);

            // then
            List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
            assertThat(favorites).hasSize(1);
            assertThat(favorites.get(0).getSourceStationId()).isEqualTo(yeoksamStation.getId());
            assertThat(favorites.get(0).getTargetStationId()).isEqualTo(yangjaeStation.getId());
        }

        @Test
        @DisplayName("즐겨찾기 생성 시 source 역이 존재하지 않는 경우 예외가 발생한다")
        void createFavoriteSourceStationNotFound() {
            // given
            FavoriteRequest favoriteRequest = new FavoriteRequest(999L, yangjaeStation.getId());
            LoginMember loginMember = new LoginMember("email@example.com");

            // when & then
            assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> favoriteService.createFavorite(favoriteRequest, loginMember))
                .withMessageContaining(STATION_NOT_FOUND_MESSAGE);
        }

        @Test
        @DisplayName("즐겨찾기 생성 시 target 역이 존재하지 않는 경우 예외 발생한다")
        void createFavoriteTargetStationNotFound() {
            // given
            FavoriteRequest favoriteRequest = new FavoriteRequest(yeoksamStation.getId(), 999L);
            LoginMember loginMember = new LoginMember("email@example.com");

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> favoriteService.createFavorite(favoriteRequest, loginMember))
                .withMessageContaining(STATION_NOT_FOUND_MESSAGE);
        }

        @Test
        @DisplayName("즐겨찾기 생성 시 경로가 존재하지 않는 경우 예외 발생한다")
        void createFavoritePathNotFound() {
            // given
            FavoriteRequest favoriteRequest = new FavoriteRequest(yeoksamStation.getId(), suwonStation.getId());
            LoginMember loginMember = new LoginMember("email@example.com");

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> favoriteService.createFavorite(favoriteRequest, loginMember))
                .withMessageContaining(PATH_NOT_FOUND_MESSAGE);
        }

        @Test
        @DisplayName("즐겨찾기를 생성할 때 회원이 존재하지 않는 경우 예외가 발생한다")
        void createFavoriteUnauthorizedMember() {
            // given
            FavoriteRequest favoriteRequest = new FavoriteRequest(yeoksamStation.getId(), yangjaeStation.getId());
            LoginMember unauthorizedMember = new LoginMember("unauthorized@example.com");

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> favoriteService.createFavorite(favoriteRequest, unauthorizedMember))
                .withMessage(MEMBER_NOT_FOUND_MESSAGE);
        }
    }

    @Nested
    @DisplayName("즐겨찾기 조회")
    class FindFavorites {
        @Test
        @DisplayName("즐겨찾기를 성공적으로 조회한다")
        void getFavoritesSuccess() {
            // given
            Favorite favorite = new Favorite(yeoksamStation.getId(), yangjaeStation.getId(), member.getId());
            favoriteRepository.save(favorite);

            LoginMember loginMember = new LoginMember("email@example.com");

            // when
            List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);

            // then
            assertThat(favorites).hasSize(1);
            assertThat(favorites.get(0).getSource().getName()).isEqualTo(yeoksamStation.getName());
            assertThat(favorites.get(0).getTarget().getName()).isEqualTo(yangjaeStation.getName());
        }

        @Test
        @DisplayName("즐겨찾기 조회 시 회원이 존재하지 않는 경우 예외가 발생한다")
        void getFavoritesMemberNotFound() {
            // given
            LoginMember unauthorizedMember = new LoginMember("unauthorized@example.com");

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> favoriteService.findFavorites(unauthorizedMember))
                .withMessage(MEMBER_NOT_FOUND_MESSAGE);
        }
    }

    @Nested
    @DisplayName("즐겨찾기 삭제")
    class DeleteFavorites {
        @Test
        @DisplayName("즐겨찾기를 성공적으로 삭제한다")
        void deleteFavoriteSuccess() {
            // given
            Favorite favorite = new Favorite(yeoksamStation.getId(), yangjaeStation.getId(), member.getId());
            favoriteRepository.save(favorite);
            LoginMember loginMember = new LoginMember("email@example.com");

            // when
            favoriteService.deleteFavorite(favorite.getId(), loginMember);

            // then
            List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
            assertThat(favorites).isEmpty();
        }

        @Test
        @DisplayName("즐겨 찾기 삭제 시 회원이 존재하지 않는 경우 예외가 발생한다")
        void deleteFavoriteUnauthorizedMember() {
            // given
            Favorite favorite = new Favorite(yeoksamStation.getId(), yangjaeStation.getId(), member.getId());
            favoriteRepository.save(favorite);

            LoginMember unauthorizedMember = new LoginMember("unauthorized@example.com");

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> favoriteService.deleteFavorite(favorite.getId(), unauthorizedMember))
                .withMessage(MEMBER_NOT_FOUND_MESSAGE);
        }
    }
}