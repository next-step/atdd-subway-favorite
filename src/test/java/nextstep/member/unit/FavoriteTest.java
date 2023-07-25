package nextstep.member.unit;

import nextstep.exception.favoriteException.FavoriteException;
import nextstep.member.domain.Favorite;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {

    private Station 마들역;
    private Station 노원역;
    private Station 상계역;
    private Station 당고계역;

    private Section 노원_마들;
    private Section 상계_당고계;

    private Line 칠호선;
    private Line 사호선;

    @BeforeEach
    void setUp() {
        칠호선 = new Line();
        사호선 = new Line();

        마들역 = new Station(1L, "마들역");
        노원역 = new Station(2L, "노원역");
        상계역 = new Station(3L, "상계역");
        당고계역 = new Station(4L, "당고계역");

        노원_마들 = new Section(칠호선, 노원역, 마들역, 10);
        상계_당고계 = new Section(사호선, 상계역, 당고계역, 10);
    }

    @Test
    void createFavorite() {
        //given
        Favorite favorite = Favorite.of(1L, 노원역, 마들역, List.of(노원_마들, 상계_당고계));

        //then
        assertThat(favorite.getSource()).isEqualTo(노원역);
        assertThat(favorite.getTarget()).isEqualTo(마들역);
    }

    @Test
    void createFavoriteWithUnconnectedStation() {
        //then
        assertThatThrownBy(() -> Favorite.of(1L, 마들역, 상계역, List.of(노원_마들, 상계_당고계)))
                .isInstanceOf(FavoriteException.class);
    }

    @Test
    void hasMemberId() {
        //given
        Favorite favorite = Favorite.of(1L, 노원역, 마들역, List.of(노원_마들, 상계_당고계));

        //then
        assertThat(favorite.hasMemberId(1L)).isTrue();
        assertThat(favorite.hasMemberId(2L)).isFalse();
    }
}
