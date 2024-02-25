package nextstep.favorite.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteCreateCommand;
import nextstep.favorite.application.dto.FavoriteDeleteCommand;
import nextstep.favorite.application.dto.FavoriteDto;
import nextstep.favorite.application.dto.FavoriteFindQuery;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.domain.exception.FavoriteBadRequestException;
import nextstep.favorite.domain.exception.FavoriteConflictException;
import nextstep.favorite.domain.exception.FavoriteMemberException;
import nextstep.favorite.domain.exception.FavoriteNotFoundException;
import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.line.domain.Section;
import nextstep.line.domain.SectionRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.exception.MemberNotFoundException;
import nextstep.path.domain.exception.PathNotFoundException;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.station.domain.exception.StationNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    private SectionRepository sectionRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private FavoriteService favoriteService;

    private Member 테스트계정;
    private Station 논현역;
    private Station 역삼역;

    /**
     *    신논현역 --- *신분당선* --- 논현역
     *      |
     *      |
     *   *신분당선*
     *      |
     *      |
     *    강남역 ---- *2호선* ------ 역삼역
     *
     */
    @BeforeEach
    void setUp() {
        테스트계정 = memberRepository.save(new Member("test@test.com", "test1234", 18));

        Station 강남역 = stationRepository.save(강남역_엔티티);
        논현역 = stationRepository.save(논현역_엔티티);
        Station 신논현역 = stationRepository.save(신논현역_엔티티);
        역삼역 = stationRepository.save(역삼역_엔티티);

        lineRepository.save(이호선_엔티티(강남역, 역삼역));

        Line 신분당선 = lineRepository.save(신분당선_엔티티(논현역, 신논현역));
        Section 신분당선_추가구간 = sectionRepository.save(추가구간_엔티티(신논현역, 강남역));

        신분당선.addSection(신분당선_추가구간);
        lineRepository.save(신분당선);
    }

    @Test
    @DisplayName("즐겨찾기 추가 성공")
    void succeedToAdd() {
        // Given
        FavoriteCreateCommand createFavoriteRequest = new FavoriteCreateCommand(
                논현역.getId(), 역삼역.getId(), 테스트계정.getEmail()
        );

        // When
        favoriteService.createFavorite(createFavoriteRequest);

        // Then
        List<Favorite> favorites = favoriteRepository.findAll();
        assertThat(favorites).hasSize(1);


        assertThat(favorites.get(0).getMember().getEmail()).isEqualTo(테스트계정.getEmail());
        assertThat(favorites.get(0).getSourceStation().getName()).isEqualTo(논현역.getName());
        assertThat(favorites.get(0).getTargetStation().getName()).isEqualTo(역삼역.getName());
    }


    @Test
    @DisplayName("동일한 역을 출발,도착 지점으로 즐겨찾기 추가 불가")
    void failToAddForSameSourceAndTarget() {
        // Given
        FavoriteCreateCommand createFavoriteRequest = new FavoriteCreateCommand(
                논현역.getId(), 논현역.getId(), 테스트계정.getEmail()
        );

        // When Then
        assertThrows(
                FavoriteBadRequestException.class,
                () -> favoriteService.createFavorite(createFavoriteRequest),
                "should throw"
        );
    }

    @Test
    @DisplayName("이미 등록된 즐겨찾기라면 추가 불가")
    void failToAddForAlreadyExist() {
        // Given
        FavoriteCreateCommand createFavoriteRequest = new FavoriteCreateCommand(
                논현역.getId(), 역삼역.getId(), 테스트계정.getEmail()
        );
        favoriteService.createFavorite(createFavoriteRequest);

        // When Then
        assertThrows(
                FavoriteConflictException.class,
                () -> favoriteService.createFavorite(createFavoriteRequest),
                "should throw"
        );
    }

    @Test
    @DisplayName("즐겨찾기를 생성하고자 하는 멤버가 존재하지 않는 멤버라면 추가 불가")
    void failToAddForMemberNotFound() {
        // When
        FavoriteCreateCommand createFavoriteRequest = new FavoriteCreateCommand(
                논현역.getId(), 역삼역.getId(), "NOT_YOUR_MEMBER@test.com"
        );

        // Then
        assertThrows(
                MemberNotFoundException.class,
                () -> favoriteService.createFavorite(createFavoriteRequest),
                "should throw"
        );
    }

    @Test
    @DisplayName("존재하지 않는 역에 대해 즐겨찾기 추가 불가")
    void failToAddForStationNotFound() {
        // When
        FavoriteCreateCommand createFavoriteRequest = new FavoriteCreateCommand(
                논현역.getId(), 404L, 테스트계정.getEmail()
        );

        // Then
        assertThrows(
                StationNotFoundException.class,
                () -> favoriteService.createFavorite(createFavoriteRequest),
                "should throw"
        );
    }

    @Test
    @DisplayName("연결되지 않은 두 역에 대해 즐겨찾기 추가 불가")
    void failToAddForPathNotFound() {
        // Given
        Station 한남역 = stationRepository.save(한남역_엔티티);
        Station 서빙고역 = stationRepository.save(서빙고역_엔티티);
        lineRepository.save(경의중앙선_엔티티(한남역, 서빙고역));

        FavoriteCreateCommand createFavoriteRequest = new FavoriteCreateCommand(
                논현역.getId(), 서빙고역.getId(), 테스트계정.getEmail()
        );

        // When // Then
        assertThrows(
                PathNotFoundException.class,
                () -> favoriteService.createFavorite(createFavoriteRequest),
                "should throw"
        );
    }

    @Test
    @DisplayName("즐겨찾기 리스트 조회 성공")
    void succeedToFind() {
        // Given
        favoriteRepository.save(Favorite.create(논현역, 역삼역, 테스트계정));

        // When
        List<FavoriteDto> favorites = favoriteService.findFavorites(new FavoriteFindQuery(테스트계정.getEmail()));

        // Then
        assertThat(favorites).hasSize(1);
        assertThat(favorites.get(0).getSource().getName()).isEqualTo(논현역.getName());
        assertThat(favorites.get(0).getTarget().getName()).isEqualTo(역삼역.getName());
    }

    @Test
    @DisplayName("즐겨찾기가 존재하지 않아도 조회 성공")
    void succeedToFindButEmpty() {
        // When
        List<FavoriteDto> favorites = favoriteService.findFavorites(new FavoriteFindQuery(테스트계정.getEmail()));

        // Then
        assertThat(favorites).hasSize(0);
    }

    @Test
    @DisplayName("즐겨찾기 삭제 성공")
    void succeedToDelete() {
        // Given
        Favorite favorite = favoriteRepository.save(Favorite.create(논현역, 역삼역, 테스트계정));

        // When
        FavoriteDeleteCommand command = new FavoriteDeleteCommand(favorite.getId(), 테스트계정.getEmail());
        favoriteService.deleteFavorite(command);

        // Then
        assertThat(favoriteRepository.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("즐겨찾기를 삭제하려는 멤버가 삭제하려는 즐겨찾기의 멤버와 다르면 실패")
    void failToDeleteForDifferentMember() {
        // Given
        Favorite favorite = favoriteRepository.save(Favorite.create(논현역, 역삼역, 테스트계정));

        // When
        FavoriteDeleteCommand command = new FavoriteDeleteCommand(
                favorite.getId(), "NOT_YOUR_MEMBER@test.com"
        );

        // Then
        assertThrows(
                FavoriteMemberException.class,
                () -> favoriteService.deleteFavorite(command),
                "should throw"
        );
    }

    @Test
    @DisplayName("삭제하려는 즐겨찾기를 찾을 수 없다면 실패")
    void failToDeleteForNotFoundFavorite() {
        // Given
        favoriteRepository.save(Favorite.create(논현역, 역삼역, 테스트계정));

        // When
        FavoriteDeleteCommand command = new FavoriteDeleteCommand(404L, 테스트계정.getEmail());

        // When // Then
        assertThrows(
                FavoriteNotFoundException.class,
                () -> favoriteService.deleteFavorite(command),
                "should throw"
        );
    }
}
