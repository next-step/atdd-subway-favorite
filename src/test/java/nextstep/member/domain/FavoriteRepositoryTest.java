package nextstep.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.persistence.EntityManager;
import nextstep.subway.applicaion.dto.StationData;
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
    public static final int First_INDEX = 0;
    public static final int SECOND_INDEX = 1;
    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    StationRepository stationRepository;
    @Autowired
    FavoriteDataRepository favoriteDataRepository;
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
        List<FavoriteData> favorites = favoriteDataRepository.findAllByMember(member);

        // then
        FavoriteData favoriteData1 = favorites.get(First_INDEX);
        FavoriteData favoriteData2 = favorites.get(SECOND_INDEX);

        Assertions.assertAll(
                () -> assertThat(favoriteData1.getId()).isEqualTo(favorite1.getId()),
                () -> assertThat(favoriteData1.getSource()).usingRecursiveComparison()
                        .isEqualTo(StationData.of(favorite1.getSource())),
                () -> assertThat(favoriteData1.getTarget()).usingRecursiveComparison()
                        .isEqualTo(StationData.of(favorite1.getTarget())),
                () -> assertThat(favoriteData2.getId()).isEqualTo(favorite2.getId()),
                () -> assertThat(favoriteData2.getSource()).usingRecursiveComparison()
                        .isEqualTo(StationData.of(favorite2.getSource())),
                () -> assertThat(favoriteData2.getTarget()).usingRecursiveComparison()
                        .isEqualTo(StationData.of(favorite2.getTarget()))
        );
    }
}
