package nextstep.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.persistence.EntityManager;
import nextstep.subway.applicaion.dto.StationResponse;
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

    public static final int SOURCE = 1;
    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    StationRepository stationRepository;
    @Autowired
    FavoriteResponseRepository favoriteResponseRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager entityManager;
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
        favorite1 = favoriteRepository.save(new Favorite(gyoDaeStation, yangjaeStation));
        favorite2 = favoriteRepository.save(new Favorite(yangjaeStation, gangnamStation));
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
                () -> assertThat(save.getSource()).usingRecursiveComparison().isEqualTo(gyoDaeStation),
                () -> assertThat(save.getTarget()).usingRecursiveComparison().isEqualTo(yangjaeStation)
        );
    }

    @DisplayName("유저의 모든 즐겨 찾기를 가져온다")
    @Test
    void findAllByEmail() {
        // given
        Member member = memberRepository.save(new Member("email@email.com", "password", 20));
        member.addFavorite(favorite1);
        member.addFavorite(favorite2);
        entityManager.persist(member);

        // when
        List<FavoriteResponse> favorites = favoriteResponseRepository.findAllByMember(member);

        // then
        FavoriteResponse favoriteResponse1 = favorites.get(0);
        FavoriteResponse favoriteResponse2 = favorites.get(1);
        Assertions.assertAll(
                () -> assertThat(favoriteResponse1.getId()).isEqualTo(favorite1.getId()),
                () -> assertThat(favoriteResponse1.getSource()).usingRecursiveComparison()
                        .isEqualTo(StationResponse.of(favorite1.getSource())),
                () -> assertThat(favoriteResponse1.getTarget()).usingRecursiveComparison()
                        .isEqualTo(StationResponse.of(favorite1.getTarget())),
                () -> assertThat(favoriteResponse2.getId()).isEqualTo(favorite2.getId()),
                () -> assertThat(favoriteResponse2.getSource()).usingRecursiveComparison()
                        .isEqualTo(StationResponse.of(favorite2.getSource())),
                () -> assertThat(favoriteResponse2.getTarget()).usingRecursiveComparison()
                        .isEqualTo(StationResponse.of(favorite2.getTarget()))
        );
    }
}
