package nextstep.favorite.acceptance;

import io.restassured.RestAssured;
import nextstep.support.AcceptanceTest;
import nextstep.support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.UNDEFINED_PORT;

@AcceptanceTest
public class FavoriteAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
        }
        databaseCleanup.execute();
    }

    /**
     * <pre>
     * Given 로그인을 하고
     * When 경로를 즐겨찾기에 등록하면
     * Then 즐겨찾기 목록 조회 시 등록한 경로를 찾을 수 있다
     * </pre>
     */
    @DisplayName("경로를 즐겨찾기에 등록한다.")
    @Test
    void createFavoritePath() {

    }

    /**
     * <pre>
     * When 로그인을 하지 않고 경로를 즐겨찾기에 등록하려하면
     * Then 즐겨찾기 등록에 실패한다.
     * </pre>
     */
    @DisplayName("비로그인 상태로 경로를 즐겨찾기에 등록하면 실패한다.")
    @Test
    void createFavoritePathWhenNonLoggedIn() {

    }

    /**
     * <pre>
     * Given 2개의 경로를 즐겨찾기에 등록하고
     * When 즐겨찾기 목록을 조회하면
     * Then 즐겨 찾기 목록 조회 시 2개의 경로를 조회할 수 있다.
     * </pre>
     */
    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void readFavorites() {

    }

    /**
     * <pre>
     * Given 2개의 경로를 즐겨찾기에 등록하고
     * When 로그인을 하지 않고 즐겨찾기 목록을 조회하면
     * Then 즐겨 찾기 목록을 조회할 수 없다.
     * </pre>
     */
    @DisplayName("비로그인 상태로 즐겨찾기 목록을 조회할 수 없다.")
    @Test
    void readFavoritesWhenNonLoggedIn() {

    }

    /**
     * <pre>
     * Given 경로를 즐겨찾기에 등록하고
     * When 즐겨찾기에 등록한 경로를 즐겨찾기 목록에서 삭제하면
     * Then 해당 경로는 즐겨찾기 목록에서 찾을 수 없다.
     * </pre>
     */
    @DisplayName("즐겨찾기 항목을 삭제한다.")
    @Test
    void deleteFavorite() {

    }

    /**
     * <pre>
     * Given 경로를 즐겨찾기에 등록하고
     * When 로그인을 하지 않고 즐겨찾기에 등록한 경로를 즐겨찾기 목록에서 삭제하면
     * Then 즐겨찾기 항목 삭제에 실패한다.
     * </pre>
     */
    @DisplayName("비로그인 상태로 즐겨찾기 항목을 삭제할 수 없다.")
    @Test
    void deleteFavoriteWhenNonLoggedIn() {
        
    }

}
