package nextstep.subway.unit;

import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.PathRequest;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

public final class FavoriteUnitTestHelper {

    private static Line 이호선;
    private static Line 신분당선;
    public static Station 강남역;
    public static Station 역삼역;
    public static Station 판교역;
    public static Station 정자역;
    public static Member member;
    public static FavoriteRequest favoriteRequest;
    public static List<Line> lines = new ArrayList<>();
    public static Favorite 강남_TO_역삼;
    public static Favorite 정자_TO_판교;
    public static List<Favorite> favorites = new ArrayList<>();
    public static FavoriteResponse favoriteResponse;

    public static void createLines() {
        이호선 = new Line("2호선", "bg-green-600");
        신분당선 = new Line("신분당선", "bg-red-600");
        lines.add(이호선);
        lines.add(신분당선);
    }

    public static void createStations() {
        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        역삼역 = new Station("역삼역");
        ReflectionTestUtils.setField(역삼역, "id", 2L);
        정자역 = new Station("정자역");
        ReflectionTestUtils.setField(정자역, "id", 3L);
        판교역 = new Station("판교역");
        ReflectionTestUtils.setField(판교역, "id", 4L);
    }

    public static void createSections() {
        이호선.addSection(new Section(이호선, 강남역, 역삼역, 10));
    }

    public static void createMember() {
        member = new Member();
        ReflectionTestUtils.setField(member, "id", 1L);
    }

    public static void createFavorites() {
        강남_TO_역삼 = Favorite.of(강남역, 역삼역, member);
        ReflectionTestUtils.setField(강남_TO_역삼, "id", 1L);

        정자_TO_판교 = Favorite.of(정자역, 판교역, member);
        ReflectionTestUtils.setField(강남_TO_역삼, "id", 2L);

        favorites.add(강남_TO_역삼);
        favorites.add(정자_TO_판교);
    }

    public static FavoriteRequest createFavoriteRequest() {
        favoriteRequest = new FavoriteRequest();
        ReflectionTestUtils.setField(favoriteRequest, "source", 1L);
        ReflectionTestUtils.setField(favoriteRequest, "target", 2L);
        return favoriteRequest;
    }

    public static FavoriteResponse createFavoriteResponse() {
        favoriteResponse = FavoriteResponse.of(강남_TO_역삼);
        ReflectionTestUtils.setField(favoriteResponse, "id", 1L);
        return favoriteResponse;
    }
}
