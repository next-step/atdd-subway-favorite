package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * Given : jwt 로그인을 하고
     * When  : 구간 즐겨찾기를 하면
     * Then  : 즐겨찾기가 등록된다
     */
    @DisplayName("즐거찾기 등록")
    @Test
    void favoriteCreate() {
    }

    /**
     * Given : jwt 로그인을 하고
     * and   : 즐겨찾기 등록을 하고
     * When  : 즐겨찾기 조회를 하면
     * Then  : 즐겨찾기 목록이 조회된다
     */
    @DisplayName("증겨 찾기 조회")
    @Test
    void favoriteList() {
    }

    /**
     * Given : jwt 로그인을 하고
     * and   : 즐겨찾기 등록을 하고
     * When  : 즐겨찾기 삭제를 하면
     * Then  : 즐겨찾기가 삭제된다
     */
    @DisplayName("즐겨찾기 삭제")
    @Test
    void favoriteDelete() {
    }
}
