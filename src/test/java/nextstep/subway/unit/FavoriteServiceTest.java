package nextstep.subway.unit;

import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteReadResponse;
import nextstep.subway.applicaion.message.Message;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("단위 테스트 : FavoriteService")
@Import({StationService.class, FavoriteService.class})
@DataJpaTest
public class FavoriteServiceTest {
    @Autowired
    StationRepository stationRepository;
    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    FavoriteService favoriteService;
    @PersistenceContext
    EntityManager entityManager;
    Station station1;
    Station station2;
    FavoriteCreateRequest request;

    @BeforeEach
    public void setUp() {
        station1 = new Station("강남역");
        ReflectionTestUtils.setField(station1, "id", 1L);
        station2 = new Station("판교역");
        ReflectionTestUtils.setField(station2, "id", 2L);

        stationRepository.save(station1);
        stationRepository.save(station2);

        request = new FavoriteCreateRequest(station1.getId(), station2.getId());
    }

    @AfterEach
    public void setDown() {
        List<String> tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(entity -> entity.getJavaType().getAnnotation(Entity.class) != null)
                .map(EntityType::getName)
                .collect(Collectors.toList());
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Test
    @DisplayName("성공 : 역 즐겨찾기 생성")
    void create_favorite_success() {
        Favorite favorite = favoriteService.createFavorite(request);
        assertThat(favorite.getSource().getName()).isEqualTo(station1.getName());
        assertThat(favorite.getTarget().getName()).isEqualTo(station2.getName());
    }

    @Test
    @DisplayName("실패 : 역 즐겨찾기 생성 : source, target이 같은 경우")
    void create_favorite_fail() {
        FavoriteCreateRequest request = new FavoriteCreateRequest(station1.getId(), station1.getId());
        assertThatThrownBy(() -> {
            favoriteService.createFavorite(request);
        }).hasMessage(Message.SOURCE_TARGET_DUPLICATE_STATION.getMessage());
    }

    @Test
    @DisplayName("성공 : 역 즐겨찾기 조회")
    void read_favorites() {
        favoriteService.createFavorite(request);
        assertThat(favoriteService.readFavorites())
                .extracting(FavoriteReadResponse::getSource)
                .extracting(FavoriteReadResponse.FavoriteReadStationResponse::getName)
                .contains("강남역");
        assertThat(favoriteService.readFavorites())
                .extracting(FavoriteReadResponse::getTarget)
                .extracting(FavoriteReadResponse.FavoriteReadStationResponse::getName)
                .contains("판교역");
    }

    @Test
    @DisplayName("성공 : 역 삭제")
    void delete_favorite() {
        Favorite favorite = favoriteService.createFavorite(request);
        favoriteService.deleteFavorite(favorite.getId());
        assertThat(favoriteRepository.findById(favorite.getId())).isEmpty();
    }

}
