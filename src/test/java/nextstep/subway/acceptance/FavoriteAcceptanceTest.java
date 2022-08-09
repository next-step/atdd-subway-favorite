package nextstep.subway.acceptance;

import org.junit.jupiter.api.Test;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * Given : 로그인한 사용자가
     * Then  : 즐겨찾기에 출발역과 도착역을 등록합니다.
     */
    @Test
    void addFavorite() {

    }

    /**
     * Given : 로그인하지 않은 사용자가
     * When  : 즐겨찾기를 추가하려하면
     * Then : 401 Unauthorized 응답을 받습니다.
     */
    @Test
    void addFavorite_unauthorized() {

    }

    /**
     * Given : 로그인한 사용자가
     * When  : 즐겨찾기에 출발역과 도착역을 등록하면
     * Then : 자신의 즐겨찾기 목록에서 찾을 수 있습니다.
     */
    @Test
    void getFavorites() {

    }

    /**
     * Given : 로그인하지 않은 사용자가
     * When  : 즐겨찾기를 조회하려하면
     * Then : 401 Unauthorized 응답을 받습니다.
     */
    @Test
    void getFavorites_unauthorized() {

    }

    /**
     * Given : 로그인한 사용자가
     * When  : 즐겨찾기에 출발역과 도착역을 등록하고
     * Then : 즐겨찾기를 삭제합니다.
     */
    @Test
    void deleteFavorite() {

    }

    /**
     * Given : 로그인하지 않은 사용자가
     * When  : 즐겨찾기를 삭제하려하면
     * Then : 401 Unauthorized 응답을 받습니다.
     */
    @Test
    void deleteFavorite_unauthorized() {

    }

}
