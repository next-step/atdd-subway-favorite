package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.application.dto.FavoriteRequest;
import nextstep.subway.favorite.application.dto.FavoriteResponse;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.exception.DuplicateFavoriteException;
import nextstep.subway.favorite.exception.FavoriteNotFoundException;
import nextstep.subway.favorite.exception.UnauthorizedFavoriteAccessException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.common.exception.SubwayExceptionType;
import nextstep.subway.line.domain.entity.Line;
import nextstep.subway.line.domain.entity.LineSection;
import nextstep.subway.line.domain.entity.LineSections;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockTest {

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PathService pathService;

    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    private Long 교대역_id = 1L;
    private Long 강남역_id = 2L;
    private Long 양재역_id = 3L;
    private Long 남부터미널역_id = 4L;

    private String email = "test@example.com";
    private String password = "password";

    private Member member;
    private LoginMember loginMember;

    private FavoriteRequest favoriteRequest;
    private Favorite favorite;
    private Long favoriteId = 1L;

    @BeforeEach
    void setUp() {

        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        Line 이호선 = new Line("이호선", "bg-red-600", new LineSections());
        Line 신분당선 = new Line("신분당선", "bg-green-600", new LineSections());
        Line 삼호선 = new Line("삼호선", "bg-orange-600", new LineSections());

        이호선.addSection(new LineSection(이호선, 교대역, 강남역, 10L));
        신분당선.addSection(new LineSection(신분당선, 강남역, 양재역, 10L));
        삼호선.addSection(new LineSection(삼호선, 교대역, 남부터미널역, 2L));
        삼호선.addSection(new LineSection(삼호선, 남부터미널역, 양재역, 10L));

        member = new Member(email, password, 30);
        loginMember = new LoginMember(email);

        favoriteRequest = new FavoriteRequest(교대역_id, 강남역_id);
        favorite = new Favorite(member, 교대역, 강남역);
    }

    @Nested
    @DisplayName("createFavorite 테스트")
    class CreateFavorite {

        @Test
        @DisplayName("즐겨찾기를 생성한다")
        void createFavorite() {
            // given
            when(stationRepository.findByIdOrThrow(교대역_id)).thenReturn(교대역);
            when(stationRepository.findByIdOrThrow(강남역_id)).thenReturn(강남역);
            when(memberRepository.findByEmailOrElseThrow(email)).thenReturn(member);
            when(pathService.existsPath(교대역_id, 강남역_id)).thenReturn(true);
            when(
                favoriteRepository.existsByMemberAndSourceStationAndTargetStation(member, 교대역, 강남역))
                .thenReturn(false);
            when(favoriteRepository.save(any(Favorite.class))).thenAnswer(
                invocation -> invocation.getArgument(0));

            // when
            FavoriteResponse response = favoriteService.createFavorite(loginMember,
                favoriteRequest);

            // then
            assertThat(response.getSource().getId()).isEqualTo(교대역.getId());
            assertThat(response.getTarget().getId()).isEqualTo(강남역.getId());
        }

        @Test
        @DisplayName("중복된 즐겨찾기를 생성하려고 하면 예외를 발생시킨다")
        void throwExceptionWhenDuplicateFavorite() {
            // given
            when(stationRepository.findByIdOrThrow(교대역_id)).thenReturn(교대역);
            when(stationRepository.findByIdOrThrow(강남역_id)).thenReturn(강남역);
            when(memberRepository.findByEmailOrElseThrow(email)).thenReturn(member);
            when(pathService.existsPath(교대역_id, 강남역_id)).thenReturn(true);
            when(
                favoriteRepository.existsByMemberAndSourceStationAndTargetStation(member, 교대역, 강남역))
                .thenReturn(true);

            // when, then
            assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, favoriteRequest))
                .isInstanceOf(DuplicateFavoriteException.class);
        }

        @Test
        @DisplayName("경로가 존재하지 않을 경우 SubwayExceptionType.PATH_NOT_FOUND 예외를 발생시킨다")
        void throwExceptionWhenPathNotFound() {
            // given
            Station disconnectedStation = new Station("공덕역");
            Long disconnectedStation_id = 5L;
            FavoriteRequest disconnectedFavoriteRequest = new FavoriteRequest(교대역_id, disconnectedStation_id);

            when(stationRepository.findByIdOrThrow(교대역_id)).thenReturn(교대역);
            when(stationRepository.findByIdOrThrow(disconnectedStation_id)).thenReturn(
                disconnectedStation);
            when(memberRepository.findByEmailOrElseThrow(email)).thenReturn(member);
            when(pathService.existsPath(교대역_id, disconnectedStation_id)).thenReturn(false);

            // when, then
            assertThatThrownBy(() -> favoriteService.createFavorite(loginMember,
                disconnectedFavoriteRequest))
                .isInstanceOf(SubwayException.class)
                .hasMessage(SubwayExceptionType.PATH_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("findFavorites 테스트")
    class FindFavorites {

        @Test
        @DisplayName("즐겨찾기를 조회한다")
        void findFavorites() {
            // given
            when(memberRepository.findByEmailOrElseThrow(email)).thenReturn(member);
            when(favoriteRepository.findAllByMember(member))
                .thenReturn(Stream.of(favorite).collect(Collectors.toList()));

            // when
            List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);

            // then
            assertThat(favorites).hasSize(1);
            assertThat(favorites.get(0).getSource().getId()).isEqualTo(교대역.getId());
            assertThat(favorites.get(0).getTarget().getId()).isEqualTo(강남역.getId());
        }
    }

    @Nested
    @DisplayName("deleteFavorite 테스트")
    class DeleteFavorite {

        @Test
        @DisplayName("즐겨찾기를 삭제한다")
        void deleteFavorite() {
            // given
            when(favoriteRepository.findByIdOrElseThrow(favoriteId)).thenReturn(favorite);
            when(memberRepository.findByEmailOrElseThrow(email)).thenReturn(member);

            // when
            favoriteService.deleteFavorite(loginMember, favoriteId);

            // then
            verify(favoriteRepository).delete(favorite);
        }

        @Test
        @DisplayName("다른 사용자의 즐겨찾기를 삭제하려고 하면 예외를 발생시킨다")
        void throwExceptionWhenUnauthorizedAccess() {
            // given
            Member otherMember = new Member("other@example.com", "password", 25);
            Favorite otherFavorite = new Favorite(otherMember, 교대역, 강남역);
            Long otherFavoriteId = 2L;
            when(favoriteRepository.findByIdOrElseThrow(otherFavoriteId)).thenReturn(otherFavorite);
            when(memberRepository.findByEmailOrElseThrow(email)).thenReturn(member);

            // when, then
            assertThatThrownBy(() -> favoriteService.deleteFavorite(loginMember, otherFavoriteId))
                .isInstanceOf(UnauthorizedFavoriteAccessException.class);
        }

        @Test
        @DisplayName("삭제하려는 데이터가 존재하지 않는 경우 예외를 발생시킨다")
        void throwExceptionWhenFavoriteNotFound() {
            // given
            when(favoriteRepository.findByIdOrElseThrow(favoriteId)).thenThrow(
                new FavoriteNotFoundException(favoriteId));

            // when, then
            assertThatThrownBy(() -> favoriteService.deleteFavorite(loginMember, favoriteId))
                .isInstanceOf(FavoriteNotFoundException.class);
        }
    }
}
