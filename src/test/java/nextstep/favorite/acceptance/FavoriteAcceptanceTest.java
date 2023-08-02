package nextstep.favorite.acceptance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * <pre>
     *     Feature: 즐겨찾기 생성
     *     Given : 지하철역이 여러개 추가되어있다.
     *      And : 지하철역에 대한 노선이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간이 추가되어있다.
     *      And : 회원을 생성한다.
     *      And : 로그인을 한다.
     *     When : 토큰과 함께 즐겨찾기를 추가를 요청한다.
     *     Then : 즐겨찾기가 추가된다.
     * </pre>
     */
    @Test
    void createFavorite() {
        // given : 선행조건 기술

        // when : 기능 수행

        // then : 결과 확인
    }


    /**
     * <pre>
     *     Feature: 즐겨찾기 조회
     *     Given : 지하철역이 여러개 추가되어있다.
     *      And : 지하철역에 대한 노선이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간이 추가되어있다.
     *      And : 회원을 생성한다.
     *      And : 로그인을 한다.
     *      And : 토큰과 함께 즐겨찾기를 추가를 요청한다.
     *     When : 토큰과 함게 즐겨찾기를 조회한다.
     *     Then : 즐겨찾기가 조회된다.
     * </pre>
     */
    @Test
    void findFavorite() {
        // given : 선행조건 기술

        // when : 기능 수행

        // then : 결과 확인
    }

    /**
     * <pre>
     *     Feature: 즐겨찾기 삭제
     *     Given : 지하철역이 여러개 추가되어있다.
     *      And : 지하철역에 대한 노선이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간이 추가되어있다.
     *      And : 회원을 생성한다.
     *      And : 로그인을 한다.
     *      And : 토큰과 함께 즐겨찾기를 추가를 요청한다.
     *     When : 토큰과 함게 즐겨찾기를 삭제한다.
     *     Then : 즐겨찾기가 삭제된다.
     * </pre>
     */
    @Test
    void deleteFavorite() {
        // given : 선행조건 기술

        // when : 기능 수행

        // then : 결과 확인
    }

    /**
     * <pre>
     *     Feature: 401 Unauthorized 에러 발생
     *     Given : 지하철역이 여러개 추가되어있다.
     *      And : 지하철역에 대한 노선이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간이 추가되어있다.
     *     When : 즐겨찾기를 추가를 요청한다.
     *     Then : 401 Unauthorized 에러가 발생한다.
     * </pre>
     */
    @Test
    void unauthorized() {
        // given : 선행조건 기술

        // when : 기능 수행

        // then : 결과 확인
    }

    /**
     * <pre>
     *     Feature: 비정상 경로를 즐겨찾기로 등록하는 경우
     *     Given : 지하철역이 여러개 추가되어있다.
     *      And : 지하철역에 대한 노선이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간이 추가되어있다.
     *      And : 지하철역에 대한 노선에 대한 구간에 대한 거리가 추가되어있다.
     *      And : 회원을 생성한다.
     *      And : 로그인을 한다.
     *     When : 토큰과 함께 구간에 없는 역을 즐겨찾기를 추가를 요청한다.
     *     Then : 예외를 응답한다.
     * </pre>
     */
    @DisplayName("비정상 경로를 즐겨찾기로 등록하는 경우")
    @Test
    void createFavoriteReturnException() {
        // given : 선행조건 기술

        // when : 기능 수행

        // then : 결과 확인
    }
}
