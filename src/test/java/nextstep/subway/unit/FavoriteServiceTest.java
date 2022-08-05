package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.util.List;
import nextstep.auth.user.User;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FavoriteServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationService stationService;

    @Autowired
    private FavoriteService favoriteService;

    User user;
    FavoriteRequest favoriteRequest;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member());
        final StationResponse 강남역 = stationService.saveStation(new StationRequest("강남역"));
        final StationResponse 양재역 = stationService.saveStation(new StationRequest("양재역"));
        user = User.of("admin@email.com", "password", List.of(RoleType.ROLE_ADMIN.name()));
        favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
    }

    @Test
    @DisplayName("즐겨찾기 저장")
    void createFavorite() {
        //when
        final FavoriteResponse favorite = favoriteService.createFavorite(user, favoriteRequest);

        //then
        assertThat(favorite.getSource().getName()).isEqualTo("강남역");
        assertThat(favorite.getTarget().getName()).isEqualTo("양재역");
    }

    @Test
    @DisplayName("즐겨찾기 전체 조회")
    void getFavorite() {
        //given
        favoriteService.createFavorite(user, favoriteRequest);

        //when
        final List<FavoriteResponse> favorites = favoriteService.getFavorites(user);

        //then
        assertThat(favorites).hasSize(1);
    }

    @Test
    @DisplayName("즐겨찾기 삭제 시 멤버의 즐겨찾기가 아닌경우 예외")
    void notMemberFavoriteException() {
        //given
        favoriteService.createFavorite(user, favoriteRequest);
        final User userDetails = User.of("member@email.com", "password", List.of(RoleType.ROLE_MEMBER.name()));

        //then
        assertThatExceptionOfType(RuntimeException.class)
            .isThrownBy(() -> favoriteService.removeFavorites(userDetails, 1))
            .withMessage("멤버의 즐겨찾기 id가 아닙니다.");
    }

    @Test
    @DisplayName("즐겨찾기 삭제 시 멤버의 즐겨찾기가 아닌경우 예외")
    void removeFavorite() {
        //given
        final FavoriteResponse favorite = favoriteService.createFavorite(user, favoriteRequest);

        //then
        favoriteService.removeFavorites(user, favorite.getId());

        favoriteRepository.flush();

        final List<FavoriteResponse> favorites = favoriteService.getFavorites(user);

        assertThat(favorites).hasSize(0);
    }

}