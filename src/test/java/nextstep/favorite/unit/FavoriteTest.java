package nextstep.favorite.unit;

import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.subway.entity.Line;
import nextstep.subway.entity.Section;
import nextstep.subway.entity.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void 즐겨찿기_생성() {
        // given
        Member 사용자 = new Member("user@email.com", "password", 20);

        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");

        Line 이호선 = new Line("2호선", "green");
        이호선.addSection(new Section(이호선, 교대역, 강남역, 10));

        // when
        Favorite 즐겨찾기 = new Favorite(사용자, 교대역, 강남역, List.of(이호선.getSections()));

        // then
        assertThat(즐겨찾기.getSourceStation()).isEqualTo(교대역);
        assertThat(즐겨찾기.getTargetStation()).isEqualTo(강남역);
    }

    @DisplayName("출발역과 도착역이 동일한 경우 즐겨찾기 등록 시 에러가 발생한다.")
    @Test
    void 출발역_도착역_동일_즐겨찾기_등록_실패() {
        // given
        Member 사용자 = new Member("user@email.com", "password", 20);

        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");

        Line 이호선 = new Line("2호선", "green");
        이호선.addSection(new Section(이호선, 교대역, 강남역, 10));

        // then
        assertThatThrownBy(() -> new Favorite(사용자, 교대역, 교대역, List.of(이호선.getSections())))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("출발역과 도착역은 동일할 수 없다");
    }

    @DisplayName("연결되지 않은 경인 경우 즐겨찾기 등록 시 에러가 발생한다.")
    @Test
    void 연결되지_않은_역_즐겨찾기_등록_실패() {
        // given
        Member 사용자 = new Member("user@email.com", "password", 20);

        Station 교대역 = new Station("교대역");
        Station 강남역 = new Station("강남역");

        Line 이호선 = new Line("2호선", "green");
        이호선.addSection(new Section(이호선, 교대역, 강남역, 10));

        Station 연결되지_않은_역 = new Station("연결되지 않은 역");

        // then
        assertThatThrownBy(() -> new Favorite(사용자, 교대역, 연결되지_않은_역, List.of(이호선.getSections())))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("출발역과 도착역이 연결되어 있어야 한다.");
    }
}
