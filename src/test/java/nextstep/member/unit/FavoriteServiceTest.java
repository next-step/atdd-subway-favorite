package nextstep.member.unit;

import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.exception.ErrorCode;
import nextstep.member.application.exception.FavoriteException;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("즐겨찾기 서비스 테스트")
public class FavoriteServiceTest {
    @Mock
    MemberService memberService;
    @Mock
    StationService stationService;
    @Mock
    PathService pathService;
    @Mock
    FavoriteRepository favoriteRepository;
    @InjectMocks
    FavoriteService favoriteService;


    private Station 교대역 = new Station("교대역");
    private Station 양재역 = new Station("양재역");

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    public static final Member member = new Member(EMAIL, PASSWORD, AGE);

    @Test
    @DisplayName("즐겨찾기 생성")
    void createFavorite() {
        Favorite expect = new Favorite(member, 교대역, 양재역);

        when(memberService.findMemberByEmail(EMAIL)).thenReturn(new Member(EMAIL, PASSWORD, AGE));
        when(stationService.findById(1L)).thenReturn(교대역);
        when(stationService.findById(3L)).thenReturn(양재역);
        when(favoriteRepository.save(expect)).thenReturn(expect);
        when(pathService.hasPath(1L, 3L)).thenReturn(true);

        UserPrincipal userPrincipal = new UserPrincipal(EMAIL, "ROLE_MEMBER");
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 3L);
        Favorite favorite = favoriteService.create(userPrincipal, favoriteRequest);

        assertThat(favorite).isEqualTo(expect);
    }

    @Test
    @DisplayName("즐겨찾기 생성 실패")
    void createFavoriteFail() {
        when(pathService.hasPath(1L, 3L)).thenReturn(false);

        UserPrincipal userPrincipal = new UserPrincipal(EMAIL, "ROLE_MEMBER");
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 3L);

        assertThatThrownBy(() -> favoriteService.create(userPrincipal, favoriteRequest))
                .isInstanceOf(FavoriteException.class)
                .hasMessage(ErrorCode.CANNOT_ADD_NOT_EXIST_PATH.getMessage());
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void deleteFavorite() {
        when(memberService.findMemberByEmail(EMAIL)).thenReturn(new Member(EMAIL, PASSWORD, AGE, new Favorite(1L, 교대역, 양재역)));

        UserPrincipal userPrincipal = new UserPrincipal(EMAIL, "ROLE_MEMBER");
        assertThatNoException().isThrownBy(() -> favoriteService.delete(userPrincipal,1L));
    }

    @Test
    @DisplayName("즐겨찾기 삭제 실패")
    void deleteFavoriteFail() {
        when(memberService.findMemberByEmail(EMAIL)).thenReturn(new Member(EMAIL, PASSWORD, AGE, new Favorite(1L, 교대역, 양재역)));

        UserPrincipal userPrincipal = new UserPrincipal(EMAIL, "ROLE_MEMBER");
        assertThatThrownBy(() -> favoriteService.delete(userPrincipal,2L))
                .isInstanceOf(FavoriteException.class)
                .hasMessage(ErrorCode.CANNOT_DELETE_NOT_EXIST_FAVORITE.getMessage());
    }
}
