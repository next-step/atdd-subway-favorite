package subway.acceptance.favorite;

import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.acceptance.AcceptanceTest;
import subway.utils.enums.Location;
import subway.utils.rest.Rest;

@DisplayName("즐겨찾기 인수 테스트")
class FavoriteAcceptanceTest extends AcceptanceTest {

	/**
	 * given 로그인된 회원인지 검사한다.
	 * when 즐겨찾기를 생성한다.
	 * then 생성된 즐겨찾기를 응답받는다.
	 */
	@DisplayName("즐겨찾기를 생성한다.")
	@Test
	void saveFavorite() {
		HashMap<String, String> params = new HashMap<>();
		Rest.builder()
			.uri(Location.FAVORITES.path())
			.body(params)
			.post();
	}
}
