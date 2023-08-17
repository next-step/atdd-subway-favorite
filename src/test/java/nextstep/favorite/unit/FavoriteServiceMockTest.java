package nextstep.favorite.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.repository.FavoriteRepository;
import nextstep.favorite.unit.fixture.FavoriteFixture;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.member.exception.NoExistedMemberException;
import nextstep.member.unit.fixture.MemberFixture;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.BadRequestPathException;
import nextstep.subway.exception.NullPointerSectionsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static nextstep.subway.unit.fixture.LineFixture.지하철_노선_생성;
import static nextstep.subway.unit.fixture.StationFixture.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("즐겨찾기 서비스 단위 테스트 with Mock")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;
    private static final int DEFAULT_DISTANCE = 10;

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private StationService stationService;
    @Mock
    private PathService pathService;
    @InjectMocks
    private FavoriteService favoriteService;

    private Line 신분당선, 이호선;
    private Station 교대역, 강남역, 양재역, 신도림역;
    private Member 회원;

    @BeforeEach
    void set() {
        회원 = MemberFixture.회원_생성(EMAIL, PASSWORD, AGE);
        교대역 = 지하철역_생성("교대역");
        강남역 = 지하철역_생성("강남역");
        양재역 = 지하철역_생성("양재역");
        신도림역 = 지하철역_생성("신도림역");

        신분당선 = 지하철_노선_생성("신분당선", "bg-red-600", 강남역, 양재역, DEFAULT_DISTANCE);
        이호선 = 지하철_노선_생성("2호선", "bg-red-600", 강남역, 교대역, DEFAULT_DISTANCE);

    }

    @DisplayName("즐겨찾기를 등록한다.")
    @Test
    void createFavorites() {
        Favorite favorite = FavoriteFixture.즐겨찾기_등록(회원, 교대역, 양재역);
        given(memberService.findMemberByEmail(EMAIL)).willReturn(회원);
        given(stationService.getStations(교대역.getId())).willReturn(교대역);
        given(stationService.getStations(양재역.getId())).willReturn(양재역);
        given(favoriteRepository.save(any())).willReturn(favorite);

        FavoriteResponse favoriteResponse = favoriteService.createFavorite(EMAIL, new FavoriteRequest(교대역.getId(), 양재역.getId()));

        assertThat(favoriteResponse.getMemeber().getEmail()).isEqualTo(EMAIL);
        assertThat(favoriteResponse.getSource().getId()).isEqualTo(교대역.getId());
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(양재역.getId());
    }

    @DisplayName("즐겨찾기 등록에 실패한다. - 등록되지 않은 회원의 경우 에러를 던짐")
    @Test
    void createFavoritesExceptionWhenNoExistMember() {
        given(memberService.findMemberByEmail(any())).willThrow(new NoExistedMemberException(""));

        assertThrows(RuntimeException.class, () -> favoriteService.createFavorite(EMAIL, new FavoriteRequest(교대역.getId(), 양재역.getId())));
    }

    @DisplayName("즐겨찾기 등록에 실패한다. - 등록되지 않은 역의 경우 에러를 던짐")
    @Test
    void createFavoritesExceptionWhenNoExistStation() {
        given(memberService.findMemberByEmail(EMAIL)).willReturn(회원);
        given(stationService.getStations(교대역.getId())).willThrow(new NullPointerSectionsException(""));

        assertThrows(NullPointerSectionsException.class, () -> favoriteService.createFavorite(EMAIL, new FavoriteRequest(교대역.getId(), 양재역.getId())));
    }

    @DisplayName("즐겨찾기 등록에 실패한다. - 경로가 올바르지 않은 경우 에러를 던짐")
    @Test
    void createFavoritesExceptionWhenWrongPath() {
        given(memberService.findMemberByEmail(EMAIL)).willReturn(회원);
        given(stationService.getStations(교대역.getId())).willReturn(교대역);
        given(stationService.getStations(신도림역.getId())).willReturn(신도림역);
        given(pathService.getPaths(교대역.getId(), 신도림역.getId())).willThrow(new BadRequestPathException(""));

        assertThrows(BadRequestPathException.class, () -> favoriteService.createFavorite(EMAIL, new FavoriteRequest(교대역.getId(), 신도림역.getId())));
    }

    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void findFavorite() {
        Favorite favorite = FavoriteFixture.즐겨찾기_등록(회원, 교대역, 양재역);

        given(memberService.findMemberByEmail(EMAIL)).willReturn(회원);
        given(favoriteRepository.findByMember(회원)).willReturn(List.of(favorite));

        FavoriteResponse favorite1 = favoriteService.findFavorite(EMAIL).get(0);
        assertThat(favorite1.getId()).isEqualTo(favorite1.getId());
    }

    @DisplayName("즐겨찾기 조회에 실패한다. -회원 정보를 찾을 수 없을 경우 에러를 던진다.")
    @Test
    void findFavoriteExceptionWhenNoExistedMember() {
        given(memberService.findMemberByEmail(any())).willThrow(new NoExistedMemberException(""));

        assertThrows(NoExistedMemberException.class, () -> favoriteService.findFavorite(" "));
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorites() {
        Favorite favorite = FavoriteFixture.즐겨찾기_등록(회원, 교대역, 양재역);
        given(memberService.findMemberByEmail(EMAIL)).willReturn(회원);
        given(favoriteRepository.findById(favorite.getId())).willReturn(Optional.of(favorite));

        favoriteService.deleteFavorites(EMAIL, favorite.getId());

        verify(favoriteRepository).delete(favorite);
    }

    @DisplayName("즐겨찾기 삭제에 실패한다. - 회원을 찾을 수 없을 경우 에러를 던진다.")
    @Test
    void deleteFavoritesExceptionWhenNoExistedMember() {
        given(memberService.findMemberByEmail(any())).willThrow(new NoExistedMemberException(""));

        assertThrows(NoExistedMemberException.class, () -> favoriteService.findFavorite(" "));
    }
}
