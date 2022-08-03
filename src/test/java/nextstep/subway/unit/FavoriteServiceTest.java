package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static nextstep.subway.fixture.MockMember.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class FavoriteServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private EntityManager em;

    private Station 강남역;
    private Station 역삼역;
    private Member member;

    private FavoriteResponse favorite;


    @BeforeEach
    void setUp() {
        member = memberRepository.save(ADMIN.toMember());

        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));

        favorite = favoriteService.createFavorite(member.getEmail(), new FavoriteRequest(강남역.getId(), 역삼역.getId()));
        em.clear();
    }

    @Test
    @DisplayName("즐겨찾기가 생성 된다.")
    void createFavoriteTest() {
        // then
        assertAll(
                () -> assertThat(favorite.getId()).isNotNull(),

                () -> assertThat(favorite.getSource().getId()).isEqualTo(강남역.getId()),
                () -> assertThat(favorite.getSource().getName()).isEqualTo(강남역.getName()),
                () -> assertThat(favorite.getSource().getCreatedDate()).isNotNull(),
                () -> assertThat(favorite.getSource().getModifiedDate()).isNotNull(),

                () -> assertThat(favorite.getTarget().getId()).isEqualTo(역삼역.getId()),
                () -> assertThat(favorite.getTarget().getName()).isEqualTo(역삼역.getName()),
                () -> assertThat(favorite.getTarget().getCreatedDate()).isNotNull(),
                () -> assertThat(favorite.getTarget().getModifiedDate()).isNotNull()
        );
    }

    @Test
    @DisplayName("없는 역을 즐겨찾기에 등록하면 에러가 발생한다.")
    void createFavoriteFailNonExistStationTest() {
        // then
        Station 없는역 = new Station(-1L, "없는역");
        assertThatIllegalArgumentException().isThrownBy(() -> favoriteService.createFavorite(member.getEmail(), new FavoriteRequest(없는역.getId(), 역삼역.getId())));
        assertThatIllegalArgumentException().isThrownBy(() -> favoriteService.createFavorite(member.getEmail(), new FavoriteRequest(강남역.getId(), 없는역.getId())));
    }

    @Test
    @DisplayName("즐겨찾기가 조회된다.")
    void getFavoritesTest() {
        // when
        List<FavoriteResponse> favorites = favoriteService.getFavorites(member.getEmail());

        // then
        assertAll(
                () -> assertThat(favorites).hasSize(1),
                () -> assertThat(favorites.get(0).getSource().getId()).isEqualTo(강남역.getId()),
                () -> assertThat(favorites.get(0).getTarget().getId()).isEqualTo(역삼역.getId())
        );
    }

    @Test
    @DisplayName("즐겨찾기가 삭제된다.")
    void deleteFavoriteTest() {
        Long favoriteId = favorite.getId();

        // when
        favoriteService.deleteFavorite(member.getEmail(), favoriteId);
        em.flush();

        // then
        assertThat(favoriteService.getFavorites(member.getEmail())).isEmpty();
    }

    @Test
    @DisplayName("즐겨찾기 번호가 잘못되면 예외가 발생한다.")
    void deleteFavoriteFailTest() {
        // given
        Long favoriteId = -1L;

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> favoriteService.deleteFavorite(member.getEmail(), favoriteId));
    }

}
