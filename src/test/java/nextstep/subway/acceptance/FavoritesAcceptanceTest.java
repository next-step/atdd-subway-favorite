package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기")
public class FavoritesAcceptanceTest extends AcceptanceTest {

	/**
	 *     Given 지하철역 등록되어 있음
	 *     And 지하철 노선 등록되어 있음
	 *     And 지하철 노선에 지하철역 등록되어 있음
	 *     And 회원 등록되어 있음
	 *     And 로그인 되어있음
	 */
	private void init() {

	}

	/**
	 *     When 즐겨찾기 생성을 요청
	 *     Then 즐겨찾기 생성됨
	 */
	@DisplayName("즐겨찾기를 생성하다.")
	@Test
	void createFavorites() {
		init();

	}

	/**
	 *  	 given 즐겨찾기 생성
	 *     When 즐겨찾기 목록 조회 요청
	 *     Then 즐겨찾기 목록 조회됨
	 */
	@DisplayName("즐겨찾기를 조회하다.")
	@Test
	void showFavorites() {
		init();

	}

	/**
	 * 		given 즐겨찾기 생성
	 *    When 즐겨찾기 삭제 요청
	 *    Then 즐겨찾기 삭제됨
	 */
	@DisplayName("즐가찾기를 삭제하다")
	@Test
	void removeFavorites() {
		init();

	}
}