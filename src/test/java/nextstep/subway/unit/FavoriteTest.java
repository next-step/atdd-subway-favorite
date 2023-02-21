package nextstep.subway.unit;

import nextstep.favorite.application.exception.FavoriteCreateException;
import nextstep.favorite.application.exception.FavoriteErrorCode;
import nextstep.favorite.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static nextstep.subway.fixtures.MemberFixtures.어드민_유저;
import static nextstep.subway.fixtures.StationFixtures.강남역;
import static nextstep.subway.fixtures.StationFixtures.판교역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteTest {

    @DisplayName("즐겨찾기 생성에 성공한다")
    @Test
    void 즐겨찾기_생성에_성공한다() {
        // given
        Member 어드민_유저 = 어드민_유저();
        Station 강남역 = 강남역();
        Station 판교역 = 판교역();

        // when
        Favorite favorite = new Favorite(어드민_유저, 강남역, 판교역);

        // then
        assertAll(
                () -> assertThat(favorite.getMember()).isEqualTo(어드민_유저),
                () -> assertThat(favorite.getSource()).isEqualTo(강남역),
                () -> assertThat(favorite.getTarget()).isEqualTo(판교역)
        );
    }

    @DisplayName("즐겨찾기 생성시 출발역과 도착역이 같을경우 예외가 발생한다")
    @Test
    void 즐겨찾기_생성시_출발역과_도착역이_같을경우_예외가_발생한다() {
        Member 어드민_유저 = 어드민_유저();
        Station 강남역 = 강남역();

        assertThatThrownBy(() -> new Favorite(어드민_유저, 강남역, 강남역))
                .isInstanceOf(FavoriteCreateException.class)
                .hasMessage(FavoriteErrorCode.INVALID_CREATED_EQUAL_STATION.getMessage());
    }

    @DisplayName("즐겨찾기 생성시 출발역을 지정하지 않으면 예외가 발생한다")
    @ParameterizedTest
    @NullSource
    void 즐겨찾기_생성시_출발역을_지정하지_않으면_예외가_발생한다(Station startStation) {
        Member 어드민_유저 = 어드민_유저();
        Station 판교역 = 판교역();


        assertThatThrownBy(() -> new Favorite(어드민_유저, startStation, 판교역))
                .isInstanceOf(FavoriteCreateException.class)
                .hasMessage(FavoriteErrorCode.INVALID_CREATE_REQUEST_STATION.getMessage());
    }

    @DisplayName("즐겨찾기 생성시 도착역을 지정하지 않으면 예외가 발생한다")
    @ParameterizedTest
    @NullSource
    void 즐겨찾기_생성시_도착역을_지정하지_않으면_예외가_발생한다(Station destinationStation) {
        Member 어드민_유저 = 어드민_유저();
        Station 강남역 = 강남역();

        assertThatThrownBy(() -> new Favorite(어드민_유저, 강남역, destinationStation))
                .isInstanceOf(FavoriteCreateException.class)
                .hasMessage(FavoriteErrorCode.INVALID_CREATE_REQUEST_STATION.getMessage());
    }
}
