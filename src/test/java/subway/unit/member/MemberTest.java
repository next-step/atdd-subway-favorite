package subway.unit.member;

import static org.assertj.core.api.Assertions.*;
import static subway.fixture.favorite.FavoriteEntityFixture.*;
import static subway.fixture.member.MemberEntityFixture.*;
import static subway.fixture.station.StationEntityFixture.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.favorite.Favorite;
import subway.member.Member;
import subway.station.Station;

@DisplayName("Member 도메인 테스트")
class MemberTest {
	private Member 멤버;

	@BeforeEach
	void setUp() {
		멤버 = 멤버_생성();
	}

	@DisplayName("즐겨찾기 추가")
	@Test
	void successAddFavorite() {
		// given
		Favorite favorite = 즐겨찾기_생성();

		// when
		멤버.addFavorite(favorite);

		// then
		assertThat(멤버.getFavoriteList())
			.hasSize(1)
			.usingRecursiveComparison()
			.isEqualTo(List.of(favorite));
	}

	@DisplayName("Source Station과 Target Station은 같을 수 없다.")
	@Test
	void failAddFavoriteSameStation() {
		// given
		Station 불광역 = 정류장_생성("불광역");
		Favorite favorite = 즐겨찾기_생성(불광역, 불광역);

		// when
		// then
		assertThatThrownBy(() -> 멤버.addFavorite(favorite))
			.hasMessage("출발역과 도착역은 동일할 수 없습니다.")
			.isInstanceOf(IllegalArgumentException.class);
	}
}
