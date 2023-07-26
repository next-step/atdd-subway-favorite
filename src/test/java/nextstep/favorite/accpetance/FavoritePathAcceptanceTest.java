package nextstep.favorite.accpetance;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static nextstep.favorite.accpetance.FavoriteSteps.즐겨찾기_경로_등록;
import static nextstep.favorite.accpetance.FavoriteSteps.즐겨찾기_경로_등록됨;
import static nextstep.utils.AcceptanceUtils.*;

@DisplayName("경로 즐겨찾기 기능")
public class FavoritePathAcceptanceTest extends AcceptanceTest {

    Map<String, Long> stationIdByName;


    @BeforeEach
    public void setUp2() {
        //given
        stationIdByName = createStationsAndGetStationMap(List.of("혜화", "동대문", "동대문역사문화공원", "종로3가", "종로5가", "동묘앞", "양산", "남양산"));

        var line1 = createStationLine("1호선", "blue", "종로3가", "종로5가", BigDecimal.valueOf(3L), stationIdByName);
        createStationLineSection(line1, "종로5가", "동대문", BigDecimal.valueOf(5L), stationIdByName);
        createStationLineSection(line1, "동대문", "동묘앞", BigDecimal.valueOf(5L), stationIdByName);

        var line2 = createStationLine("4호선", "mint", "혜화", "동대문", BigDecimal.ONE, stationIdByName);
        createStationLineSection(line2, "동대문", "동대문역사문화공원", BigDecimal.TEN, stationIdByName);

        createStationLine("부산2호선", "red", "양산", "남양산", BigDecimal.TEN, stationIdByName);
    }

    /**
     * When 종로3가역에서 동대문역으로 경로 즐겨찾기 추가
     * Then 즐겨찾기 조회 목록에 혜화역에서 동대문으로 경로가 존재
     */
    @DisplayName("경로 즐겨찾기 등록")
    @Test
    void createFavoritePath() {
        //when
        즐겨찾기_경로_등록(사용자1_토큰, "종로3가", "동대문", stationIdByName);

        //then
        즐겨찾기_경로_등록됨(사용자1_토큰, "종로3가", "동대문", 0);
    }

    /**
     * When 비정상 토큰으로 사용자로 즐겨찾기 추가
     * Then 에러 발생
     */
    @DisplayName("비정상 토큰으로 경로 즐겨찾기 등록")
    @Test
    void createFavoritePath_With_InvalidToken() {
        //when & then
        즐겨찾기_경로_등록("invalidToken", "종로3가", "동대문", stationIdByName, HttpStatus.UNAUTHORIZED);
    }

    /**
     * When 존재하지 않는 경로의 즐겨찾기 추가
     * Then 에러 발생
     */
    @DisplayName("존재하지 않는 경로 즐겨찾기 등록")
    @Test
    void createFavoritePath_InvalidPath() {
        //when & then
        즐겨찾기_경로_등록(사용자1_토큰, "종로3가", "양산", stationIdByName, HttpStatus.BAD_REQUEST);
    }

    /**
     * Given (종로3가, 종로5가) 경로 즐겨찾기 추가
     * And (동대문, 동묘앞) 경로 즐겨찾기 추가
     * When 즐겨찾기 목록 조회
     * Then 추가한 즐겨찾기 경로 2개가 조회된다
     */
    @DisplayName("경로 즐겨찾기 등록")
    @Test
    void getFavoritePaths() {
        //given
        즐겨찾기_경로_등록(사용자1_토큰, "종로3가", "종로5가", stationIdByName);
        즐겨찾기_경로_등록(사용자1_토큰, "동대문", "동묘앞", stationIdByName);

        //when & then
        즐겨찾기_경로_등록됨(사용자1_토큰, "종로3가", "종로5가", 0);
        즐겨찾기_경로_등록됨(사용자1_토큰, "동대문", "동묘앞", 1);
    }

}
