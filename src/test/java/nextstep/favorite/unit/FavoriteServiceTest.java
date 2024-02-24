package nextstep.favorite.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.line.domain.Section;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static nextstep.utils.fixture.LineFixture.*;
import static nextstep.utils.fixture.SectionFixture.추가구간_엔티티;
import static nextstep.utils.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private FavoriteService favoriteService;
    private Member 테스트계정;
    private Line 신분당선;

    /**
     *      한남역 --- *경의중앙선* --- 서빙고역
     *
     *      신논현역 --- *신분당선* --- 논현역
     *        |
     *        |
     *     *신분당선*
     *        |
     *        |
     *      강남역 ---- *2호선* ------ 역삼역
     *
     */
    @BeforeEach
    void setUp() {
        테스트계정 = memberRepository.save(new Member("test@test.com", "test1234", 18));
        신분당선 = 신분당선_엔티티(논현역_엔티티, 신논현역_엔티티);
        Line 이호선 = 이호선_엔티티(강남역_엔티티, 역삼역_엔티티);
        Line 경의중앙선 = 경의중앙선_엔티티(한남역_엔티티, 서빙고역_엔티티);

        Section 신분당선_추가구간 = 추가구간_엔티티(신논현역_엔티티, 강남역_엔티티);
        신분당선.addSection(신분당선_추가구간);

        lineRepository.save(신분당선);
        lineRepository.save(경의중앙선);
        lineRepository.save(이호선);
    }

    @Nested
    class AddTest {
        /**
         * Given
         * When
         * Then
         */
        @Test
        @DisplayName("즐겨찾기 추가 성공")
        void succeed() {
            // Given
            FavoriteRequest createFavoriteRequest = new FavoriteRequest(강남역_엔티티.getId(), 논현역_엔티티.getId());

            // When
            favoriteService.createFavorite(createFavoriteRequest);

            // Then
            List<Favorite> favorites = favoriteRepository.findAll();
            assertThat(favorites.size()).isEqualTo(1);
            assertThat(favorites.get(0).getMember()).isEqualTo(테스트계정);
            assertThat(favorites.get(0).getSourceStation()).isEqualTo(강남역_엔티티);
            assertThat(favorites.get(0).getSourceStation()).isEqualTo(논현역_엔티티);
        }

        @Test
        @DisplayName("동일한 역을 출발,도착 지점으로 즐겨찾기 추가 불가")
        void failForSameSourceAndTarget() {
            // Given
            FavoriteRequest createFavoriteRequest = new FavoriteRequest(강남역_엔티티.getId(), 강남역_엔티티.getId());

            // When Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> favoriteService.createFavorite(createFavoriteRequest),
                    "should throw"
            );

        }

        @Test
        @DisplayName("이미 등록된 즐겨찾기라면 추가 불가")
        void failForAlreadyExist() {
            // Given
            FavoriteRequest createFavoriteRequest = new FavoriteRequest(강남역_엔티티.getId(), 논현역_엔티티.getId());
            favoriteService.createFavorite(createFavoriteRequest);

            // When // Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> favoriteService.createFavorite(createFavoriteRequest),
                    "should throw"
            );
        }

        @Test
        @DisplayName("즐겨찾기를 생성하고자 하는 멤버가 존재하지 않는 멤버라면 추가 불가")
        void failForMemberNotFound() {
            // Add later
        }

        @Test
        @DisplayName("존재하지 않는 역에 대해 즐겨찾기 추가 불가")
        void failForStationNotFound() {
            FavoriteRequest createFavoriteRequest = new FavoriteRequest(강남역_엔티티.getId(), 404L);

            // When // Then
            assertThrows(
                    EntityNotFoundException.class,
                    () -> favoriteService.createFavorite(createFavoriteRequest),
                    "should throw"
            );
        }

        @Test
        @DisplayName("연결되지 않은 두 역에 대해 즐겨찾기 추가 불가")
        void failForPathNotFound() {
            FavoriteRequest createFavoriteRequest = new FavoriteRequest(강남역_엔티티.getId(), 서빙고역_엔티티.getId());

            // When // Then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> favoriteService.createFavorite(createFavoriteRequest),
                    "should throw"
            );
        }
    }

    @Nested
    class FindTest {
        @Test
        @DisplayName("즐겨찾기 리스트 조회 성공")
        void succeed() {
            // Given
            favoriteRepository.save(new Favorite(논현역_엔티티, 역삼역_엔티티, 테스트계정));

            // When
            List<FavoriteResponse> favorites = favoriteService.findFavorites();

            // Then
            assertThat(favorites).hasSize(1);
            assertThat(favorites.get(0).getSource()).isEqualTo(논현역_엔티티);
            assertThat(favorites.get(0).getTarget()).isEqualTo(역삼역_엔티티);
        }

        @Test
        @DisplayName("즐겨찾기가 존재하지 않아도 조회 성공")
        void succeedButEmpty() {
            // When
            List<FavoriteResponse> favorites = favoriteService.findFavorites();

            // Then
            assertThat(favorites).hasSize(0);
        }

        @Test
        @DisplayName("추가한 즐겨찾기 중 시작,출발 역이 삭제된 게 있더라도 성공")
        void succeedButDeletedStationIncluded() {
            // Given
            favoriteRepository.save(new Favorite(논현역_엔티티, 역삼역_엔티티, 테스트계정));
            신분당선.deleteStation(논현역_엔티티.getId());
            lineRepository.save(신분당선);

            // When
            List<FavoriteResponse> favorites = favoriteService.findFavorites();

            // When // Then
            assertThat(favorites).hasSize(1);
            assertThat(favorites.get(0).getSource()).isNull();
        }
    }

    @Nested
    class DeleteTest {
        @Test
        @DisplayName("즐겨찾기 삭제 성공")
        void succeed() {
            // Given
            favoriteRepository.save(new Favorite(논현역_엔티티, 역삼역_엔티티, 테스트계정));

            // When
            favoriteService.deleteFavorite(1L);

            // Then
            assertThat(favoriteRepository.findAll()).hasSize(0);
        }

        @Test
        @DisplayName("즐겨찾기를 삭제하려는 멤버가 삭제하려는 즐겨찾기의 멤버와 다르면 실패")
        void failForDifferentMember() {
            // Add later
        }

        @Test
        @DisplayName("삭제하려는 즐겨찾기를 찾을 수 없다면 실패")
        void failForNotFoundFavorite() {
            // Given
            favoriteRepository.save(new Favorite(논현역_엔티티, 역삼역_엔티티, 테스트계정));

            // When // Then
            assertThrows(
                    EntityNotFoundException.class,
                    () -> favoriteService.deleteFavorite(404L),
                    "should throw"
            );
        }
    }
}
