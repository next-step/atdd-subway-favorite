package nextstep.core.subway.favorite.fixture;

import nextstep.core.subway.favorite.application.dto.FavoriteRequest;

public class FavoriteFixture {

    public static FavoriteRequest 추가할_즐겨찾기_정보(Long 출발역_번호, Long 도착역_번호) {
        return new FavoriteRequest(출발역_번호, 도착역_번호);
    }

    public static FavoriteRequest 확인할_즐겨찾기_정보(Long 출발역_번호, Long 도착역_번호) {
        return new FavoriteRequest(출발역_번호, 도착역_번호);
    }
}
