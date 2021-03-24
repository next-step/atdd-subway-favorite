package nextstep.subway.favorite.domain;

import nextstep.subway.favorite.exception.SameStationsException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {

    @Test
    void getSourceTest() {
        Station source = new Station("강남역");
        Station target = new Station("역삼역");

        Favorite favorite = new Favorite(source, target);

        assertThat(favorite.getSource()).isEqualTo(source);
    }

    @Test
    void getTargetTest() {
        Station source = new Station("강남역");
        Station target = new Station("역삼역");

        Favorite favorite = new Favorite(source, target);

        assertThat(favorite.getTarget()).isEqualTo(target);
    }

    @Test
    void newTest_whenStationEquals() {
        Station source = new Station("강남역");
        Station target = new Station("강남역");

        assertThatThrownBy( () -> new Favorite(source, target)).isInstanceOf(SameStationsException.class);
    }

}
