package nextstep.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class FavoriteRepositoryTest {

    public static final String TEST_EMAIL = "email@email.com";
    public static final int SOURCE = 1;
    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    StationRepository stationRepository;
    Station gyoDaeStation;
    Station gangnamStation;
    Station yangjaeStation;
    Favorite favorite1;
    Favorite favorite2;

    @BeforeEach
    void setUp() {
        gyoDaeStation = stationRepository.save(new Station("교대역"));
        gangnamStation = stationRepository.save(new Station("강남역"));
        yangjaeStation = stationRepository.save(new Station("양재역"));
        favorite1 = favoriteRepository.save(new Favorite(TEST_EMAIL, gyoDaeStation, yangjaeStation));
        favorite2 = favoriteRepository.save(new Favorite(TEST_EMAIL, yangjaeStation, gangnamStation));
    }

    @AfterEach
    void clear() {
        favoriteRepository.deleteAll();
        stationRepository.deleteAll();
    }

    @DisplayName("즐겨 찾기를 저장하면 아이디를 부여한다")
    @Test
    void save() {
        // when
        Favorite save = favoriteRepository.save(favorite1);

        // then
        Assertions.assertAll(
                () -> assertThat(save.getId()).isEqualTo(SOURCE),
                () -> assertThat(save.getEmail()).isEqualTo(TEST_EMAIL),
                () -> assertThat(save.getSource()).usingRecursiveComparison().isEqualTo(gyoDaeStation),
                () -> assertThat(save.getTarget()).usingRecursiveComparison().isEqualTo(yangjaeStation)
        );
    }

    @DisplayName("유저 이메일로 모든 즐겨 찾기를 가져온다")
    @Test
    void findAllByEmail() {
        // when
        List<Favorite> favorites = favoriteRepository.findAllByEmail(TEST_EMAIL);

        // then
        assertThat(favorites).usingRecursiveComparison().isEqualTo(List.of(favorite1, favorite2));
    }
}
