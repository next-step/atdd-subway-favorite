package nextstep.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
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
class MemberRepositoryTest {

    private static final String EMAIL = "eamil@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    StationRepository stationRepository;
    @Autowired
    FavoriteRepository favoriteRepository;
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

    @DisplayName("유저를 저장하면 유저 이메일로 찾을 수 있다.")
    @Test
    void saveMember() {
        // given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Member saveMember = memberRepository.save(member);
        saveMember.addFavorite(favorite1);
        saveMember.addFavorite(favorite2);
        entityManager.flush();

        // when
        Member findedMember = memberRepository.findByEmail(EMAIL).orElseThrow();

        // then
        Assertions.assertAll(
                () -> assertThat(findedMember.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(findedMember.getAge()).isEqualTo(AGE)
        );
    }
}
