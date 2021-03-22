package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Favorites 단위 테스트")
public class FavoritesTest {
    private final Member member = new Member("email@email.com", "username", 20);
    private final Station source  = new Station("source");
    private final Station target  = new Station("target");
    private final Favorite favorite = new Favorite();

    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    public void 즐겨찾기_추가_테스트() {

    }

    @DisplayName("즐겨찾기를 중복으로 추가할 경우 Exception이 발생한.")
    @Test
    public void 즐겨찾기_중복_추가_테스트() {

    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    public void 즐겨찾기_목록_조회_테스트() {

    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    public void 즐겨찾기_삭제다_테스트() {

    }

    @DisplayName("존재하지 않는 즐겨찾기를 삭제할 경우 Exception이 발생한다.")
    @Test
    public void 즐겨찾기_삭제_실패_테스트() {

    }
}
