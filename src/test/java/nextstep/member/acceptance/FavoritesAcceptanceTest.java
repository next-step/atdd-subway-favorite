package nextstep.member.acceptance;

import nextstep.utils.AcceptanceTest;

public class FavoritesAcceptanceTest extends AcceptanceTest {

    /**
     * Given: 역과 구간을 등록한다.
     * When: 로그인 한다.
     * When: 즐겨찾기를 등록한다.
     * Then: 즐겨찾기를 조회 하면 즐겨찾기로 등록한 역이 조회된다.
     */

    /**
     * Given: 역과 구간을 등록한다.
     * When: 로그인 한다.
     * When: 이어져 있지 않은 역을 즐겨찾기로 등록한다.
     * Then: 예외가 발생한다.
     */

    /**
     * Given: 역과 구간을 등록한다.
     * When: 로그인 하지 않고 즐겨찾기를 등록한다.
     * Then: 예외가 발생한다.
     */

    /**
     * Given: 역과 구간을 등록한다.
     * When: 로그인 한다.
     * When: 즐겨찾기를 등록한다.
     * When: 즐겨찾기를 삭제한다.
     * Then: 즐겨찾기를 조회 하면 삭제한 즐겨찾기는 존재하지 않는다.
     */

    /**
     * Given: 역과 구간을 등록한다.
     * When: 로그인 한다.
     * When: 즐겨찾기를 등록한다.
     * When: 토큰 없이 즐겨찾기를 삭제한다.
     * Then: 예외가 발생한다.
     */
}
